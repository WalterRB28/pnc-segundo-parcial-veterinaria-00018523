package com.uca.pncsegundoparcialveterinaria.repository;

import com.uca.pncsegundoparcialveterinaria.entities.Product;
import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByNameIgnoreCase(String name);
}