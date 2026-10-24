package com.crackers.vinayakatraders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PRICE_LIST")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceList {

    @Id
    @SequenceGenerator(name = "price_list_id_seq", sequenceName = "price_list_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_list_id_seq")
    private Long id;

    private String item;

    private Integer price;

    private String brand;

}
