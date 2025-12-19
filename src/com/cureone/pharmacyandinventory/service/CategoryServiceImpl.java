package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.repository.CategoryRepository;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Result<Category> addCategory(Category category) {

        if (category == null || category.getName() == null || category.getName().isBlank()) {
            return new Result<>(false, "Category name required");
        }

        if (repo.existsByName(category.getName())) {
            return new Result<>(false, "Category already exists");
        }

        int id = repo.save(category);
        category.setId(id);

        return new Result<>(true, "Category added successfully", category);
    }

    @Override
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return repo.findById(id);
    }
    @Override
    public List<Category> searchByName(String keyword) {
        return repo.searchByName(keyword);
    }

    @Override
    public void deleteCategory(int id) {
        repo.deleteById(id);
    }

}
