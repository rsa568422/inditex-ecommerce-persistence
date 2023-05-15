package org.inditex.ecommerce.persistence.csv;

import org.inditex.ecommerce.persistence.csv.repositories.StockCsvRepository;

public class Main {

    public static void main(String[] args) {
        new StockCsvRepository().findBySizeId(31L).ifPresent(System.out::println);
    }

}
