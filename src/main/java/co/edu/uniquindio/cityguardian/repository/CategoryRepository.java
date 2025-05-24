package co.edu.uniquindio.cityguardian.repository;

import co.edu.uniquindio.cityguardian.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
