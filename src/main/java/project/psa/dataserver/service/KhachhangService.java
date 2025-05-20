package project.psa.dataserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.entity.Khachhang;
import project.psa.dataserver.model.KhachhangModel;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.repository.KhachhangRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.SecureRandom;

@Service
public class KhachhangService {
    @Autowired
    private KhachhangRepository khachhangRepository;

    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

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
    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        sb.append("KH");
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public ResponMessage create(KhachhangModel model) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khachhang khachhang  = new Khachhang();
            String id = generateRandomString();
            while (khachhangRepository.existsById(id))  {
                id = generateRandomString();
            }
            khachhang.setId(id);
            khachhang.setHoten(model.getHoten());
            khachhang.setDchi(model.getDchi());
            khachhang.setNgsinh(model.getNgsinh());
            khachhang.setSodt(model.getSodt());
            khachhang.setStatus(constant.ACCOUNT_STATUS.ACTIVE);
            khachhang = khachhangRepository.save(khachhang);
            logService.create(getCurrentUsername(),constant.HANHDONG.CREATE,constant.DOITUONG.KHACHHANG,null,mapper.writeValueAsString(khachhang));
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(khachhang);
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage update(KhachhangModel model, String khachhangId) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khachhang khachhang  = khachhangRepository.findKhachhangById(khachhangId);
            if(khachhang==null) {
                responMessage.setMessage("Không tìm thấy thông tin khách hàng");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                String currentKH = mapper.writeValueAsString(khachhang);
                khachhang.setHoten(model.getHoten());
                khachhang.setDchi(model.getDchi());
                khachhang.setNgsinh(model.getNgsinh());
                khachhang.setSodt(model.getSodt());
                khachhang = khachhangRepository.save(khachhang);
                String newKH = mapper.writeValueAsString(khachhang);
                logService.create(getCurrentUsername(),constant.HANHDONG.UPDATE,constant.DOITUONG.KHACHHANG,currentKH,newKH);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(khachhang);
            }
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage delete(String khachhangId) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khachhang khachhang  = khachhangRepository.findKhachhangById(khachhangId);
            if(khachhang==null) {
                responMessage.setMessage("Không tìm thấy thông tin khách hàng");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                khachhang.setStatus(constant.ACCOUNT_STATUS.DELETED);
                khachhang = khachhangRepository.save(khachhang);
                logService.create(getCurrentUsername(),constant.HANHDONG.DELETE,constant.DOITUONG.KHACHHANG,khachhangId,null);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(khachhang);
            }
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findByStatus(String status) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(khachhangRepository.findKhachhangByStatus(status));
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
            responMessage.setData(khachhangRepository.findAll());
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findByKhachhangID(String khachhangID) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(khachhangRepository.findKhachhangById(khachhangID));
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

}
