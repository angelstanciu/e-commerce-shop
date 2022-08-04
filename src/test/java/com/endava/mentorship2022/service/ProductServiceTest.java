package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.ProductNotFound;
import com.endava.mentorship2022.model.Product;
import com.endava.mentorship2022.model.TechnicalDetail;
import com.endava.mentorship2022.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should find all products")
    void findAllProductsTest() {
        // given
        Product product1 = new Product(1L,
                "Tchibo cafea macinata",
                "Tchibo-cafea-macinata",
                "O cafea macinata foarte buna",
                "Tchibo",
                20,
                15,
                true,
                null,
                null
        );

        Product product2 = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                null
        );

        List<Product> productList = List.of(product1, product2);
        when(productRepository.findAll()).thenReturn(productList);

        // when
        List<Product> actualProducts = productService.findAllProducts();

        // then
        assertThat(actualProducts).hasSize(2);
        assertThat(productList).isEqualTo(actualProducts);
    }

    @Test
    @DisplayName("Should find a product by ID")
    void findProductByIdTest() throws ProductNotFound {
        // given
        Product productToBeFound = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                null
        );

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productToBeFound));

        // when
        Product actualProduct = productService.findById(anyLong());

        // then
        assertThat(actualProduct).isEqualTo(productToBeFound);
    }

    @Test
    @DisplayName("Should throw ProductNotFound Exception")
    void findProductById_ExceptionTest() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFound.class, () -> productService.findById(anyLong()));
    }

    @Test
    @DisplayName("Should update a product")
    void updateProductTest() {
        // given
        Product productToBeUpdated = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                null
        );

        Product newProduct = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                null
        );

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productToBeUpdated));
        when(productRepository.save(productToBeUpdated)).thenReturn(newProduct);

        // when
        Product resultedProduct = productService.updateProduct(anyLong(), newProduct);

        //then
        assertThat(resultedProduct).isEqualTo(newProduct);
    }

    @Test
    @DisplayName("Should throw ProductNotFound Exception")
    void updateProduct_ExceptionTest() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFound.class, () -> productService.deleteById(anyLong()));
    }

    @Test
    @DisplayName("Should save a product")
    void saveProductTest() {
        // given
        Product product = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                Set.of(
                        new TechnicalDetail(1L, "Brand", "Lavazza", null)
                )
        );

        when(productRepository.save(product)).thenReturn(product);

        // when
        Product newProduct = productService.saveProduct(product);

        // then
        assertThat(newProduct).isEqualTo(product);
    }

    @Test
    void shouldSaveLotsOfProducts() throws IOException {
        // given
        List<Product> productsToBeSaved = createListOfProducts();

        for (Product product : productsToBeSaved) {
            when(productRepository.save(product)).thenReturn(product);
        }

        // when
        List<Product> productsSaved = new ArrayList<>();
        for (Product product : productsToBeSaved) {
            Product productSaved = productService.saveProduct(product);
            productsSaved.add(productSaved);
        }

        // then
        assertThat(productsSaved).hasSize(100);

        for (int i = 0; i < productsSaved.size(); i++) {
            assertThat(productsSaved.get(i)).isEqualTo(productsToBeSaved.get(i));
        }

    }

    // method for creating a list of products using the words from a text file
    private List<Product> createListOfProducts() throws IOException {

        final int MAX_PRODUCTS = 100;                                   // how many products you want to save

        List<Product> productList = new ArrayList<>();

        List<String> wordList = Files
                .lines(Paths.get("src/main/greatexpectations.txt"))  // get the lines from a text file as a stream
                .map(line -> line.split("[^A-Za-z]"))              // create a stream of String[] only with letters
                .map(Arrays::asList)                                     // create a stream of List<String> - easy to work with
                .flatMap(list -> list.stream())                          // create a stream of words
                .filter(word -> word.length() > 3 && word.length() < 20) // keep the words with length 3-20
                .limit(MAX_PRODUCTS * 5)                          // limit the list size
                .collect(Collectors.toList());                            // save the stream into a list

        // loop through the lists, create and add a new Product to the list on each iteration
        for (int i = 0; i < wordList.size() - 4; i += 5) {
            Product product = new Product();
            //selects 3 words and sets the name with blanks between words
            product.setName(wordList.get(i) + " " + wordList.get(i + 1) + " " + wordList.get(i + 2));
            //selects 3 words and sets the alias with hyphens between words
            product.setAlias(wordList.get(i) + "-" + wordList.get(i + 1) + "-" + wordList.get(i + 2));
            product.setDescription(wordList.get(i + 3));
            product.setBrand(wordList.get(i + 4));
            product.setPrice((float) (Math.random() * 1000) + 1);
            product.setStock((int) (Math.random() * 100) + 1);
            product.setEnabled(true);
            product.setCategory(null);
            productList.add(product);
        }

        return productList;
    }

    @Test
    void shouldDeleteById() {
        // given
        Product product = new Product(2L,
                "Lavazza cafea boabe",
                "Lavazza-cafea-boabe",
                "O cafea boabe foarte buna",
                "Lavazza",
                15,
                10,
                false,
                null,
                Set.of(
                        new TechnicalDetail(1L, "Brand", "Lavazza", null)
                )
        );
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // when
        productService.deleteById(2L);

        // then
        verify(productRepository).deleteById(2L);
    }

    @Test
    void shouldFindAllByPage() {
        // given
        List<Product> products = List.of(
                new Product(1L,
                        "Lavazza cafea boabe",
                        "Lavazza-cafea-boabe",
                        "O cafea boabe foarte buna",
                        "Lavazza",
                        15,
                        10,
                        false,
                        null,
                        null),
                new Product(2L,
                        "Lavazza cafea boabe",
                        "Lavazza-cafea-boabe",
                        "O cafea boabe foarte buna",
                        "Lavazza",
                        15,
                        10,
                        false,
                        null,
                        null)
        );
        Page<Product> foundPage = new PageImpl<>(products);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(foundPage);

        // when
        List<Product> actual = productService.findAllByPage(1, 5, "id", "asc");

        // then
        assertThat(actual).isEqualTo(products);
    }

}