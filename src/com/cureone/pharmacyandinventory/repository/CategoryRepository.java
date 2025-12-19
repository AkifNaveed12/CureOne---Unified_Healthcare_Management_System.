package com.cureone.pharmacyandinventory.repository;

import com.cureone.pharmacyandinventory.model.Category;
import java.util.List;

public interface CategoryRepository {

    int save(Category category);

    List<Category> findAll();

    Category findById(int id);

    boolean existsByName(String name);
    List<Category> searchByName(String keyword);

    void deleteById(int id);


}
