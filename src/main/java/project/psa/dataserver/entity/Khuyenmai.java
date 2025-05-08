package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Table(name = "KHUYENMAI")
@Entity
@Getter
@Setter
public class Khuyenmai {
    @Id
    @Column(name = "MAKM", nullable = false, length = 10)
    private String id;

    @Column(name = "NOIDUNG", length = 100)
    private String noidung;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "NGAYBD")
    private LocalDate ngaybd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "NGAYKT")
    private LocalDate ngaykt;

    @Column(name = "PHANTRAM")
    private Integer phantram;
    @OneToMany(mappedBy = "makm", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Sanpham> sanphams;

    @Column(name = "STATUS", length = 40)
    private String status;

}