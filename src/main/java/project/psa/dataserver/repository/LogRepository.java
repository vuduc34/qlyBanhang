package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.LogHoatdong;
import project.psa.dataserver.entity.Nhanvien;
@Repository
public interface LogRepository extends JpaRepository<LogHoatdong, Long> {
}
