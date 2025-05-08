package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.psa.dataserver.entity.Khachhang;
import project.psa.dataserver.entity.Nhanvien;
import project.psa.dataserver.repository.KhachhangRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.psa.dataserver.repository.NhanvienRepository;

@Service
public class customUserDetailService implements UserDetailsService {
    @Autowired
    private NhanvienRepository nhanvienRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Nhanvien account = nhanvienRepository.findNhanvienById(username);
        return customUserDetail.createCustomUserDetails(account);
    }
}