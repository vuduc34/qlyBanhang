package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Nhanvien;

import java.util.List;

@Repository
public interface NhanvienRepository extends JpaRepository<Nhanvien, String> {
    @Query(value = "SELECT * FROM NHANVIEN WHERE MANV = :id ", nativeQuery = true)
    Nhanvien findNhanvienById(@Param("id") String id);
    boolean existsById(String id);
    @Query(value = "SELECT * FROM NHANVIEN WHERE STATUS = 'active'", nativeQuery = true)
    List<Nhanvien> findAllNhanvien();

    List<Nhanvien> findNhanvienByStatus(String status);

    @Query(value = "SELECT count(*) FROM NHANVIEN ", nativeQuery = true)
    int countNhanvien();

    @Query(value = "SELECT count(*) FROM NHANVIEN WHERE STATUS = 'active' ", nativeQuery = true)
    int countNhanvienActive();
}
