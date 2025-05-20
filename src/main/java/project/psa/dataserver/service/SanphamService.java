package project.psa.dataserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.entity.Khuyenmai;
import project.psa.dataserver.entity.Sanpham;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.SanphamModel;
import project.psa.dataserver.repository.KhuyenmaiRepository;
import project.psa.dataserver.repository.SanphamRepository;
import java.security.SecureRandom;

@Service
public class SanphamService {
    @Autowired
    private KhuyenmaiRepository khuyenmaiRepository;
    @Autowired
    private SanphamRepository sanphamRepository;
    @Autowired
    private LogService logService;

    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        sb.append("SP");
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public ResponMessage create(SanphamModel model) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Sanpham sanpham = new Sanpham();
            String id = generateRandomString();
            while (sanphamRepository.existsById(id))  {
                id = generateRandomString();
            }
            sanpham.setId(id);
            sanpham.setDvt(model.getDvt());
            sanpham.setGia(model.getGia());
            sanpham.setNuocsx(model.getNuocsx());
            sanpham.setTensp(model.getTensp());
            sanpham.setStatus(model.getStatus());
            sanpham.setImageurl(model.getImageurl());
            sanpham = sanphamRepository.save(sanpham);
            logService.create(getCurrentUsername(),constant.HANHDONG.CREATE,constant.DOITUONG.SANPHAM,null,mapper.writeValueAsString(sanpham));
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(sanpham);
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }


    public ResponMessage update(SanphamModel model,String maSP) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Sanpham sanpham = sanphamRepository.findSanphamById(maSP);
            if(sanpham == null) {
                responMessage.setMessage("Không tìm thấy thông tin sản phẩm");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                String currentSp = mapper.writeValueAsString(sanpham);
                sanpham.setDvt(model.getDvt());
                sanpham.setGia(model.getGia());
                sanpham.setNuocsx(model.getNuocsx());
                sanpham.setTensp(model.getTensp());
                sanpham.setStatus(model.getStatus());
                sanpham.setImageurl(model.getImageurl());
                sanpham = sanphamRepository.save(sanpham);
                String newSp = mapper.writeValueAsString(sanpham);
                logService.create(getCurrentUsername(),constant.HANHDONG.UPDATE,constant.DOITUONG.SANPHAM,currentSp,newSp);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(sanpham);
            }
        } catch (Exception  e) {
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
            responMessage.setData(sanphamRepository.findAll());
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findById(String maSP) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(sanphamRepository.findSanphamById(maSP));
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }



    public ResponMessage addKhuyenmai(String maSP, String maKM) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Sanpham sanpham = sanphamRepository.findSanphamById(maSP);
            String currentSp = mapper.writeValueAsString(sanpham);
            Khuyenmai khuyenmai = khuyenmaiRepository.findKhuyenmaiById(maKM);
            if(sanpham == null) {
                responMessage.setMessage("Không tìm thấy thông tin sản phẩm");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else if(khuyenmai == null) {
                responMessage.setMessage("Không tìm thấy thông tin khuyến mãi");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else  if(khuyenmai.getStatus().equals(constant.KHUYENMAI_STATUS.UN_AVAILABLE)) {
                responMessage.setMessage("Mã khuyến mãi không khả dụng");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                sanpham.setMakm(khuyenmai);
                sanpham = sanphamRepository.save(sanpham);
                String newSp = mapper.writeValueAsString(sanpham);
                logService.create(getCurrentUsername(),constant.HANHDONG.ADDKM,constant.DOITUONG.SANPHAM,currentSp,newSp);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(sanpham);
            }
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage reomveKhuyenmai(String maSP) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Sanpham sanpham = sanphamRepository.findSanphamById(maSP);
            String currentSp = mapper.writeValueAsString(sanpham);
            if(sanpham == null) {
                responMessage.setMessage("Không tìm thấy thông tin sản phẩm");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                sanpham.setMakm(null);
                sanpham = sanphamRepository.save(sanpham);
                logService.create(getCurrentUsername(),constant.HANHDONG.REMOVEKM,constant.DOITUONG.SANPHAM,currentSp,mapper.writeValueAsString(sanpham));
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(sanpham);
            }
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

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



}
