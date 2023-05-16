package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Stock;
import org.iditex.ecommerce.model.repositories.StockRepository;
import org.inditex.ecommerce.persistence.csv.entities.StockDto;

import java.util.Optional;

public class StockCsvRepository extends CsvRepository<StockDto, Stock> implements StockRepository {

    @Override
    public Optional<Stock> findBySizeId(Long sizeId) {
        return elements.stream().filter(stock -> stock.getSizeId().equals(sizeId)).findFirst();
    }

    @Override
    protected Class<StockDto> getDtoClass() {
        return StockDto.class;
    }

}
