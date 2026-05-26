package com.uca.pncsegundoparcialveterinaria.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Category category;
    private BigDecimal price;
    private Integer stock;
    private Boolean available;
    private Boolean requiresPrescription;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    private String supplier;
}

