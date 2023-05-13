package org.iditex.ecommerce.persistence.repositories.csv;

public class CsvRepositoryException extends RuntimeException {

    public CsvRepositoryException() {
    }

    public CsvRepositoryException(String message) {
        super(message);
    }

    public CsvRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvRepositoryException(Throwable cause) {
        super(cause);
    }

}
