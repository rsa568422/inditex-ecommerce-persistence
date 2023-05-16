package org.inditex.ecommerce.persistence.csv.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.inditex.ecommerce.persistence.csv.Files;
import org.inditex.ecommerce.persistence.csv.annotations.Column;
import org.inditex.ecommerce.persistence.csv.annotations.ForeignKey;
import org.inditex.ecommerce.persistence.csv.annotations.Source;

@Data
@NoArgsConstructor
@Source(fileName = Files.STOCKS)
public class StockDto implements Dto {

    @Column(index = 0)
    @ForeignKey(origin = SizeDto.class)
    private Long sizeId;

    @Column(index = 1)
    private Long quantity;

}
