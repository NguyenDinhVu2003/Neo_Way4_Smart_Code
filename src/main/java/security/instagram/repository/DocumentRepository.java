package security.instagram.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import security.instagram.entity.Document;
import security.instagram.entity.User;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    List<Document> findByIsDeletedFalseOrderByAvgRatingDescRatingCountDesc(Pageable pageable);
    List<Document> findByOwnerAndIsDeletedFalseOrderByCreatedAtDesc(User owner, Pageable pageable);
}