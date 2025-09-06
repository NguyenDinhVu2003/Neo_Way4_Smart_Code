package security.instagram.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import security.instagram.entity.Group;
import security.instagram.entity.GroupMember;
import security.instagram.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    boolean existsByGroupAndUser(Group group, User user);
    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    boolean existsByGroupAndUser(Long id, Long id1);
}