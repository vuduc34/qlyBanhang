package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.SanphamModel;
import project.psa.dataserver.repository.SanphamRepository;
import project.psa.dataserver.service.SanphamService;

@RestController
@RequestMapping(constant.API.PREFIX+"/sanpham")
public class sanphamController {
    @Autowired
    private SanphamService sanphamService;

    @GetMapping("/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return sanphamService.findAll();
    }

    @GetMapping("/findByMaSP")
    @ResponseBody
    public ResponMessage findByMaSP(@RequestParam String maSP) {
        return sanphamService.findById(maSP);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponMessage create(@RequestBody SanphamModel model) {
        return sanphamService.create(model);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponMessage update(@RequestBody SanphamModel model,@RequestParam String maSP) {
        return sanphamService.update(model,maSP);
    }

    @GetMapping("/addKhuyenmai")
    @ResponseBody
    public ResponMessage addKhuyenmai(@RequestParam String maSP,@RequestParam String maKM) {
        return sanphamService.addKhuyenmai(maSP,maKM);
    }

    @GetMapping("/removeKhuyenmai")
    @ResponseBody
    public ResponMessage removeKhuyenmai(@RequestParam String maSP) {
        return sanphamService.reomveKhuyenmai(maSP);
    }
}
