package security.instagram.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author ket_ein17
 * @Date 5/27/2024
 */
@Table(name = "user")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String password;
    private Long avatarId;
    private String avatarPath;
    private Role role;
    private boolean enabled;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    @CreationTimestamp
    private Date createAt;
    @UpdateTimestamp
    private Date modifyAt;
    public String getUsername(){
        return username;
    }
    public String getName(){
        return firstName + " " + lastName;
    }
}
