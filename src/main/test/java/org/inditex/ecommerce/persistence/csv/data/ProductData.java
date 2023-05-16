package org.inditex.ecommerce.persistence.csv.data;

import org.iditex.ecommerce.model.entities.Product;
import org.inditex.ecommerce.persistence.csv.entities.ProductDto;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductData {

    public static final Map<Long, Product> PRODUCTS = Stream.of(
            new Product(1L, 10L, SizeData.findByProductId(1L)),
            new Product(2L, 7L, SizeData.findByProductId(2L)),
            new Product(3L, 15L, SizeData.findByProductId(3L)),
            new Product(4L, 13L, SizeData.findByProductId(4L)),
            new Product(5L, 6L, SizeData.findByProductId(5L))
    ).collect(Collectors.groupingBy(Product::getId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static final Map<Long, ProductDto> DTOS = Stream.of(
            getDto(1L, 10L, SizeData.findDtoByProductId(1L)),
            getDto(2L, 7L, SizeData.findDtoByProductId(2L)),
            getDto(3L, 15L, SizeData.findDtoByProductId(3L)),
            getDto(4L, 13L, SizeData.findDtoByProductId(4L)),
            getDto(5L, 6L, SizeData.findDtoByProductId(5L))
    ).collect(Collectors.groupingBy(ProductDto::getId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static Set<Product> findAll() {
        return new HashSet<>(PRODUCTS.values());
    }

    private static ProductDto getDto(Long id, Long sequence, Set<SizeDto> sizes) {
        ProductDto dto = new ProductDto();
        dto.setId(id);
        dto.setSequence(sequence);
        dto.setSizes(sizes);
        return dto;
    }

}
