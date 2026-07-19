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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;

    private Integer price;

}
