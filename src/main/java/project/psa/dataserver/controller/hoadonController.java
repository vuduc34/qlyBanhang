package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.hoadonModel;
import project.psa.dataserver.service.HoadonService;

@RestController
@RequestMapping(constant.API.PREFIX+"/hoadon")
public class hoadonController {
    @Autowired
    private HoadonService hoadonService;

    @PostMapping("/create")
    @ResponseBody
    public ResponMessage create(@RequestBody hoadonModel model) {
        return hoadonService.create(model);
    }

    @GetMapping("/findAll")
    @ResponseBody
    public ResponMessage findAll() {
        return hoadonService.findAll();
    }

    @GetMapping("/findByMaKH")
    @ResponseBody
    public ResponMessage findByMaKH(@RequestParam String maKH) {
        return hoadonService.findByMaKH(maKH);
    }

    @GetMapping("/findByMaHD")
    @ResponseBody
    public ResponMessage findByMaHD(@RequestParam Integer maHD) {
        return hoadonService.findByHD(maHD);
    }
}
