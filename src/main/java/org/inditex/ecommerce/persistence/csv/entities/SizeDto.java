package org.inditex.ecommerce.persistence.csv.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inditex.ecommerce.persistence.csv.annotations.*;
import org.inditex.ecommerce.persistence.csv.repositories.StockCsvRepository;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Source(fileName = "size-1")
public class SizeDto {

    @PrimaryKey
    private Long id;

    @Column(index = 1)
    @ForeignKey(origin = ProductDto.class)
    private Long productId;

    @Column(index = 2)
    private boolean backSoon;

    @Column(index = 3)
    private boolean special;

    @OneToOne(source = StockCsvRepository.class, method = "findBySizeId")
    private Optional<StockDto> stock;

}
