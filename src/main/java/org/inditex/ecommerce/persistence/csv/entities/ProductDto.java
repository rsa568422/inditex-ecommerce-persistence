package org.inditex.ecommerce.persistence.csv.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iditex.ecommerce.model.entities.Size;
import org.inditex.ecommerce.persistence.csv.Files;
import org.inditex.ecommerce.persistence.csv.annotations.Column;
import org.inditex.ecommerce.persistence.csv.annotations.OneToMany;
import org.inditex.ecommerce.persistence.csv.annotations.PrimaryKey;
import org.inditex.ecommerce.persistence.csv.annotations.Source;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Source(fileName = Files.PRODUCTS)
public class ProductDto {

    @PrimaryKey
    private Long id;

    @Column(index = 1)
    private Long sequence;

    @OneToMany(source = Files.SIZES)
    private Set<Size> sizes;

}
