package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.signUpData;
import project.psa.dataserver.service.NhanvienService;

@RestController
@RequestMapping(constant.API.PREFIX+"/nhanvien")
public class nhanvienController {
    @Autowired
    private NhanvienService accountService;

    @PostMapping("/create")
    @ResponseBody
    public ResponMessage signup(@RequestBody signUpData dto, @RequestParam String rolename) throws Exception {
        return  accountService.createAccount(dto,rolename);
    }

    @GetMapping("/findAll")
    @ResponseBody
    public ResponMessage findAll() throws Exception {
        return  accountService.findAll();
    }

    @GetMapping("/findAllRole")
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
    @ResponseBody
    public ResponMessage delete( @RequestParam String maNV) throws Exception {
        return  accountService.deleteNhanvien(maNV);
    }


}
