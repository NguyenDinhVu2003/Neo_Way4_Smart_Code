package security.instagram.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import security.instagram.entity.Document;
import security.instagram.entity.Group;
import security.instagram.entity.User;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    List<Document> findByIsDeletedFalseOrderByAvgRatingDescRatingCountDesc(Pageable pageable);
    List<Document> findByOwnerAndIsDeletedFalseOrderByCreatedAtDesc(User owner, Pageable pageable);
}