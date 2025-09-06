package security.instagram.entity;


import lombok.*;
import security.instagram.entity.enums.AiSummaryStatus;
import security.instagram.entity.enums.FileType;
import security.instagram.entity.enums.Visibility;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_doc_created_at", columnList = "createdAt DESC"),
        @Index(name = "idx_doc_visibility", columnList = "visibility"),
        @Index(name = "idx_doc_owner", columnList = "owner_id"),
        @Index(name = "idx_doc_group", columnList = "group_id"),
        @Index(name = "idx_doc_deleted", columnList = "isDeleted")
})
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 512)
    private String fileUrl; // optional if using signed url on demand

    @Column(nullable = false, length = 256)
    private String fileKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FileType fileType;

    @Column(nullable = false)
    private long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Visibility visibility;

    @ManyToOne @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(optional = false) @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private Double avgRating;

    private Integer ratingCount;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private AiSummaryStatus aiSummaryStatus;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToMany
    @JoinTable(name = "document_tag",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.avgRating == null) this.avgRating = 0.0;
        if (this.ratingCount == null) this.ratingCount = 0;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }
}