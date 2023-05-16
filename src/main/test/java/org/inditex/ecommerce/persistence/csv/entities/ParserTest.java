package org.inditex.ecommerce.persistence.csv.entities;

import org.inditex.ecommerce.persistence.csv.data.ProductData;
import org.inditex.ecommerce.persistence.csv.data.SizeData;
import org.inditex.ecommerce.persistence.csv.data.StockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @ParameterizedTest(name = "testParseProduct for id {argumentsWithNames}")
    @ValueSource(strings = {"1", "2", "3", "4", "5"})
    void testParseProduct(String param) {
        Long id = Long.valueOf(param);
        assertEquals(ProductData.PRODUCTS.get(id), Parser.parse(ProductData.DTOS.get(id)));
    }

    @ParameterizedTest(name = "testParseSize for id {argumentsWithNames}")
    @ValueSource(strings = {"11", "12", "13", "21", "22", "23", "31", "32", "33", "41", "42", "43", "44", "51", "52", "53", "54"})
    void testParseSize(String param) {
        Long id = Long.valueOf(param);
        assertEquals(SizeData.SIZES.get(id), Parser.parse(SizeData.DTOS.get(id)));
    }

    @ParameterizedTest(name = "testParseStock for id {argumentsWithNames}")
    @ValueSource(strings = {"11", "12", "13", "22", "31", "32", "33", "41", "42", "43", "44", "51", "52", "53", "54"})
    void testParseStock(String param) {
        Long id = Long.valueOf(param);
        assertEquals(StockData.STOCKS.get(id), Parser.parse(StockData.DTOS.get(id)));
    }

    @Test
    void testParseBadObject() {

        class A implements Dto {
            private final String name;
            private final int value;

            A(String name, int value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public int getValue() {
                return value;
            }
        }

        A a = new A("test", 1);

        assertNull(Parser.parse(a));
    }

}