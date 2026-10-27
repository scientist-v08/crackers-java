package com.crackers.vinayakatraders.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inventory {
    @Id
    @SequenceGenerator(
            name = "inventories_id_seq",
            sequenceName = "inventories_id_seq",
            allocationSize = 50
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "inventories_id_seq"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "brand_or_company", columnDefinition = "text")
    private String brandOrCompany;

    @Column(name = "state", nullable = false)
    private Short state;

    @Column(name = "item", columnDefinition = "text")
    private String item;

    @Min(1)
    @Column(name = "num_of_boxes", nullable = false)
    private Integer numOfBoxes;

    @Min(1)
    @Column(name = "num_of_cartons", nullable = false)
    private Integer numOfCartons;

    @Min(1)
    @Column(name = "price_per_carton", nullable = false)
    private Integer pricePerCarton;

    @Column(name = "sub_total", nullable = false)
    private Integer subTotal;

    public Inventory(String brandOrCompany, Short state, String item, Integer numOfBoxes,
                     Integer numOfCartons, Integer pricePerCarton, Integer subTotal) {
        this.brandOrCompany = brandOrCompany;
        this.state = state;
        this.item = item;
        this.numOfBoxes = numOfBoxes;
        this.numOfCartons = numOfCartons;
        this.pricePerCarton = pricePerCarton;
        this.subTotal = subTotal;
    }
}