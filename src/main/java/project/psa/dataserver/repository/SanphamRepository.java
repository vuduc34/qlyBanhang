package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Khachhang;
import project.psa.dataserver.entity.Sanpham;

import java.util.List;

@Repository
public interface SanphamRepository extends JpaRepository<Sanpham, String> {
    Sanpham findSanphamById(String id);

    @Query(value = "SELECT COUNT(*) FROM SANPHAM ", nativeQuery = true)
    int countSanpham();

    @Query(value = "SELECT COUNT(*) FROM SANPHAM WHERE STATUS = 'available'", nativeQuery = true)
    int countSanphamAvailable();

    @Query(value = "SELECT TOP 5 SP.MASP, SP.TENSP, SP.DVT, SP.NUOCSX," +
            " SP.GIA, SP.MAKM, SP.STATUS, SP.IMAGEURL," +
            " ISNULL(SUM(CT.SL), 0) AS tongSoLuongBan" +
            " FROM SANPHAM SP LEFT JOIN " +
            " CTHD CT ON SP.MASP = CT.MASP" +
            " GROUP BY SP.MASP, SP.TENSP, SP.DVT, SP.NUOCSX," +
            " SP.GIA, SP.MAKM, SP.STATUS,SP.IMAGEURL" +
            " ORDER BY tongSoLuongBan DESC; ", nativeQuery = true)
    List<Object[]> banchay();


}
