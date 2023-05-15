package org.inditex.ecommerce.persistence.csv.repositories;

import org.iditex.ecommerce.model.entities.Size;
import org.iditex.ecommerce.model.repositories.SizeRepository;
import org.inditex.ecommerce.persistence.csv.entities.SizeDto;

import java.util.Set;
import java.util.stream.Collectors;

public class SizeCsvRepository extends CsvRepository<SizeDto, Size> implements SizeRepository {

    @Override
    public Set<Size> findByProductId(Long productId) {
        return elements.stream().filter(size -> size.getProductId().equals(productId)).collect(Collectors.toSet());
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
