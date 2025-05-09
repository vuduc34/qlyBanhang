package project.psa.dataserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.entity.Khuyenmai;
import project.psa.dataserver.model.KhuyenmaiModel;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.repository.KhuyenmaiRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.SecureRandom;

@Service
public class KhuyenmaiService {
    @Autowired
    private KhuyenmaiRepository khuyenmaiRepository;

    @Autowired
    private LogService logService;

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();
    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        sb.append("KM");
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public ResponMessage create(KhuyenmaiModel model) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khuyenmai khuyenmai = new Khuyenmai();
            String id = generateRandomString();
            while (khuyenmaiRepository.existsById(id))  {
                id = generateRandomString();
            }
            khuyenmai.setId(id);
            khuyenmai.setNgaybd(model.getNgaybd());
            khuyenmai.setNgaykt(model.getNgaykt());
            khuyenmai.setNoidung(model.getNoidung());
            khuyenmai.setPhantram(model.getPhantram());
            khuyenmai.setStatus(model.getStatus());
            khuyenmai  = khuyenmaiRepository.save(khuyenmai);
            logService.create(getCurrentUsername(),constant.HANHDONG.CREATE,constant.DOITUONG.KHUYENMAI,null,mapper.writeValueAsString(khuyenmai));
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(khuyenmai);
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage update(KhuyenmaiModel model, String maKM) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Khuyenmai khuyenmai = khuyenmaiRepository.findKhuyenmaiById(maKM);
            if(khuyenmai==null) {
                responMessage.setMessage("Không tìm thấy thông tin khuyến mãi");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                String currentKM = mapper.writeValueAsString(khuyenmai);
                khuyenmai.setNgaybd(model.getNgaybd());
                khuyenmai.setNgaykt(model.getNgaykt());
                khuyenmai.setNoidung(model.getNoidung());
                khuyenmai.setPhantram(model.getPhantram());
                khuyenmai.setStatus(model.getStatus());
                khuyenmai  = khuyenmaiRepository.save(khuyenmai);
                String newKM = mapper.writeValueAsString(khuyenmai);
                logService.create(getCurrentUsername(),constant.HANHDONG.UPDATE,constant.DOITUONG.KHUYENMAI,currentKM,newKM);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setData(khuyenmai);
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
            responMessage.setData(khuyenmaiRepository.findAll());
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

    public ResponMessage findByMaKM(String maKM) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(khuyenmaiRepository.findKhuyenmaiById(maKM));
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
