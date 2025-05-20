package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Hoadon;
import project.psa.dataserver.entity.Khachhang;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HoadonRepository extends JpaRepository<Hoadon, Integer> {
    @Query(value = "SELECT * FROM HOADON ORDER BY NGHD DESC", nativeQuery = true)
    List<Hoadon> findAllHoaDon();

    @Query(value = "SELECT * FROM HOADON WHERE MAKH = :MAKH ORDER BY NGHD DESC", nativeQuery = true)
    List<Hoadon> findHoaDonByMaKH(@Param("MAKH") String MAKH);

    Hoadon findHoadonById(Integer id);

    @Query(value = "SELECT COUNT(*) FROM HOADON ", nativeQuery = true)
    int countHoaDon();

    @Query(value = "SELECT SUM(TRIGIA) FROM HOADON WHERE CAST(NGHD AS DATE) = CAST(GETDATE() AS DATE)", nativeQuery = true)
    BigDecimal doanhThuHomNay();

    @Query(value = "SELECT SUM(TRIGIA) FROM HOADON WHERE MONTH(NGHD) = MONTH(GETDATE()) AND YEAR(NGHD) = YEAR(GETDATE())", nativeQuery = true)
    BigDecimal doanhThuThangNay();

    @Query(value = "SELECT SUM(TRIGIA) FROM HOADON WHERE DATEPART(QUARTER, NGHD) = DATEPART(QUARTER, GETDATE()) AND YEAR(NGHD) = YEAR(GETDATE())", nativeQuery = true)
    BigDecimal doanhThuQuyNay();
}
