package org.inditex.ecommerce.persistence.csv.repositories;

import org.inditex.ecommerce.persistence.csv.data.StockData;
import org.inditex.ecommerce.persistence.csv.entities.StockDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StockCsvRepositoryTest {

    private static final StockCsvRepository REPOSITORY = new StockCsvRepository();

    @ParameterizedTest(name = "testFindBySizeId for id {argumentsWithNames}")
    @ValueSource(strings = {"11", "12", "13", "21", "22", "23", "31", "32", "33", "41", "42", "43", "44", "51", "52", "53", "54"})
    void testFindBySizeId(String param) {
        Long id = Long.valueOf(param);

        assertEquals(StockData.findBySizeId(id), REPOSITORY.findBySizeId(id));
    }

    @Test
    void testGetDtoClass() {
        assertEquals(StockDto.class, REPOSITORY.getDtoClass());
    }
}