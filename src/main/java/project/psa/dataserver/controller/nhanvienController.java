package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.changePassword;
import project.psa.dataserver.model.signUpData;
import project.psa.dataserver.model.updateNhanvien;
import project.psa.dataserver.service.NhanvienService;

@RestController
@RequestMapping(constant.API.PREFIX+"/nhanvien")
public class nhanvienController {
    @Autowired
    private NhanvienService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage signup(@RequestBody signUpData dto, @RequestParam String rolename) throws Exception {
        return  accountService.createAccount(dto,rolename);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage delete( @RequestParam String maNV,@RequestBody updateNhanvien model) throws Exception {
        return  accountService.updateNhanvien(maNV,model.getFullName(),model.getPhoneNumber());
    }

    @PutMapping("/changePassword")
    @ResponseBody
    public ResponMessage changePassword( @RequestBody changePassword model) throws Exception {
        return  accountService.changePassword(model);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage findAll() throws Exception {
        return  accountService.findAll();
    }

    @GetMapping("/findNhanvienActive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage findNhanvienActive() throws Exception {
        return  accountService.findByStatus(constant.ACCOUNT_STATUS.ACTIVE);
    }

    @GetMapping("/findNhanvienDeleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage findNhanvienDeleted() throws Exception {
        return  accountService.findByStatus(constant.ACCOUNT_STATUS.DELETED);
    }


    @GetMapping("/findAllRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage findAllRole() throws Exception {
        return  accountService.findAllRole();
    }

    @GetMapping("/findByMaNV")
    @ResponseBody
    public ResponMessage findByMaNV( @RequestParam String maNV) throws Exception {
        return  accountService.findByMaNV(maNV);
    }

    @DeleteMapping("/deleteByMaNV")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponMessage delete( @RequestParam String maNV) throws Exception {
        return  accountService.deleteNhanvien(maNV);
    }


}
