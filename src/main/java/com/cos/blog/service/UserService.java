package com.cos.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;


//스프링이 컴포넌트 스캔을 통해서 bean에 등록을 해줌. IOC
@Service
public class UserService {
/*
 * 서비스가 왜 필요한지
 * 1)트랜잭션을 관리
 * 2)서비스의 의미 : 하나의 기능인데 여러가지 crud 등 로직들이 묶여서 하나의 서비스가 된다. 
 *  			  그래서 한기능이라도 실패하면 롤백해야해
 */
	@Autowired
	private UserRepository userRepository;
	
	
	@Transactional
	public void join(User user) {
		userRepository.save(user);
	}
	
	
}
