package org.inditex.ecommerce.persistence.csv;

import org.inditex.ecommerce.persistence.csv.repositories.ProductCsvRepository;

public class Main {

    public static void main(String[] args) {
        new ProductCsvRepository().findAll().forEach(System.out::println);
    }

}
