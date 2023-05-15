package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Size;
import org.iditex.ecommerce.model.repositories.SizeRepository;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;

import java.util.Set;
import java.util.stream.Collectors;

public class SizeCsvRepository extends CsvRepository<SizeDto, Size> implements SizeRepository {

    private static final StockCsvRepository STOCK_CSV_REPOSITORY = new StockCsvRepository();

    @Override
    public Set<Size> findByProductId(Long productId) {
        return elements.stream().filter(size -> size.getProductId().equals(productId)).collect(Collectors.toSet());
    }

    @Override
    protected Size parse(String line) {
        String[] columns = line.split(",");
        Long id = Long.valueOf(columns[0].trim());
        return new Size(
                id,
                Long.parseLong(columns[1].trim()),
                Boolean.parseBoolean(columns[2].trim()),
                Boolean.parseBoolean(columns[3].trim()),
                STOCK_CSV_REPOSITORY.findBySizeId(id)
        );
    }

    @Override
    protected Class<SizeDto> getDtoClass() {
        return SizeDto.class;
    }

    @Override
    protected Class<Size> getEntityClass() {
        return Size.class;
    }

}
