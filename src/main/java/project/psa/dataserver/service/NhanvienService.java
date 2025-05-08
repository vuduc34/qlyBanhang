package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.config.jwt.jwtProvider;
import project.psa.dataserver.entity.Nhanvien;
import project.psa.dataserver.entity.Role;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.SignInData;
import project.psa.dataserver.model.loginResponse;
import project.psa.dataserver.model.signUpData;
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

    public ResponMessage findAll() {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setData(nhanvienRepository.findAllNhanvien());
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
            nhanvien.setStatus(constant.ACCOUNT_STATUS.DELETED);
            nhanvienRepository.save(nhanvien);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
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





}
