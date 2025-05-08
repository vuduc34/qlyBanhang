package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Table(name = "NHANVIEN")
@Entity
@Getter
@Setter
public class Nhanvien {
    @Id
    @Column(name = "MANV", nullable = false, length = 8)
    private String id;

    @Column(name = "HOTEN", nullable = false, length = 40)
    private String hoten;

    @Column(name = "SODT", length = 20)
    private String sodt;

    @Column(name = "NGVL", nullable = false)
    private Instant ngvl;
    @OneToMany(mappedBy = "manv", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Hoadon> hoadons;

    @ManyToOne
    @JoinColumn(name = "ROLEID")
    private Role roleid;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name = "STATUS", length = 40)
    private String status;

    @JsonProperty("ngvl") // Đổi tên lại đúng khi trả về
    public String getNgvlFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh")); // Hoặc ZoneId.systemDefault()
        return formatter.format(ngvl);
    }

}