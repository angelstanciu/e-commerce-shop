package com.endava.mentorship2022.controller;


import com.endava.mentorship2022.model.Product;
import com.endava.mentorship2022.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findAll(){
        return productService.findAllProducts();
    }
    @GetMapping("/page/{pageNum}")
    public List<Product> findAllByPage(@PathVariable int pageNum,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "id") String sortField,
                                    @RequestParam(defaultValue = "asc") String sortDir ) {
        return productService.findAllByPage(pageNum, pageSize, sortField, sortDir);
    }
    @GetMapping("{id}")
    public Product findProductById(@PathVariable long id){
        return productService.findById(id);
    }
    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable long id){
        productService.deleteById(id);
    }

    @PutMapping("{id}")
    public Product updateProduct(@PathVariable long id, @RequestBody Product newProduct){
        return productService.updateProduct(id, newProduct);
    }

}
