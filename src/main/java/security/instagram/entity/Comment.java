package security.instagram.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "comments", indexes = {
        @Index(name = "idx_comment_doc", columnList = "document_id")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false) @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(optional = false) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }
}