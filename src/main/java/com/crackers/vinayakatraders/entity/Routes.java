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
    @SequenceGenerator(name = "routes_id_seq", sequenceName = "routes_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "routes_id_seq")
    @Column(name = "id", updatable = false)
    private Short id;

    @Column(name = "route", nullable = false)
    private String route;

    @Column(name = "heading", nullable = false)
    private String heading;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
