package org.iditex.ecommerce.persistence.repositories.csv;

import org.iditex.ecommerce.model.entities.Stock;
import org.iditex.ecommerce.model.repositories.StockRepository;

import java.util.Optional;

import static org.iditex.ecommerce.persistence.repositories.csv.CsvRepository.Paths.STOCKS;

public class StockCsvRepository extends CsvRepository<Stock> implements StockRepository {

    public StockCsvRepository() {
        super(STOCKS.toString());
    }

    @Override
    public Optional<Stock> findBySizeId(Long sizeId) {
        return elements.stream().filter(stock -> stock.getSizeId().equals(sizeId)).findFirst();
    }

    @Override
    protected Stock parse(String line) {
        String[] columns = line.split(",");
        return new Stock(Long.parseLong(columns[0].trim()), Long.parseLong(columns[1].trim()));
    }
}
