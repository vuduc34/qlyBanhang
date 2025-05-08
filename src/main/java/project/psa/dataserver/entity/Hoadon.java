package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Table(name = "HOADON")
@Entity
@Getter
@Setter
public class Hoadon {
    @Id
    @Column(name = "SOHD", nullable = false)
    private Integer id;

    @Column(name = "NGHD", nullable = false)
    private Instant nghd =   Instant.now();

    @ManyToOne
    @JoinColumn(name = "MAKH")
    private Khachhang makh;

    @ManyToOne
    @JoinColumn(name = "MANV")
    private Nhanvien manv;

    @Column(name = "TRIGIA")
    private BigDecimal trigia;

    @OneToMany(mappedBy = "sohd", cascade = CascadeType.ALL)
    private Set<Cthd> cthds;

    @JsonProperty("nghd") // Đổi tên lại đúng khi trả về
    public String getNgvlFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh")); // Hoặc ZoneId.systemDefault()
        return formatter.format(nghd);
    }

}