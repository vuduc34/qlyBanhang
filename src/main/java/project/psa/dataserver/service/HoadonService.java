package project.psa.dataserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.entity.*;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.hoadonModel;
import project.psa.dataserver.model.orderModel;
import project.psa.dataserver.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class HoadonService {
    @Autowired
    private HoadonRepository hoadonRepository;
    @Autowired
    private CthdRepository cthdRepository;
    @Autowired
    private KhachhangRepository khachhangRepository;
    @Autowired
    private NhanvienRepository nhanvienRepository;
    @Autowired
    private SanphamRepository sanphamRepository;

    @Autowired
    private LogService logService;

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString(); // Nếu không phải UserDetails thì lấy toString (ví dụ String username)
            }
        }
        return null;
    }

    private static final Random random = new Random();

    public ResponMessage create(hoadonModel model) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khachhang khachhang = khachhangRepository.findKhachhangById(model.getMaKH());
            Nhanvien nhanvien = nhanvienRepository.findNhanvienById(model.getMaNV());
            boolean validSP = true;
            List<orderModel> list = model.getSanpham();
            for(int i = 0; i<list.size();i++) {
                Sanpham  sp = sanphamRepository.findSanphamById(list.get(i).getMaSP());
                if(sp == null || sp.getStatus().equals(constant.SANPHAM_STATUS.UN_AVAILABLE)) {
                    validSP = false;
                    break;
                }
            }
            if(khachhang == null) {
                responMessage.setMessage("Không tìm thấy thông tin khách hàng");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else if(nhanvien == null) {
                responMessage.setMessage("Không tìm thấy thông tin nhân viên");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else if(!(nhanvien.getStatus().equals(constant.ACCOUNT_STATUS.ACTIVE))) {
                responMessage.setMessage("Nhân viên không ở trạng thái hợp lệ");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            }else if(!(khachhang.getStatus().equals(constant.ACCOUNT_STATUS.ACTIVE))) {
                responMessage.setMessage("Khách hàng không ở trạng thái hợp lệ");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else if(!validSP) {
                responMessage.setMessage("Sản phẩm không khả dụng");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                Hoadon hoadon = new Hoadon();
                int id = generateRandomId();
                while (hoadonRepository.existsById(id)) {
                    id = generateRandomId();
                }
                hoadon.setId(id);
                hoadon.setMakh(khachhang);
                hoadon.setManv(nhanvien);
                hoadon = hoadonRepository.save(hoadon);
                BigDecimal tonghoadon = new BigDecimal("0");
                for (int i = 0; i< list.size();i++) {
                    orderModel orderModel = list.get(i);
                    Sanpham sanpham = sanphamRepository.findSanphamById(orderModel.getMaSP());
                    Khuyenmai khuyenmai = sanpham.getMakm();
                    if(khuyenmai!=null) {
                        Cthd cthd = new Cthd();
                        CthdId cthdId = new CthdId();
                        cthdId.setMasp(sanpham.getId());
                        cthdId.setSohd(hoadon.getId());
                        cthd.setId(cthdId);
                        cthd.setSohd(hoadon);
                        cthd.setMasp(sanpham);
                        BigDecimal tiLeConLai = new BigDecimal("100").subtract(new BigDecimal(khuyenmai.getPhantram())).divide(new BigDecimal("100"));
                        BigDecimal giaConLai = sanpham.getGia().multiply(tiLeConLai);
                        cthd.setGia(giaConLai);
                        cthd.setSl(orderModel.getSoluong());
                        tonghoadon = tonghoadon.add(giaConLai.multiply(BigDecimal.valueOf(orderModel.getSoluong())));
                        cthdRepository.save(cthd);
                    } else {
                        Cthd cthd = new Cthd();
                        CthdId cthdId = new CthdId();
                        cthdId.setMasp(sanpham.getId());
                        cthdId.setSohd(hoadon.getId());
                        cthd.setId(cthdId);
                        cthd.setSohd(hoadon);
                        cthd.setMasp(sanpham);
                        cthd.setGia(sanpham.getGia());
                        cthd.setSl(orderModel.getSoluong());
                        tonghoadon = tonghoadon.add(sanpham.getGia().multiply(BigDecimal.valueOf(orderModel.getSoluong())));
                        cthdRepository.save(cthd);
                    }
                }
                hoadon.setTrigia(tonghoadon);
                hoadon = hoadonRepository.save(hoadon);
                khachhang.setDoanhso(khachhang.getDoanhso().add(tonghoadon));
                khachhangRepository.save(khachhang);
                logService.create(getCurrentUsername(),constant.HANHDONG.CREATE,constant.DOITUONG.HOADON,
                        null,mapper.writeValueAsString(hoadon));
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(hoadon);
            }

        } catch (Exception e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findAll() {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(hoadonRepository.findAllHoaDon());
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findByMaKH(String maKH) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(hoadonRepository.findHoaDonByMaKH(maKH));
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findByHD(Integer maHD) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(hoadonRepository.findHoadonById(maHD));
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }




    public static int generateRandomId() {
        int min = 1001;
        int bound = Integer.MAX_VALUE - min;
        return random.nextInt(bound) + min;
    }
}
