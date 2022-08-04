package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.ProductNotFound;
import com.endava.mentorship2022.model.Product;
import com.endava.mentorship2022.model.TechnicalDetail;
import com.endava.mentorship2022.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFound("Product: " + id + " not found!"));
    }

    public Product saveProduct(Product product) {
        // Make a copy of technicalDetails
        Set<TechnicalDetail> technicalDetails = new HashSet<>(product.getTechnicalDetails());
        product.setTechnicalDetails(null);

        // Save without technicalDetails first, in order to get a productId to use in technicalDetails
        productRepository.save(product);

        // Add technicalDetails and then save product
        technicalDetails.forEach(technicalDetail -> technicalDetail.setProduct(product));
        product.setTechnicalDetails(technicalDetails);
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        findById(id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(long id, Product newProduct) {
        Product productToUpdate = findById(id);
        productToUpdate.setName(newProduct.getName());
        productToUpdate.setAlias(newProduct.getAlias());
        productToUpdate.setBrand(newProduct.getBrand());
        productToUpdate.setDescription(newProduct.getDescription());
        productToUpdate.setCategory(newProduct.getCategory());
        productToUpdate.setEnabled(newProduct.isEnabled());
        productToUpdate.setPrice(newProduct.getPrice());
        productToUpdate.setStock(newProduct.getStock());
        productToUpdate.setTechnicalDetails(newProduct.getTechnicalDetails());
        return productRepository.save(productToUpdate);
    }

    public List<Product> findAllByPage(int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        if (pageNum < 1) pageNum = 1;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<Product> pageProducts = productRepository.findAll(pageable);

        return pageProducts.getContent();
    }
}
