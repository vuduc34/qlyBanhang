package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Khuyenmai;
@Repository
public interface KhuyenmaiRepository extends JpaRepository<Khuyenmai, String> {
    Khuyenmai findKhuyenmaiById(String id);
}
