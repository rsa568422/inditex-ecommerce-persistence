package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Product;
import org.iditex.ecommerce.model.repositories.ProductRepository;
import org.inditex.ecommerce.persistence.csv.entities.ProductDto;

import java.util.Set;

public class ProductCsvRepository extends CsvRepository<ProductDto, Product> implements ProductRepository {

    private static final SizeCsvRepository SIZE_CSV_REPOSITORY = new SizeCsvRepository();

    @Override
    public Set<Product> findAll() {
        return this.elements;
    }

    @Override
    protected Product parse(String line) {
        String[] columns = line.split(",");
        Long id = Long.valueOf(columns[0].trim());
        return new Product(
                id,
                Long.valueOf(columns[1].trim()),
                SIZE_CSV_REPOSITORY.findByProductId(id)
        );
    }

    @Override
    protected Class<ProductDto> getDtoClass() {
        return ProductDto.class;
    }

    @Override
    protected Class<Product> getEntityClass() {
        return Product.class;
    }

}
