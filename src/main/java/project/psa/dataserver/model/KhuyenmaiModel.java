package project.psa.dataserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import project.psa.dataserver.entity.Sanpham;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class KhuyenmaiModel {
    private String noidung;
    private LocalDate ngaybd;
    private LocalDate ngaykt;
    private Integer phantram;
    private String status;
}
