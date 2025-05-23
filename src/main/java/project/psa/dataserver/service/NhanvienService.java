package project.psa.dataserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.config.jwt.jwtProvider;
import project.psa.dataserver.entity.Nhanvien;
import project.psa.dataserver.entity.Role;
import project.psa.dataserver.model.*;
import project.psa.dataserver.repository.NhanvienRepository;
import project.psa.dataserver.repository.RoleRepository;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class NhanvienService {
    @Autowired
    private NhanvienRepository nhanvienRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private jwtProvider jwtProvider;

    @Autowired
    private LogService logService;

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        sb.append("NV");
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public ResponMessage signIn(SignInData data) {

        Nhanvien account = nhanvienRepository.findNhanvienById(data.getUserName());
        ResponMessage responMessage = new ResponMessage();
        if (account == null ) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.NOT_FOUND_USER);
            return responMessage;
        } else if (!passwordEncoder.matches(data.getPassWord(), account.getPassword())) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.PASSWORD_INCORRECT);
            return responMessage;
        } else if(!account.getStatus().equals(constant.ACCOUNT_STATUS.ACTIVE)) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ACCOUNT_DEACTIVE);
            return  responMessage;
        } else {
            try {
                String token = jwtProvider.generateToken(data.getUserName());
                Role role = account.getRoleid();
                loginResponse loginResponse = new loginResponse();
                loginResponse.setToken(token);
                loginResponse.setUsername(data.getUserName());
                loginResponse.setRole(role.getRolename());
                loginResponse.setFullname(account.getHoten());
                loginResponse.setPhoneNumber(account.getSodt());
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                logService.create(account.getId(),constant.HANHDONG.LOGIN,constant.DOITUONG.NHANVIEN,mapper.writeValueAsString(account),null);
                responMessage.setData(loginResponse);
                return responMessage;
            } catch (Exception e) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ERROR);
                responMessage.setData(e.getMessage());
                return responMessage;
            }
        }
    }

    public ResponMessage createAccount(signUpData signUp, String roleName) throws Exception {
        ResponMessage responMessage = new ResponMessage();
        try{
            Role role = roleRepository.findByRolename(roleName);
            String username = generateRandomString();
            while (nhanvienRepository.existsById(username)) {
                username = generateRandomString();
            }
          if(role == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage(constant.MESSAGE.ROLE_ERROR);
                return responMessage;
            } else {
                Nhanvien account = new Nhanvien();
                account.setId(username);
                account.setPassword(passwordEncoder.encode(signUp.getPassword()));
                account.setSodt(signUp.getPhoneNumber());
                account.setHoten(signUp.getFullName());
                account.setRoleid(role);
                account.setNgvl(Instant.now());
                account.setStatus(constant.ACCOUNT_STATUS.ACTIVE);
                account =  nhanvienRepository.save(account);
                logService.create(getCurrentUsername(),constant.HANHDONG.CREATE,constant.DOITUONG.NHANVIEN,null,mapper.writeValueAsString(account));
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(account);
                return responMessage;
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
            return  responMessage;
        }

    }

    public ResponMessage updateNhanvien(String nhanvienId,String ten, String sdt) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Nhanvien nhanvien = nhanvienRepository.findNhanvienById(nhanvienId);
            if(nhanvien == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage("Không tìm thấy thông tin nhân viên");
            } else {
                String current = mapper.writeValueAsString(nhanvien);
                nhanvien.setHoten(ten);
                nhanvien.setSodt(sdt);
                nhanvien = nhanvienRepository.save(nhanvien);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(nhanvien);
                logService.create(getCurrentUsername(),constant.HANHDONG.UPDATE,constant.DOITUONG.NHANVIEN,current,mapper.writeValueAsString(nhanvien));
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage changePassword(changePassword model) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Nhanvien nhanvien = nhanvienRepository.findNhanvienById(model.getMaNV());
            if(nhanvien == null) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage("Không tìm thấy thông tin nhân viên");
            } else if(!passwordEncoder.matches(model.getCurrentPassword(), nhanvien.getPassword())) {
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
                responMessage.setMessage("Mật khẩu hiện tại không đúng");
            } else {
                nhanvien.setPassword(passwordEncoder.encode(model.getNewPassword()));
                nhanvien = nhanvienRepository.save(nhanvien);
                responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
                responMessage.setMessage(constant.MESSAGE.SUCCESS);
                responMessage.setData(nhanvien);
            }
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage findAll() {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setData(nhanvienRepository.findAll());
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage findByStatus(String status) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setData(nhanvienRepository.findNhanvienByStatus(status));
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        return responMessage;
    }



    public ResponMessage findAllRole() {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setData(roleRepository.findAll());
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }
    public ResponMessage deleteNhanvien(String nhanvienId) {
        ResponMessage responMessage = new ResponMessage();
        try {
            Nhanvien nhanvien = nhanvienRepository.findNhanvienById(nhanvienId);
            if(nhanvien != null) {
                nhanvien.setStatus(constant.ACCOUNT_STATUS.DELETED);
                nhanvien = nhanvienRepository.save(nhanvien);
                logService.create(getCurrentUsername(),constant.HANHDONG.DELETE,constant.DOITUONG.NHANVIEN,nhanvienId,null);
            }
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(nhanvien);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
        }
        return responMessage;
    }

    public ResponMessage findByMaNV(String nhanvienId) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setData(nhanvienRepository.findNhanvienById(nhanvienId));
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
        } catch (Exception e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(e.getMessage());
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
