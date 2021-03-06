package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자가 요청 -> 응답(html)    @Controller
// 사용자가 요청 -> 응답(data)    @RestController
// 프로젝트 >
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest:";

	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = new Member();
		Member m1 = new Member(1, "1", "2", "3");
		m.setId(5000);
		System.out.println(m.getId());
		Member m2 = Member.builder().username("name").password("1234").build();
		return null;
	}
	
	//인터넷 브라우저 요청은 무조건 get요청만 할 수 있어
	//http://localhost:8080/http/get
	@GetMapping("/http/get")
	public String getTest(Member m) {
		return "get 요청"+m.getId()+","+m.getUsername();
	}
	
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		
		return "post 요청"+m.getId()+","+m.getUsername();
	}
	
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청";
	}
	
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
