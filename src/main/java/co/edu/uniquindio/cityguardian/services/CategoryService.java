package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.mapping.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(String id) throws Exception;
}
