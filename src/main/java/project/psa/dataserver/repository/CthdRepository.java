package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Cthd;
import project.psa.dataserver.entity.Khachhang;
@Repository
public interface CthdRepository extends JpaRepository<Cthd, Integer> {
}
