package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.entity.LogHoatdong;
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

}
