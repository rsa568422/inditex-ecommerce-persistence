package org.inditex.ecommerce.persistence.csv.repositories;

import org.inditex.ecommerce.persistence.csv.data.SizeData;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SizeCsvRepositoryTest {

    private static final SizeCsvRepository REPOSITORY = new SizeCsvRepository();

    @ParameterizedTest(name = "testFindByProductId for id {argumentsWithNames}")
    @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
    void testFindByProductId(String param) {
        Long id = Long.valueOf(param);

        assertEquals(SizeData.findByProductId(id), REPOSITORY.findByProductId(id));
    }

    @Test
    void testGetDtoClass() {
        assertEquals(SizeDto.class, REPOSITORY.getDtoClass());
    }
}