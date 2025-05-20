package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Khachhang;

import java.util.List;

@Repository
public interface KhachhangRepository extends JpaRepository<Khachhang, String> {
    @Query(value = "SELECT * FROM KHACHHANG WHERE MAKH = :id ", nativeQuery = true)
    Khachhang findKhachhangById(@Param("id") String id);

    @Query(value = "SELECT * FROM KHACHHANG WHERE  STATUS = 'active'", nativeQuery = true)
    List<Khachhang> findAllKhachhang();

    boolean existsById(String id);

    List<Khachhang> findKhachhangByStatus(String status);

    @Query(value = "SELECT COUNT(*) FROM KHACHHANG  ", nativeQuery = true)
    int countKhachHang();

    @Query(value = "SELECT COUNT(*) FROM KHACHHANG WHERE STATUS = 'active' ", nativeQuery = true)
    int countKhachHangActive();
}
