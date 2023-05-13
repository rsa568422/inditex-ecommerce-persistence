package org.iditex.ecommerce.persistence.repositories.csv;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CsvRepository<T> {

    protected final List<T> elements;

    protected CsvRepository(String fileName) {
        this.elements = readFromCsv(fileName);
    }

    public List<T> findAll() {
        return elements;
    }

    public void close() throws Exception {

    }

    protected List<T> readFromCsv(String fileName) {
        try {
            return Files.readAllLines(Path.of(ROOT.concat(fileName)))
                    .stream()
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new CsvRepositoryException(e);
        }
    }

    protected abstract T parse(String line);

    private static final String ROOT = FileSystems.getDefault()
            .getPath("")
            .normalize()
            .toAbsolutePath()
            .toString()
            .concat(Paths.RESOURCES.toString());

    public enum Paths {
        RESOURCES ("\\src\\main\\resources\\"),
        PRODUCTS ("product.csv"),
        SIZES ("size-1.csv"),
        STOCKS ("stock.csv");

        private final String path;

        Paths(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

}
