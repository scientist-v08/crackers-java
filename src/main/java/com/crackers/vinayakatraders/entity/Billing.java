package com.crackers.vinayakatraders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "BILLINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {
    @Id
    @SequenceGenerator(
            name = "billings_id_seq",
            sequenceName = "billings_id_seq",
            allocationSize = 50
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "billings_id_seq"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "\"user\"")
    private String user;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "grand_total", precision = 12, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "finalized_amt")
    private Integer finalizedAmt;
}
