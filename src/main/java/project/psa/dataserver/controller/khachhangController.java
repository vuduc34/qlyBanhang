package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.KhachhangModel;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.service.KhachhangService;

@RestController
@RequestMapping(constant.API.PREFIX+"/khachhang")
public class khachhangController {
    @Autowired
    private KhachhangService khachhangService;
    @GetMapping("/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return khachhangService.findAll();
    }

    @GetMapping("/findKhachHangActive")
    @ResponseBody
    public ResponMessage findKhachHangActive() {
        return khachhangService.findByStatus(constant.ACCOUNT_STATUS.ACTIVE);
    }

    @GetMapping("/findKhachHangDeleted")
    @ResponseBody
    public ResponMessage findKhachHangDeleted() {
        return khachhangService.findByStatus(constant.ACCOUNT_STATUS.DELETED);
    }

    @GetMapping("/findByMaKH")
    @ResponseBody
    public ResponMessage findByMaKH(@RequestParam String maKH) {
        return khachhangService.findByKhachhangID(maKH);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponMessage create(@RequestBody KhachhangModel model) {
       return khachhangService.create(model);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponMessage update(@RequestBody KhachhangModel model,@RequestParam String maKH) {
        return khachhangService.update(model,maKH);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponMessage delete(@RequestParam String maKH) {
        return khachhangService.delete(maKH);
    }

}
