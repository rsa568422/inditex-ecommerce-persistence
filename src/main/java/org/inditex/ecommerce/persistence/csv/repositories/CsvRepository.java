package org.inditex.ecommerce.persistence.csv.repositories;

import org.inditex.ecommerce.persistence.csv.annotations.*;
import org.inditex.ecommerce.persistence.csv.entities.Dto;
import org.inditex.ecommerce.persistence.csv.entities.Parser;
import org.inditex.ecommerce.persistence.csv.exceptions.CsvRepositoryException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CsvRepository<D extends Dto, E> {

    protected final Set<E> elements;

    protected CsvRepository() {
        this.elements = (Set<E>) getFromCsv(getFileName(getDtoClass().getAnnotation(Source.class).fileName().toString()), getDtoClass());
    }

    protected abstract Class<D> getDtoClass();

    private static String getFileName(String source) {
        return String.format("%s.csv", source);
    }

    private static Stream<String> readCsv(String fileName) {
        try {
            return Files.readAllLines(Path.of(ROOT.concat(fileName)))
                    .stream();
        } catch (IOException e) {
            throw new CsvRepositoryException(e);
        }
    }

    private static Set<?> getFromCsv(String fileName, Class<? extends Dto> dtoClass) {
        return readCsv(fileName)
                .map(CsvRepository::getColumns)
                .map(columns -> parse(dtoClass, columns))
                .map(Parser::parse)
                .collect(Collectors.toSet());
    }

    private static <T> T parse(Class<T> dtoClass, String[] columns) {
        try {
            T dto = dtoClass.getConstructor().newInstance();
            setColumns(dtoClass, dto, columns);
            setOneToMany(dtoClass, dto);
            setOneToOne(dtoClass, dto);
            return dto;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new CsvRepositoryException(e);
        }
    }

    private static <T> void setColumns(Class<T> dtoClass, T dto, String[] columns) {
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(PrimaryKey.class))
                .forEach(field -> setValues(dtoClass, dto, field, columns));
    }

    private static <T> void setValues(Class<T> dtoClass, T dto, Field field, String[] columns) {
        Arrays.stream(dtoClass.getMethods())
                .filter(filterAccessMethod(AccessType.SETTER, field))
                .findFirst()
                .ifPresent(setter -> setValue(field.getGenericType(), setter, dto, columns, getColumnIndex(field)));
    }

    private static int getColumnIndex(Field field) {
        return field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).index() : 0;
    }

    private static Predicate<Method> filterAccessMethod(AccessType type, Field field) {
        return m -> m.getName().toLowerCase().contains(field.getName().toLowerCase()) && m.getName().toLowerCase().startsWith(type.prefix);
    }

    private static <T> void setValue(Type type, Method setter, T dto, String[] columns, int index) {
        try {
            if (type.getTypeName().equals("java.lang.Long")) {
                setter.invoke(dto, Long.valueOf(columns[index].trim()));
            } else if (type.getTypeName().equalsIgnoreCase("boolean")) {
                setter.invoke(dto, Boolean.valueOf(columns[index].trim()));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CsvRepositoryException(e);
        }
    }

    private static <T> void setOneToMany(Class<T> dtoClass, T dto) {
        String id = getId(dtoClass, dto);
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class))
                .forEach(field -> {
                    Class<?> sourceClass = field.getAnnotation(OneToMany.class).source();
                    String source = getSource(sourceClass);
                    Integer foreignKeyIndex = getForeignKeyIndex(getForeignKeyField(sourceClass, dtoClass));
                    Set<?> elements = getElements(source, sourceClass, foreignKeyIndex, id)
                            .collect(Collectors.toSet());
                    set(dtoClass, dto, field, elements);
                });
    }

    private static <T> void setOneToOne(Class<T> dtoClass, T dto) {
        String id = getId(dtoClass, dto);
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToOne.class))
                .forEach(field -> {
                    Class<?> sourceClass = field.getAnnotation(OneToOne.class).source();
                    String source = getSource(sourceClass);
                    Integer foreignKeyIndex = getForeignKeyIndex(getForeignKeyField(sourceClass, dtoClass));
                    Object element = getElements(source, sourceClass, foreignKeyIndex, id)
                            .findFirst()
                            .orElse(null);
                    set(dtoClass, dto, field, element);
                });
    }

    private static <T> String getId(Class<T> dtoClass, T dto) {
        return Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .findFirst()
                .map(field -> {
                    Method getter = Arrays.stream(dtoClass.getMethods())
                            .filter(filterAccessMethod(AccessType.GETTER, field))
                            .findFirst()
                            .orElseThrow(() -> new CsvRepositoryException("Classes with some field annotated with @OneToMany needs another field annotated with @PrimaryKey"));
                    try {
                        return getter.invoke(dto);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new CsvRepositoryException(e);
                    }
                })
                .map(String::valueOf)
                .orElse(null);
    }

    private static String getSource(Class<?> sourceDto) {
        return Optional.ofNullable(sourceDto.getAnnotation(Source.class))
                .map(Source::fileName)
                .map(org.inditex.ecommerce.persistence.csv.Files::toString)
                .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a source DTO class annotated with @Source"));
    }

    private static Field getForeignKeyField(Class<?> sourceClass, Class<?> dtoClass) {
        return Arrays.stream(sourceClass.getDeclaredFields())
                .filter(sourceField -> sourceField.isAnnotationPresent(ForeignKey.class))
                .filter(sourceField -> sourceField.getAnnotation(ForeignKey.class).origin().equals(dtoClass))
                .findFirst()
                .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a field annotated with @ForeignKey in source DTO"));
    }

    private static Integer getForeignKeyIndex(Field foreignKey) {
        return Optional.ofNullable(foreignKey.getAnnotation(Column.class))
                .map(Column::index)
                .orElseThrow(() -> new CsvRepositoryException("Field annotated with @ForeignKey must be annotated with @Column too"));
    }

    private static <T> Stream<T> getElements(String source, Class<T> sourceClass, Integer foreignKeyIndex, String id) {
        return readCsv(getFileName(source))
                .map(CsvRepository::getColumns)
                .filter(columns -> columns[foreignKeyIndex].trim().equals(id))
                .map(columns -> parse(sourceClass, columns));
    }

    private static <T> void set(Class<T> dtoClass, T dto, Field field, Object value) {
        Arrays.stream(dtoClass.getMethods())
                .filter(filterAccessMethod(AccessType.SETTER, field))
                .findFirst()
                .ifPresent(setter -> set(setter, dto, value));
    }

    private static <T> void set(Method setter, T dto, Object value) {
        try {
            setter.invoke(dto, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CsvRepositoryException(e);
        }
    }

    private static String[] getColumns(String line) {
        return line.trim().split(",");
    }

    private static final String RESOURCES = "\\src\\main\\resources\\";

    private static final String ROOT = FileSystems.getDefault()
            .getPath("")
            .normalize()
            .toAbsolutePath()
            .toString()
            .concat(RESOURCES);

    private enum AccessType {
        GETTER ("get"),
        SETTER ("set");

        private final String prefix;

        AccessType(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return prefix;
        }
    }

}
