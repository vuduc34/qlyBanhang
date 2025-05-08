package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Khachhang;
import project.psa.dataserver.entity.Sanpham;
@Repository
public interface SanphamRepository extends JpaRepository<Sanpham, String> {
    Sanpham findSanphamById(String id);
}
