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
    @SequenceGenerator(name = "expenses_id_seq", sequenceName = "expenses_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expenses_id_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "REASON_FOR_EXPENSE")
    private String reasonForExpense;

    @Column(name = "AMOUNT")
    private Integer amount;

}
