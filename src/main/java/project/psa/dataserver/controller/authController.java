package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.model.SignInData;
import project.psa.dataserver.model.signUpData;
import project.psa.dataserver.service.NhanvienService;


@RestController
@RequestMapping(constant.API.PREFIX_AUTH)
public class authController {

    @Autowired
    private NhanvienService accountService;

    @PostMapping("/signin")
    @ResponseBody
    public ResponMessage signIn(@RequestBody SignInData data) {
        return accountService.signIn(data);
    }

//    @PostMapping("/signup")
//    @ResponseBody
//    public ResponMessage signup(@RequestBody signUpData dto,@RequestParam String rolename) throws Exception {
//        return  accountService.createAccount(dto,rolename);
//
//    }


}
