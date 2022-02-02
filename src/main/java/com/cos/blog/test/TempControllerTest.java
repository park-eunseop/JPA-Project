package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //파일을 리턴해   ,  RestController data를 리턴해
public class TempControllerTest {

	@GetMapping("/temp/home")
	public String tempHome() {
		
		System.out.println("tempHome");
		
		//파일 리턴 기본경로 src/main/resources/static 
		//풀경로 : src/main/resources/static/home.html
		//우리는 템플릿 엔진을 jsp를 쓸꺼야
		return "/home.html";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		//prefix : /WEB-INF/views/
		//suffix : .jsp
		//풀경로 :  /WEB-INF/views/test.jsp
		
		
		return "test";
	}
}
