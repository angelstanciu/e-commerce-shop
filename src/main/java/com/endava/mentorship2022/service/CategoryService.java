package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.CategoryNotFound;
import com.endava.mentorship2022.model.Category;
import com.endava.mentorship2022.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFound("Category: " + id + " not found."));
    }

    public List<Category> findAllByParent(Long id) {
        Category categoryParent = null;
        if (id != 0) categoryParent = new Category(id);
        return categoryRepository.findAllByParent(categoryParent);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        Category updatedCategory = findById(id);

        updatedCategory.setName(category.getName());
        updatedCategory.setAlias(category.getAlias());
        updatedCategory.setParent(category.getParent());

        return categoryRepository.save(updatedCategory);
    }

    public void deleteById(long id){
        Category category = findById(id);
        categoryRepository.delete(category);
    }
}
