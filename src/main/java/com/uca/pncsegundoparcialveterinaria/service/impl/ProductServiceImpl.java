package com.uca.pncsegundoparcialveterinaria.service.impl;

import com.uca.pncsegundoparcialveterinaria.dto.request.ProductRequest;
import com.uca.pncsegundoparcialveterinaria.dto.response.ProductResponse;
import com.uca.pncsegundoparcialveterinaria.entities.ENUM.Category;
import com.uca.pncsegundoparcialveterinaria.entities.Product;
import com.uca.pncsegundoparcialveterinaria.exception.BusinessRuleException;
import com.uca.pncsegundoparcialveterinaria.exception.ResourceNotFoundException;
import com.uca.pncsegundoparcialveterinaria.repository.ProductRepository;
import com.uca.pncsegundoparcialveterinaria.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        validateExpirationDate(request.getExpirationDate());
        validateUniqueName(request.getName(), null);

        Product product = new Product();
        applyRequest(product, request, null);
        product.setId(null);
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Category category, Boolean available) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (category != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category"), category));
        }

        if (available != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("available"), available));
        }

        return productRepository.findAll(specification).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = getProductOrThrow(id);
        return toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        validateExpirationDate(request.getExpirationDate());
        Product product = getProductOrThrow(id);
        validateUniqueName(request.getName(), id);
        applyRequest(product, request, product.getAvailable());
        return toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = getProductOrThrow(id);
        if (product.getCategory() == Category.VACCINE && Boolean.TRUE.equals(product.getAvailable())) {
            throw new BusinessRuleException("No se puede eliminar un producto VACCINE disponible");
        }
        productRepository.delete(product);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private void validateUniqueName(String name, Long currentId) {
        String normalizedName = normalize(name);
        Optional<Product> existing = productRepository.findByNameIgnoreCase(normalizedName);
        if (existing.isPresent() && (currentId == null || !existing.get().getId().equals(currentId))) {
            throw new BusinessRuleException("Ya existe un producto con el nombre: " + name);
        }
    }

    private void validateExpirationDate(LocalDate expirationDate) {
        if (expirationDate == null) {
            throw new BusinessRuleException("La fecha de vencimiento es obligatoria");
        }
        if (!expirationDate.isAfter(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de vencimiento debe ser futura");
        }
    }

    private void applyRequest(Product product, ProductRequest request, Boolean previousAvailable) {
        product.setName(normalize(request.getName()));
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setRequiresPrescription(request.getCategory() == Category.MEDICINE || request.getCategory() == Category.VACCINE);
        product.setExpirationDate(toDate(request.getExpirationDate()));
        product.setSupplier(normalize(request.getSupplier()));

        if (request.getStock() == null || request.getStock() == 0) {
            product.setAvailable(false);
        } else {
            product.setAvailable(request.getAvailable() != null ? request.getAvailable() : Objects.requireNonNullElse(previousAvailable, true));
        }
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setAvailable(product.getAvailable());
        response.setRequiresPrescription(product.getRequiresPrescription());
        response.setExpirationDate(toLocalDate(product.getExpirationDate()));
        response.setSupplier(product.getSupplier());
        return response;
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

