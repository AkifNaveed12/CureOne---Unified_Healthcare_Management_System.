package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Category;

import java.util.List;

public interface CategoryService {

    Result<Category> addCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(int id);
    List<Category> searchByName(String keyword);

    void deleteCategory(int id);

}
