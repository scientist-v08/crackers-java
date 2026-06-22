package com.crackers.vinayakatraders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routes {
    @Id
    @SequenceGenerator(name = "routes_id_seq", sequenceName = "routes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "routes_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "route", nullable = false)
    private String route;

    @Column(name = "heading", nullable = false)
    private String heading;

    @Column(name = "role", nullable = false)
    private Long role;
}
