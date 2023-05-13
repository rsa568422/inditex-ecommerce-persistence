package org.iditex.ecommerce.persistence.repositories.csv;

import org.iditex.ecommerce.model.entities.Size;
import org.iditex.ecommerce.model.repositories.SizeRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.iditex.ecommerce.persistence.repositories.csv.CsvRepository.Paths.SIZES;

public class SizeCsvRepository extends CsvRepository<Size> implements SizeRepository {

    public SizeCsvRepository() {
        super(SIZES.toString());
    }

    @Override
    public List<Size> findByProductId(Long productId) {
        return elements.stream().filter(size -> size.getProductId().equals(productId)).collect(Collectors.toList());
    }

    @Override
    protected Size parse(String line) {
        String[] columns = line.split(",");
        return new Size(Long.parseLong(columns[0].trim()), Long.parseLong(columns[1].trim()), Boolean.parseBoolean(columns[2].trim()), Boolean.parseBoolean(columns[3].trim()));
    }
}
