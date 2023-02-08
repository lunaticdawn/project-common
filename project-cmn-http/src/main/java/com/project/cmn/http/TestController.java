package com.project.cmn.http;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping(value = "/")
	public String getMain() throws Exception {
		return "main";
	}
}
