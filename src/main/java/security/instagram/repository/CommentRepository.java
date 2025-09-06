package security.instagram.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import security.instagram.entity.Comment;
import security.instagram.entity.Document;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByDocumentAndIsDeletedFalseOrderByCreatedAtAsc(Document document, Pageable pageable);
}