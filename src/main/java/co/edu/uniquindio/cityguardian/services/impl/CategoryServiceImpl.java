package co.edu.uniquindio.cityguardian.services.impl;

import co.edu.uniquindio.cityguardian.mapping.dto.CategoryDTO;
import co.edu.uniquindio.cityguardian.model.Category;
import co.edu.uniquindio.cityguardian.repository.CategoryRepository;
import co.edu.uniquindio.cityguardian.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private  CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory.getId(), savedCategory.getName(), savedCategory.getDescription());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName(), c.getDescription()))
                .toList();
    }

    @Override
    public CategoryDTO getCategoryById(String id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Categoría no encontrada"));
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }
}
