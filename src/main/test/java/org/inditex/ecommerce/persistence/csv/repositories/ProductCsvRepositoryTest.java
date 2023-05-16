package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Product;
import org.inditex.ecommerce.persistence.csv.data.ProductData;
import org.inditex.ecommerce.persistence.csv.entities.ProductDto;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCsvRepositoryTest {

    private static final ProductCsvRepository REPOSITORY = new ProductCsvRepository();

    @Test
    void testFindVisiblesOrderBySequence() {
        StringJoiner joiner = new StringJoiner(", ");
        REPOSITORY.findVisiblesOrderBySequence().stream().map(Product::getId).map(Objects::toString).forEach(joiner::add);
        String visibleProducts = joiner.toString();

        assertEquals("5, 1, 3", visibleProducts);
    }

    @Test
    void testFindAll() {
        assertEquals(ProductData.findAll(), REPOSITORY.findAll());
    }

    @Test
    void testGetDtoClass() {
        assertEquals(ProductDto.class, REPOSITORY.getDtoClass());
    }
}