package org.inditex.ecommerce.persistence.csv.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.inditex.ecommerce.persistence.csv.annotations.Column;
import org.inditex.ecommerce.persistence.csv.annotations.OneToMany;
import org.inditex.ecommerce.persistence.csv.annotations.PrimaryKey;
import org.inditex.ecommerce.persistence.csv.annotations.Source;
import org.inditex.ecommerce.persistence.csv.exceptions.CsvRepositoryException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CsvRepository<D, E> {

    protected final Set<E> elements;

    protected CsvRepository() {
        this.elements = getFromCsv(String.format("%s.csv", getDtoClass().getAnnotation(Source.class).fileName()));
    }

    protected abstract Class<D> getDtoClass();

    protected abstract Class<E> getEntityClass();

    private static Stream<String> readCsv(String fileName) {
        try {
            return Files.readAllLines(Path.of(ROOT.concat(fileName)))
                    .stream();
        } catch (IOException e) {
            throw new CsvRepositoryException(e);
        }
    }

    private Set<E> getFromCsv(String fileName) {
        Gson gson = new GsonBuilder().create();
        return readCsv(fileName)
                .map(CsvRepository::getColumns)
                .map(columns -> parse2(getDtoClass(), columns))
                .map(dto -> gson.fromJson(gson.toJson(dto), getEntityClass()))
                .collect(Collectors.toSet());
    }

    private static <T> T parse2(Class<T> dtoClass, String[] columns) {
        try {
            T dto = dtoClass.getConstructor().newInstance();
            setColumns(dtoClass, dto, columns);
            setOneToMany(dtoClass);
            setOneToOne();
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

    private static <T> void setOneToMany(Class<T> dtoClass) {
        Arrays.stream(dtoClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class))
                .forEach(field -> {
                    OneToMany annotation = field.getAnnotation(OneToMany.class);
                });
    }

    private static void setOneToOne() {
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
