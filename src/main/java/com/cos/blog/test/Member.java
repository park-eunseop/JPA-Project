package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@Data   //Getter Setter 합친거
//@AllArgsConstructor  //생성자 생성
//@RequiredArgsConstructor  //final 붙은 애들에 대한 생성자를 만들어줘

@Data
//@AllArgsConstructor
@NoArgsConstructor // 빈 생성자
public class Member {
	/*
	private final Integer id;   //final로 잡아주는 이유 : 불변성
	private final String username;
	private final String password;
	private final String email;
	*/
	private Integer id;  
	private String username;
	private String password;
	private String email;
	
	// 객체를 만들때 .builder().username().password().build() 이렇게 만들 수 있어
	// 어노테이션 하나로 빌더 패턴을 만들 수 있어
	@Builder
	public Member(Integer id, String username, String password, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	
	
}
