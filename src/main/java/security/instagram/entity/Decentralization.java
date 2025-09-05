package security.instagram.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "decentralization",
       uniqueConstraints = @UniqueConstraint(columnNames = {"role","api_pattern","http_method"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Decentralization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;                    // USER / ADMIN

    @Column(name = "api_pattern", nullable = false, length = 255)
    private String apiPattern;            // e.g. /v1/users/**

    @Column(name = "http_method", nullable = false, length = 50)
    private String httpMethod;         // e.g. GET;POST;PUT;DELETE
}