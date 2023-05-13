package org.iditex.ecommerce.persistence.repositories.csv;

import org.iditex.ecommerce.model.entities.Product;
import org.iditex.ecommerce.model.repositories.ProductRepository;

import static org.iditex.ecommerce.persistence.repositories.csv.CsvRepository.Paths.PRODUCTS;

public class ProductCsvRepository extends CsvRepository<Product> implements ProductRepository {

    public ProductCsvRepository() {
        super(PRODUCTS.toString());
    }

    @Override
    protected Product parse(String line) {
        String[] columns = line.split(",");
        return new Product(Long.valueOf(columns[0].trim()), Long.valueOf(columns[1].trim()));
    }
}
