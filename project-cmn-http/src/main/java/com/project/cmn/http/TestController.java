package com.project.cmn.http;

import com.project.cmn.http.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final AdminService adminService;

    @GetMapping(value = "/retrieve/admin")
    public String retrieveAdmin(@RequestParam(name = "adminId") String adminId) {
        adminService.retrieveAdmin(adminId);
        return "main";
    }
}
