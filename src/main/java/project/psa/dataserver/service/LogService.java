package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.entity.LogHoatdong;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.repository.LogRepository;

import java.time.Instant;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void create(String nguoithuchien,String hanhdong, String doituong, String giatricu, String giatrimoi) {
        LogHoatdong logHoatdong = new LogHoatdong();
        logHoatdong.setDoituong(doituong);
        logHoatdong.setGiatriCu(giatricu);
        logHoatdong.setGiatriMoi(giatrimoi);
        logHoatdong.setHanhdong(hanhdong);
        logHoatdong.setNguoithuchien(nguoithuchien);
        logHoatdong.setNgaygio(Instant.now());
        logRepository.save(logHoatdong);
    }

    public ResponMessage findByMaNV(String maNV) {
        ResponMessage responMessage = new ResponMessage();
        try {
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setData(logRepository.findByMaNV(maNV));
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
            responMessage.setData(logRepository.findAllLog());
        } catch (Exception  e) {
            responMessage.setMessage(e.getMessage());
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
        }
        return responMessage;
    }

}
