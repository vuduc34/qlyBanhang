package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

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
            sanpham = sanphamRepository.save(sanpham);
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
                sanpham.setDvt(model.getDvt());
                sanpham.setGia(model.getGia());
                sanpham.setNuocsx(model.getNuocsx());
                sanpham.setTensp(model.getTensp());
                sanpham.setStatus(model.getStatus());
                sanpham = sanphamRepository.save(sanpham);
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
            if(sanpham == null) {
                responMessage.setMessage("Không tìm thấy thông tin sản phẩm");
                responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            } else {
                sanpham.setMakm(null);
                sanpham = sanphamRepository.save(sanpham);
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



}
