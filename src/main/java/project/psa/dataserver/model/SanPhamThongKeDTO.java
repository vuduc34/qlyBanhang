package project.psa.dataserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanPhamThongKeDTO {
    private String masp;
    private String tensp;
    private String dvt;
    private String nuocsx;
    private Double gia;
    private String makm;
    private String status;
    private Integer tongSoLuongBan;
    private String imageUrl;
}
