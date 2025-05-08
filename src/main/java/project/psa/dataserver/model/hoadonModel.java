package project.psa.dataserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class hoadonModel {
    private String maKH;
    private String maNV;
    private List<orderModel> sanpham;
}
