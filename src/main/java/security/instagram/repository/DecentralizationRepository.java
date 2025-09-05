package security.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import security.instagram.entity.Decentralization;
import security.instagram.entity.Role;

@Repository
public interface DecentralizationRepository extends JpaRepository<Decentralization, Long> {
    @Query("SELECT COUNT(d) > 0 FROM Decentralization d WHERE d.role = ?1 AND d.apiPattern = ?2 AND (LOCATE(?3, d.httpMethod) > 0 OR d.httpMethod = 'ALL')")
    boolean checkPemission(Role role, String apiPattern, String httpMethod);
}