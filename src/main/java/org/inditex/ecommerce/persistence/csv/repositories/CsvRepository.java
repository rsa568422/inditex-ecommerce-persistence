package org.inditex.ecommerce.persistence.csv.repositories;

import org.inditex.ecommerce.persistence.csv.annotations.Source;
import org.inditex.ecommerce.persistence.csv.exceptions.CsvRepositoryException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CsvRepository<D, E> {

    protected final Set<E> elements;

    protected CsvRepository() {
        this.elements = readFromCsv(String.format("%s.csv", getDtoClass().getAnnotation(Source.class).fileName()));
    }

    protected Set<E> readFromCsv(String fileName) {
        return readCsv(fileName)
                .map(this::parse)
                .collect(Collectors.toSet());
    }

    protected Stream<String> readCsv(String fileName) {
        try {
            return Files.readAllLines(Path.of(ROOT.concat(fileName)))
                    .stream();
        } catch (IOException e) {
            throw new CsvRepositoryException(e);
        }
    }

    protected abstract E parse(String line);

    protected abstract Class<D> getDtoClass();

    protected abstract Class<E> getEntityClass();

    private static final String RESOURCES = "\\src\\main\\resources\\";

    private static final String ROOT = FileSystems.getDefault()
            .getPath("")
            .normalize()
            .toAbsolutePath()
            .toString()
            .concat(RESOURCES);

}
