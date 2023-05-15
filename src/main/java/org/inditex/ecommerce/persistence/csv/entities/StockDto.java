package org.inditex.ecommerce.persistence.csv.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inditex.ecommerce.persistence.csv.annotations.Column;
import org.inditex.ecommerce.persistence.csv.annotations.ForeignKey;
import org.inditex.ecommerce.persistence.csv.annotations.Source;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Source(fileName = "stock")
public class StockDto {

    @Column(index = 0)
    @ForeignKey(origin = SizeDto.class)
    private Long sizeId;

    @Column(index = 1)
    private Long quantity;

}
