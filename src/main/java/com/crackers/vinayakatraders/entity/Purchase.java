package com.crackers.vinayakatraders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    @Id
    @SequenceGenerator(
            name = "purchases_id_seq",
            sequenceName = "purchases_id_seq",
            allocationSize = 50
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "purchases_id_seq"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "billing_id", nullable = false)
    private Billing billing;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "mrp_or_net")
    private Integer mrpOrNet;

    @Column(name = "item")
    private String item;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private String discount;

    @Column(name = "sub_total", precision = 12, scale = 2)
    private BigDecimal subTotal;
}
