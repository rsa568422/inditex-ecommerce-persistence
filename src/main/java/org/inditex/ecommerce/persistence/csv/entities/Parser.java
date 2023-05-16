package org.inditex.ecommerce.persistence.csv.entities;

import org.iditex.ecommerce.model.entities.Product;
import org.iditex.ecommerce.model.entities.Size;
import org.iditex.ecommerce.model.entities.Stock;

import java.util.stream.Collectors;

public class Parser {

    private Parser() {

    }

    public static <D extends Dto> Object parse(D dto) {
        if (dto instanceof ProductDto) return parseToProduct((ProductDto) dto);
        if (dto instanceof SizeDto) return parseToSize((SizeDto) dto);
        if (dto instanceof StockDto) return parseToStock((StockDto) dto);
        return null;
    }

    public static Product parseToProduct(ProductDto dto) {
        return new Product(dto.getId(), dto.getSequence(), dto.getSizes().stream().map(Parser::parseToSize).collect(Collectors.toSet()));
    }

    public static Size parseToSize(SizeDto dto) {
        return new Size(dto.getId(), dto.getProductId(), dto.isBackSoon(), dto.isSpecial(), parseToStock(dto.getStock()));
    }

    public static Stock parseToStock(StockDto dto) {
        if (dto == null) return null;
        return new Stock(dto.getSizeId(), dto.getQuantity());
    }

}
