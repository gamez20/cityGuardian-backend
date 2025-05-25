package co.edu.uniquindio.cityguardian.controller;

import co.edu.uniquindio.cityguardian.mapping.dto.CategoryDTO;
import co.edu.uniquindio.cityguardian.mapping.dto.MessageDTO;
import co.edu.uniquindio.cityguardian.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<MessageDTO<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(new MessageDTO<>(false, categoryService.createCategory(categoryDTO)));
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageDTO<>(true, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO<CategoryDTO>> getCategoryById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(new MessageDTO<>(false, categoryService.getCategoryById(id)));
    }
}
