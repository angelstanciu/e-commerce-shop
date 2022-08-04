package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.CategoryNotFound;
import com.endava.mentorship2022.model.Category;
import com.endava.mentorship2022.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void canFindAllCategories() {
        // given
        List<Category> categories = List.of(
                new Category(1L,
                        "Category 1",
                        "category-1",
                        null),
                new Category(2L,
                        "Category 2",
                        "category-2",
                        null)
        );
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List actualCategories = categoryService.findAll();

        // then
        verify(categoryRepository).findAll();
        assertThat(actualCategories).isEqualTo(categories);
    }

    @Test
    void canFindCategoryById() {
        // given
        Category category = new Category(1L,
                "Category 1",
                "category-1",
                null);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        Category actualCategory = categoryService.findById(anyLong());

        // then
        verify(categoryRepository).findById(anyLong());
        assertThat(actualCategory).isEqualTo(category);
    }

    @Test
    void willThrowCannotFindCategoryById() {
        // given
        given(categoryRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> categoryService.findById(anyLong()))
                .isInstanceOf(CategoryNotFound.class)
                .hasMessageContaining("Category: " + 0 + " not found.");
    }

    @Test
    void canFindAllCategoriesByParent() {
        // given
        Category category1 = new Category(1L,
                "Category1",
                "category1",
                null);
        Category category2 = new Category(2L,
                "Category2",
                "category2",
                category1);
        Category category3 = new Category(3L,
                "Sub-Category2",
                "sub-category2",
                category2);
        given(categoryRepository.findAllByParent(new Category(2L))).willReturn(List.of(category3));

        // when
        List actualCategories = categoryService.findAllByParent(2L);

        // then
        assertThat(actualCategories).isEqualTo(List.of(category3));
    }

    @Test
    void canSaveCategory() {
        // given
        Category category1 = new Category(1L,
                "Category1",
                "category1",
                null);

        given(categoryRepository.save(category1)).willReturn(category1);

        // when
        Category actualCategory = categoryService.save(category1);

        // then
        verify(categoryRepository).save(any());
        assertThat(actualCategory).isEqualTo(category1);
    }

    @Test
    void canUpdateCategory() {
        // given
        Category category = new Category(1L,
                "Category1",
                "category1",
                null);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(categoryRepository.save(category)).willReturn(category);

        // when
        categoryService.update(1L, category);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    void willThrowCategoryNotFoundForUpdate() {
        // given
        Category category = new Category(1L,
                "Category1",
                "category1",
                null);
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> categoryService.update(1L, category))
                .isInstanceOf(CategoryNotFound.class)
                .hasMessageContaining("Category: " + 1L + " not found.");

        // then
        verify(categoryRepository, never()).save(category);
    }

    @Test
    void canDeleteCategoryById() {
        // given
        Category category = new Category(1L,
                "Category1",
                "category1",
                null);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        // when
        categoryService.deleteById(1L);

        // then
        verify(categoryRepository).delete(category);
    }

    @Test
    void willThrowCategoryNotFoundForDelete() {
        // given
        Category category = new Category(1L,
                "Category1",
                "category1",
                null);
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> categoryService.deleteById(1L))
                .isInstanceOf(CategoryNotFound.class)
                .hasMessageContaining("Category: " + 1L + " not found.");

        // then
        verify(categoryRepository, never()).delete(category);
    }
}