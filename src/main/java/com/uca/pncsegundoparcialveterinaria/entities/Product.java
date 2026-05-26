package com.uca.pncsegundoparcialveterinaria.entities;

import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "requiresPrescription", nullable = false)
    private Boolean requiresPrescription;

    @Column(name = "expirationDate", nullable = false)
    private Date expirationDate;

    @Column(name = "supplier", nullable = false)
    private String supplier;
}
