package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.SanPhamThongKeDTO;
import project.psa.dataserver.repository.HoadonRepository;
import project.psa.dataserver.repository.KhachhangRepository;
import project.psa.dataserver.repository.NhanvienRepository;
import project.psa.dataserver.repository.SanphamRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThongkeService {
    @Autowired
    private SanphamRepository sanphamRepository;
    @Autowired
    private KhachhangRepository khachhangRepository;
    @Autowired
    private HoadonRepository hoadonRepository;
    @Autowired
    private NhanvienRepository nhanvienRepository;

    public ResponMessage thongke() {
        ResponMessage responMessage = new ResponMessage();
        try {
            int soSP = sanphamRepository.countSanpham();
            int soKH = khachhangRepository.countKhachHang();
            int soHD = hoadonRepository.countHoaDon();
            int soNV = nhanvienRepository.countNhanvien();
            int soSpAvailable = sanphamRepository.countSanphamAvailable();
            int soKhActive = khachhangRepository.countKhachHangActive();
            int soNvActive = nhanvienRepository.countNhanvienActive();
            Map<String, Object> data = new HashMap();
            Map<String, Integer> sp = new HashMap();
            Map<String, Integer> kh = new HashMap();
            Map<String, Integer> nv = new HashMap();
            Map<String, BigDecimal> dt = new HashMap();
            sp.put("soSanpham",soSP);
            sp.put("soSanphamAvailable",soSpAvailable);
            kh.put("soKhachhang", soKH);
            kh.put("soKhachangActive",soKhActive);
            nv.put("soNhanvien", soNV);
            nv.put("soNhanvienActive",soNvActive);
            data.put("soHoadon",soHD);
            data.put("sanpham",sp);
            data.put("khachang",kh);
            data.put("nhanvien",nv);
            data.put("top5Sanpham",getTop5SanPhamThongKe());
            dt.put("homNay",hoadonRepository.doanhThuHomNay());
            dt.put("thangNay",hoadonRepository.doanhThuThangNay());
            dt.put("quyNay",hoadonRepository.doanhThuQuyNay());
            data.put("doanhthu",dt);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(data);
        } catch (Exception e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return  responMessage;
    }

    public List<SanPhamThongKeDTO> getTop5SanPhamThongKe() {
        List<Object[]> rawList = sanphamRepository.banchay();

        return rawList.stream().map(row -> {
            SanPhamThongKeDTO dto = new SanPhamThongKeDTO();
            dto.setMasp((String) row[0]);
            dto.setTensp((String) row[1]);
            dto.setDvt((String) row[2]);
            dto.setNuocsx((String) row[3]);
            dto.setGia(((Number) row[4]).doubleValue());
            dto.setMakm((String) row[5]);
            dto.setStatus((String) row[6]);
            dto.setImageUrl((String) row[7]);
            dto.setTongSoLuongBan(((Number) row[8]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
