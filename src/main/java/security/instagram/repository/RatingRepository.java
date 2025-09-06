package security.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.instagram.entity.Document;
import security.instagram.entity.Rating;
import security.instagram.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByDocumentAndUser(Document document, User user);
    long countByDocument(Document document);
}