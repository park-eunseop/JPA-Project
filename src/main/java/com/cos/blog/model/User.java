 
package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //빌더패턴
@Entity //User 클래스가 mysql에 테이블이 생성이 된다.
public class User {
	
	@Id  //PK 어노테이션
	@GeneratedValue(strategy = GenerationType.IDENTITY)// 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다
	//오라클 - 시퀀스 , mysql  - auto-increment
	private int id;  //시퀀스, auto-increment
	
	@Column(nullable = false,length = 30)  //not null
	private String username; //아이디
	
	@Column(nullable = false,length = 100)
	private String password;
	
	@Column(nullable = false,length = 50)
	private String email;
	
	@ColumnDefault("'user'")  //문자라는걸 알려줘야해
	private String role;//Enum을 쓰는게 좋다. 도메인을 설정(범위를 정해)
	//admin , user, manager
	
	@CreationTimestamp //시간이 자동 입력
	private Timestamp createDate;
}
