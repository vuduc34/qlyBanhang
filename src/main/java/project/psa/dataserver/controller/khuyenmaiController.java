package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.KhuyenmaiModel;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.service.KhuyenmaiService;

@RestController
@RequestMapping(constant.API.PREFIX+"/khuyenmai")
public class khuyenmaiController {
    @Autowired
    private KhuyenmaiService khuyenmaiService;
    @GetMapping("/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return khuyenmaiService.findAll();
    }

    @GetMapping("/findByMaKM")
    @ResponseBody
    public ResponMessage findByMaKH(@RequestParam String maKM) {
        return khuyenmaiService.findByMaKM(maKM);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponMessage create(@RequestBody KhuyenmaiModel model) {
        return khuyenmaiService.create(model);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponMessage update(@RequestBody KhuyenmaiModel model,@RequestParam String maKM) {
        return khuyenmaiService.update(model,maKM);
    }

}
