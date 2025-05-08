package project.psa.dataserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Table(name = "LOG_HOATDONG")
@Entity
@Getter
@Setter
public class LogHoatdong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NGAYGIO")
    private Instant ngaygio;

    @Column(name = "NGUOITHUCHIEN", length = 20)
    private String nguoithuchien;

    @Column(name = "HANHDONG")
    private String hanhdong;

    @Column(name = "DOITUONG", length = 50)
    private String doituong;

    @Lob
    @Column(name = "GIATRI_CU")
    private String giatriCu;

    @Lob
    @Column(name = "GIATRI_MOI")
    private String giatriMoi;

    @JsonProperty("ngaygio") // Đổi tên lại đúng khi trả về
    public String getNgvlFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh")); // Hoặc ZoneId.systemDefault()
        return formatter.format(ngaygio);
    }


}