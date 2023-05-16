package org.inditex.ecommerce.persistence.csv.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inditex.ecommerce.persistence.csv.Files;
import org.inditex.ecommerce.persistence.csv.annotations.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Source(fileName = Files.SIZES)
public class SizeDto implements Dto {

    @PrimaryKey
    private Long id;

    @Column(index = 1)
    @ForeignKey(origin = ProductDto.class)
    private Long productId;

    @Column(index = 2)
    private boolean backSoon;

    @Column(index = 3)
    private boolean special;

    @OneToOne(source = StockDto.class)
    private StockDto stock;

}
