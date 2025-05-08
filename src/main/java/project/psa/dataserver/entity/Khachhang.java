package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Table(name = "KHACHHANG")
@Entity
@Getter
@Setter
public class Khachhang {
    @Id
    @Column(name = "MAKH", nullable = false, length = 8)
    private String id;

    @Column(name = "HOTEN", nullable = false, length = 40)
    private String hoten;

    @Column(name = "DCHI", length = 50)
    private String dchi;

    @Column(name = "SODT", length = 20)
    private String sodt;

    @Column(name = "NGSINH",length = 20)
    private String ngsinh;

    @Column(name = "DOANHSO", precision = 19, scale = 4)
    private BigDecimal doanhso = BigDecimal.ZERO;;

    @Column(name = "NGDK", nullable = false)
    private Instant ngdk= Instant.now();

    @OneToMany(mappedBy = "makh", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Hoadon> hoadons;

    @Column(name = "STATUS", length = 40)
    private String status;

    @JsonProperty("ngdk") // Đổi tên lại đúng khi trả về
    public String getNgdkFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh")); // Hoặc ZoneId.systemDefault()
        return formatter.format(ngdk);
    }


}