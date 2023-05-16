package org.inditex.ecommerce.persistence.csv.data;

import org.iditex.ecommerce.model.entities.Stock;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;
import org.inditex.ecommerce.persistence.csv.entities.StockDto;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockData {

    public static final Map<Long, Stock> STOCKS = Stream.of(
            new Stock(11L, 0L),
            new Stock(12L, 0L),
            new Stock(13L, 0L),
            new Stock(22L, 0L),
            new Stock(31L, 10L),
            new Stock(32L, 10L),
            new Stock(33L, 10L),
            new Stock(41L, 0L),
            new Stock(42L, 0L),
            new Stock(43L, 0L),
            new Stock(44L, 10L),
            new Stock(51L, 10L),
            new Stock(52L, 10L),
            new Stock(53L, 10L),
            new Stock(54L, 10L)
    ).collect(Collectors.groupingBy(Stock::getSizeId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static final Map<Long, StockDto> DTOS = Stream.of(
            getDto(11L, 0L),
            getDto(12L, 0L),
            getDto(13L, 0L),
            getDto(22L, 0L),
            getDto(31L, 10L),
            getDto(32L, 10L),
            getDto(33L, 10L),
            getDto(41L, 0L),
            getDto(42L, 0L),
            getDto(43L, 0L),
            getDto(44L, 10L),
            getDto(51L, 10L),
            getDto(52L, 10L),
            getDto(53L, 10L),
            getDto(54L, 10L)
    ).collect(Collectors.groupingBy(StockDto::getSizeId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static Optional<Stock> findBySizeId(Long sizeId) {
        return Optional.ofNullable(STOCKS.get(sizeId));
    }

    private static StockDto getDto(Long sizeId, Long quantity) {
        StockDto dto = new StockDto();
        dto.setSizeId(sizeId);
        dto.setQuantity(quantity);
        return dto;
    }

}
