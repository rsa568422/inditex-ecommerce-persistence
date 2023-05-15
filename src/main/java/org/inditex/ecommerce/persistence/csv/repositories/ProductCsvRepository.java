package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Product;
import org.iditex.ecommerce.model.repositories.ProductRepository;
import org.inditex.ecommerce.persistence.csv.entities.ProductDto;

import java.util.Set;

public class ProductCsvRepository extends CsvRepository<ProductDto, Product> implements ProductRepository {

    @Override
    public Set<Product> findAll() {
        return this.elements;
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
