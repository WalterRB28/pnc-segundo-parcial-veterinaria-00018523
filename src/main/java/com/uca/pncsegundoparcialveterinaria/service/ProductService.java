package com.uca.pncsegundoparcialveterinaria.service;

import com.uca.pncsegundoparcialveterinaria.dto.request.ProductRequest;
import com.uca.pncsegundoparcialveterinaria.dto.response.ProductResponse;
import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> findAll(Category category, Boolean available);
    ProductResponse findById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}

