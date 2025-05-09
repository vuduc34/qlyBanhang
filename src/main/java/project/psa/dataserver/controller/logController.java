package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.service.LogService;

@RestController
@RequestMapping(constant.API.PREFIX+"/log")
public class logController {
    @Autowired
    private LogService logService;

    @GetMapping("/findAll")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponMessage findAll() {
        return logService.findAll();
    }

    @GetMapping("/findByMaNV")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponMessage findByMaNV(@RequestParam String maNV) {
        return logService.findByMaNV(maNV);
    }
}
