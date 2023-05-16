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
                .forEach(field -> {
                    Type type = field.getGenericType();
                    int index = field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).index() : 0;
                    Arrays.stream(dtoClass.getMethods())
                            .filter(method -> method.getName().toLowerCase().contains(field.getName().toLowerCase()))
                            .filter(method -> method.getName().toLowerCase().startsWith("set"))
                            .findFirst()
                            .ifPresent(setter -> {
                                try {
                                    if (type.getTypeName().equals("java.lang.Long")) {
                                            setter.invoke(dto, Long.valueOf(columns[index].trim()));
                                    } else if (type.getTypeName().equalsIgnoreCase("boolean")) {
                                        setter.invoke(dto, Boolean.valueOf(columns[index].trim()));
                                    }
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new CsvRepositoryException(e);
                                }
                            });
                });
    }

    private static <D> void setOneToMany(Class<D> dtoClass, D dto) {
        String id = Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .findFirst()
                .map(field -> {
                    Method getter = Arrays.stream(dtoClass.getMethods())
                            .filter(method -> method.getName().toLowerCase().contains(field.getName().toLowerCase()))
                            .filter(method -> method.getName().toLowerCase().startsWith("get"))
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
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class))
                .forEach(field -> {
                    Class<?> sourceDto = field.getAnnotation(OneToMany.class).source();
                    String source = Optional.ofNullable(sourceDto.getAnnotation(Source.class))
                            .map(Source::fileName)
                            .map(org.inditex.ecommerce.persistence.csv.Files::toString)
                            .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a source DTO class annotated with @Source"));
                    Field foreignKey = Arrays.stream(sourceDto.getDeclaredFields())
                            .filter(sourceField -> sourceField.isAnnotationPresent(ForeignKey.class))
                            .filter(sourceField -> sourceField.getAnnotation(ForeignKey.class).origin().equals(dtoClass))
                            .findFirst()
                            .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a field annotated with @ForeignKey in source DTO"));
                    Integer foreignKeyIndex = Optional.ofNullable(foreignKey.getAnnotation(Column.class))
                            .map(Column::index)
                            .orElseThrow(() -> new CsvRepositoryException("Field annotated with @ForeignKey must be annotated with @Column too"));
                    Set<?> elements = readCsv(getFileName(source))
                            .map(CsvRepository::getColumns)
                            .filter(columns -> columns[foreignKeyIndex].trim().equals(id))
                            .map(columns -> parse(sourceDto, columns))
                            .collect(Collectors.toSet());
                    Arrays.stream(dtoClass.getMethods())
                            .filter(method -> method.getName().toLowerCase().contains(field.getName().toLowerCase()))
                            .filter(method -> method.getName().toLowerCase().startsWith("set"))
                            .findFirst()
                            .ifPresent(setter -> {
                                try {
                                    setter.invoke(dto, elements);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new CsvRepositoryException(e);
                                }
                            });
                });
    }

    private static <D> void setOneToOne(Class<D> dtoClass, D dto) {
        String id = Arrays.stream(dtoClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
            .findFirst()
            .map(field -> {
                Method getter = Arrays.stream(dtoClass.getMethods())
                        .filter(method -> method.getName().toLowerCase().contains(field.getName().toLowerCase()))
                        .filter(method -> method.getName().toLowerCase().startsWith("get"))
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
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToOne.class))
                .forEach(field -> {
                    Class<?> sourceDto = field.getAnnotation(OneToOne.class).source();
                    String source = Optional.ofNullable(sourceDto.getAnnotation(Source.class))
                            .map(Source::fileName)
                            .map(org.inditex.ecommerce.persistence.csv.Files::toString)
                            .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a source DTO class annotated with @Source"));
                    Field foreignKey = Arrays.stream(sourceDto.getDeclaredFields())
                            .filter(sourceField -> sourceField.isAnnotationPresent(ForeignKey.class))
                            .filter(sourceField -> sourceField.getAnnotation(ForeignKey.class).origin().equals(dtoClass))
                            .findFirst()
                            .orElseThrow(() -> new CsvRepositoryException("Annotation @OneToMany needs a field annotated with @ForeignKey in source DTO"));
                    Integer foreignKeyIndex = Optional.ofNullable(foreignKey.getAnnotation(Column.class))
                            .map(Column::index)
                            .orElseThrow(() -> new CsvRepositoryException("Field annotated with @ForeignKey must be annotated with @Column too"));
                    Object element = readCsv(getFileName(source))
                            .map(CsvRepository::getColumns)
                            .filter(columns -> columns[foreignKeyIndex].trim().equals(id))
                            .map(columns -> parse(sourceDto, columns))
                            .findFirst()
                            .orElse(null);
                    Arrays.stream(dtoClass.getMethods())
                            .filter(method -> method.getName().toLowerCase().contains(field.getName().toLowerCase()))
                            .filter(method -> method.getName().toLowerCase().startsWith("set"))
                            .findFirst()
                            .ifPresent(setter -> {
                                try {
                                    setter.invoke(dto, element);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new CsvRepositoryException(e);
                                }
                            });
                });
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

}
