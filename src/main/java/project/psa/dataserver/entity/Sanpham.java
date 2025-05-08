package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Table(name = "SANPHAM")
@Entity
@Getter
@Setter
public class Sanpham {
    @Id
    @Column(name = "MASP", nullable = false, length = 8)
    private String id;

    @Column(name = "TENSP", nullable = false, length = 40)
    private String tensp;

    @Column(name = "DVT", length = 20)
    private String dvt;

    @Column(name = "NUOCSX", length = 40)
    private String nuocsx;

    @Column(name = "GIA", nullable = false, precision = 19, scale = 4)
    private BigDecimal gia;
    @OneToMany(mappedBy = "masp", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Cthd> cthds;

    @ManyToOne
    @JoinColumn(name = "MAKM")
    private Khuyenmai makm;

    @Column(name = "STATUS", length = 40)
    private String status;

}