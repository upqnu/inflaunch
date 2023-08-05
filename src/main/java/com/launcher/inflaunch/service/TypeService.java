package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.repository.CategoryRepository;
import com.launcher.inflaunch.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TypeService {
    private TypeRepository typeRepository;
    private CategoryRepository categoryRepository;


    @Autowired
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> categoryList() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public int delCategory(Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null){
            categoryRepository.delete(category);
            return 1;
        }
        return 0;
    }

    public int addCategory(Category category) {
        for (Category i : categoryList()) {
            if (category.equals(i.getCategory())) return 0;
        }
        category = categoryRepository.saveAndFlush(category);
        return 1;
    }


    public List<Type> typeList() {
        List<Type> types = typeRepository.findAll();
        return types;
    }


    public int delType(Long id){
        Type type = typeRepository.findById(id).orElse(null);
        if(type != null){
            typeRepository.delete(type);
            return 1;
        }
        return 0;
    }

    public int addType(Type type, Long categoryId) {
        for (Type i : typeList()) {
            if (type.equals(i.getType())) return 0;
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);

        type.setCategory(category);
        type = typeRepository.saveAndFlush(type);
        return 1;
    }
}
