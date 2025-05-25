package co.edu.uniquindio.cityguardian.repository;

import co.edu.uniquindio.cityguardian.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    @NotNull Optional<User> findById(@NotNull String id);

    void deleteByEmail(String email);
}
