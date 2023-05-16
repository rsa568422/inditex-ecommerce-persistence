package org.inditex.ecommerce.persistence.csv.data;

import org.iditex.ecommerce.model.entities.Size;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;
import org.inditex.ecommerce.persistence.csv.entities.StockDto;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SizeData {

    public static final Map<Long, Size> SIZES = Stream.of(
            new Size(11L, 1L, true, false, StockData.STOCKS.get(11L)),
            new Size(12L, 1L, false, false, StockData.STOCKS.get(12L)),
            new Size(13L, 1L , true, false, StockData.STOCKS.get(13L)),
            new Size(21L, 2L, false, false, StockData.STOCKS.get(21L)),
            new Size(22L, 2L, false, false, StockData.STOCKS.get(22L)),
            new Size(23L, 2L, true, true, StockData.STOCKS.get(23L)),
            new Size(31L, 3L, true, false, StockData.STOCKS.get(31L)),
            new Size(32L, 3L, true, false, StockData.STOCKS.get(32L)),
            new Size(33L, 3L, false, false, StockData.STOCKS.get(33L)),
            new Size(41L, 4L, false, false, StockData.STOCKS.get(41L)),
            new Size(42L, 4L, false, false, StockData.STOCKS.get(42L)),
            new Size(43L, 4L, false, false, StockData.STOCKS.get(43L)),
            new Size(44L, 4L, true, true, StockData.STOCKS.get(44L)),
            new Size(51L, 5L, true, false, StockData.STOCKS.get(51L)),
            new Size(52L, 5L, false, false, StockData.STOCKS.get(52L)),
            new Size(53L, 5L, false, false, StockData.STOCKS.get(53L)),
            new Size(54L, 5L, true, true, StockData.STOCKS.get(54L))
    ).collect(Collectors.groupingBy(Size::getId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static final Map<Long, SizeDto> DTOS = Stream.of(
            getDto(11L, 1L, true, false, StockData.DTOS.get(11L)),
            getDto(12L, 1L, false, false, StockData.DTOS.get(12L)),
            getDto(13L, 1L , true, false, StockData.DTOS.get(13L)),
            getDto(21L, 2L, false, false, StockData.DTOS.get(21L)),
            getDto(22L, 2L, false, false, StockData.DTOS.get(22L)),
            getDto(23L, 2L, true, true, StockData.DTOS.get(23L)),
            getDto(31L, 3L, true, false, StockData.DTOS.get(31L)),
            getDto(32L, 3L, true, false, StockData.DTOS.get(32L)),
            getDto(33L, 3L, false, false, StockData.DTOS.get(33L)),
            getDto(41L, 4L, false, false, StockData.DTOS.get(41L)),
            getDto(42L, 4L, false, false, StockData.DTOS.get(42L)),
            getDto(43L, 4L, false, false, StockData.DTOS.get(43L)),
            getDto(44L, 4L, true, true, StockData.DTOS.get(44L)),
            getDto(51L, 5L, true, false, StockData.DTOS.get(51L)),
            getDto(52L, 5L, false, false, StockData.DTOS.get(52L)),
            getDto(53L, 5L, false, false, StockData.DTOS.get(53L)),
            getDto(54L, 5L, true, true, StockData.DTOS.get(54L))
    ).collect(Collectors.groupingBy(SizeDto::getId, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));

    public static Set<Size> findByProductId(Long id) {
        return SIZES.values().stream().filter(size -> size.getProductId().equals(id)).collect(Collectors.toSet());
    }

    public static Set<SizeDto> findDtoByProductId(Long id) {
        return DTOS.values().stream().filter(size -> size.getProductId().equals(id)).collect(Collectors.toSet());
    }

    private static SizeDto getDto(Long id, Long productId, boolean backSoon, boolean special, StockDto stock) {
        SizeDto dto = new SizeDto();
        dto.setId(id);
        dto.setProductId(productId);
        dto.setBackSoon(backSoon);
        dto.setSpecial(special);
        dto.setStock(stock);
        return dto;
    }

}
