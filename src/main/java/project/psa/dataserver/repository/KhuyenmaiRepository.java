package project.psa.dataserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.psa.dataserver.entity.Khuyenmai;

import java.util.List;

@Repository
public interface KhuyenmaiRepository extends JpaRepository<Khuyenmai, String> {
    Khuyenmai findKhuyenmaiById(String id);
    @Query(value = "SELECT * FROM KHUYENMAI WHERE STATUS = 'available'", nativeQuery = true)
    List<Khuyenmai> findKhuyenmaiAvailable();
}
