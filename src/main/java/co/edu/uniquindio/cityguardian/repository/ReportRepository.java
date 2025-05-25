package co.edu.uniquindio.cityguardian.repository;

import co.edu.uniquindio.cityguardian.model.Report;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    @NotNull Optional<Report> findById(@NotNull String id);

    List<Report> findByCategoryId(String categoryId);
}
