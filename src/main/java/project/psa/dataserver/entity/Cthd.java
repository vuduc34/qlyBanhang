package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "CTHD")
@Entity
@Getter
@Setter
public class Cthd {
    @EmbeddedId
    @JsonIgnore
    private CthdId id;

    @MapsId("masp")
    @ManyToOne
    @JoinColumn(name = "MASP")
    private Sanpham masp;

    @MapsId("sohd")
    @ManyToOne
    @JoinColumn(name = "SOHD")
    @JsonIgnore
    private Hoadon sohd;

    @Column(name = "SL", nullable = false)
    private Integer sl;
    @Column(name = "GIA")
    private BigDecimal gia;

//    public String getMasp() {
//        return masp != null ? masp.getId(): null;
//    }
//
//    public Integer getSohd() {
//        return sohd != null ? sohd.getId(): null;
//    }
}