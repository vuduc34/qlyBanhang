package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRolename(String name);
}
