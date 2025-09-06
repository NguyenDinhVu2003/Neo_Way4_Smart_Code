package security.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.instagram.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}