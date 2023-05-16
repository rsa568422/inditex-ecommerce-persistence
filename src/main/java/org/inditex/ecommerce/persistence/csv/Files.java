package org.inditex.ecommerce.persistence.csv;

public enum Files {

    PRODUCTS("product"),
    SIZES("size-1"),
    STOCKS("stock");

    private final String fileName;

    Files(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
