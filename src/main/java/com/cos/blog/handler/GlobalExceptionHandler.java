package com.cos.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;import lombok.val;

@ControllerAdvice  //모든 Exception이 발생하면 이 class 로 올꺼야
@RestController
public class GlobalExceptionHandler {
	
	//IllegalArgumentException Exception이발생하면 여기로 들어와
	@ExceptionHandler(value=IllegalArgumentException.class)
	public String handleArgumentException(IllegalArgumentException e) {
		return "<h1>"+e.getMessage()+"</h1>";
	}
	

}
