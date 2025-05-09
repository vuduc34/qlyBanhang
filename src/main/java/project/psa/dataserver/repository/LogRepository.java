package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.LogHoatdong;
import project.psa.dataserver.entity.Nhanvien;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogHoatdong, Long> {
    @Query(value = "SELECT * FROM LOG_HOATDONG ORDER BY ID DESC", nativeQuery = true)
    List<LogHoatdong> findAllLog();

    @Query(value = "SELECT * FROM LOG_HOATDONG WHERE NGUOITHUCHIEN = :maNV  ORDER BY ID DESC", nativeQuery = true)
    List<LogHoatdong> findByMaNV(@Param("maNV") String maNV);
}
