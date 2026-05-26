package com.uca.pncsegundoparcialveterinaria.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
public class ProductRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    private Boolean available;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @NotBlank(message = "El proveedor es obligatorio")
    private String supplier;
}