package com.crackers.vinayakatraders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EXPENSES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REASON_FOR_EXPENSE")
    private String reasonForExpense;

    @Column(name = "AMOUNT")
    private Integer amount;

}
