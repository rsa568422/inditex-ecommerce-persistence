package org.iditex.ecommerce.persistence.repositories.csv;

import org.iditex.ecommerce.model.repositories.RepositoryException;

public class CsvRepositoryException extends RepositoryException {

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
