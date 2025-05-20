package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.service.ThongkeService;

@RestController
@RequestMapping(constant.API.PREFIX+"/thongke")
public class thongkeController {
    @Autowired
    private ThongkeService thongkeService;
    @GetMapping
    @ResponseBody
    public ResponMessage thongke() {
        return thongkeService.thongke();
    }
}
