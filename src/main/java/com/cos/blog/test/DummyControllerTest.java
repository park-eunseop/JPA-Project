package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
/*
 * 영속성 컨텍스트
 * controller <-> JAP(영속성 컨텍스트,1차캐시) <-(flush)> DB
 * 
 * 
 */
/*
 * 트랜잭션: 일이 처리되기 위한 가장 작은 단위
 * 여러개의 트랜잭션이 모여서 하나의 트랜잭션으로 처리할 수 있고 , 스프링에서는 서비스라 부른다.
 * 
 * DB격리 수준
 * 오라클 : read commit
 * 데이터 변경을 요청하는 어떤 쿼리문이 날라오면, 일단 db가 undo(이전데이터를 가지고 있는 영역) 영역의
 * 데이터를 변경하고, select를 하면 undo 영역에서 읽어
 * 단점: 데이터의 정확성이 깨질때가 있어(팬텀 리드: select 할때마다 데이터가 보였다 안보였다 하는 경우)
 * 
 * MYSQL : repeatable read 이상  -> 부정합 발생 X
 * 트랜잭션이 종료되지 않았다면, 항상 동일한 결과를 보여준다.
 * undo에서 변경되더라도 기존 데이터를 보여준다.
 * 자기 트랜잭션 번호보다 낮은 undo 로그를 보고 select한다.
 * 
 * 
 * 스프링에서 CRUD 할때 , select를 할때에도 정합성을 위해서 @Transactional 어노테이션을 꼭 붙혀야한다.
 * for 정합성 유지를 위해
 * 
 * 
 * 
 * 스프링 시작
 * 1. 톰켓 시작 > 서버작동
 * 2. web.xml 읽는다. 
 * 3. context.xml 읽어서 >db 연결 테스트
 * 
 *  전통적인 트랜잭션 방법
 *	스프링 컨테이너
 *   (Controller)  <->  (Service)  <->  (Repository) <-> 영속성 컨텍스트  <-> DB
 *  여기서 request 를 할때 DB연결 세션 생성하고, 트랜잭션을 시작(web.xml)을 하고 영속석 컨텍스트가 시작된다.
 *  Controller에서 트랜잭션을 종료하고 변경감지(더티체킹)를 해서 DB에 flush한다. 그 후 response 를 보내고 DB연결 세션 종료 
 *  정리)
 *  1. jdbc 커넥션 시작
 *  2. 트랜잭션 시작
 *  3. 영속성 컨텍스트 시작
 *  4. jdbc 커넥션 종료
 *  5. 트랜잭션 종료 ( 변경감지)
 *  6. 영속성 컨텍스트 종료
 *  
 *  jdbc 커넥션의 시간이 너무 많아.
 *  
 *	이방식에 문제점이 있어 
 *	1. 영속성 컨텍스트 시작
 *  2. 트랜잭션, jdbc 연결 시작
 *  3. 영속성,jdbc, 트랜잭션 종료
 *  
 *  > 개선방안(open in view = true)
 *  
 *  Lazy 와 Eager의 차이
 *  Eager는 join 된 정보까지 다가져온다.
 *  Lazy(지연로딩)는 join 된 정보는 필요없어, 연결된 프록시(빈) 객체를 가져와
 *  
 *  영속성 컨텍스트 종료를  response하기 전에 하면 프록시 객체로 영속성 컨텍스트에서 가져올수있어
 *  프록시가 jdbc 컨넥션을 시작하고 원하는 정보를 가져오고 컨넥션 종료한다.
 *  변경감지를 하지는 않아.(트랜잭션은 종료했기 때문에)
 *  
 *  jpa : open-in-view: true 로 하면 이때부터 lazy 로딩이 가능하다.
 *  getinthere.tistory.com/20에 정리  >>/27
 *  
 *  false를 하면 lazy 로딩이 안된다. 왜냐? 영속성컨텍스트 종료시점이 service로 내려오기 때문에
 *  
 *  
 *  
 *  
 *  
 *  
 *	
 */
@RestController
public class DummyControllerTest {

	@Autowired  //의존성 주입(DI)
	private UserRepository userRepository;
	
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		//userRepository.deleteById(id);
		try {
			userRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			return "삭제 실패! 해당 아이디는 없습니다.";
		}
		
		return "삭제되었습니다.";
	}
	
	
	
	
	
	//update, email, password
	//json data를 받을려면 @RequestBody 어노테이션이 필요
	@Transactional //함수 종료시에 자동 commit이 됨,변경을 감지해서 변경이 있을시에는 DB에 update를 시켜줘.(더티체킹)
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id,@RequestBody User requestUser) {
		//json data를 요청했는데 spring이 자바 오브젝트로 반환해서 받아줘
		//MessageConverter의 Jackson 라이브러리가 변환해서 받아줘 >> 이때 필요한 어노테이션이 RequestBody
		
		System.out.println("id:"+id);
		System.out.println("passwd:"+requestUser.getPassword());
		System.out.println("email:"+requestUser.getEmail());
		
	
		//save 함수는 id를 전달하지 않으면 insert할때 쓰는건데, id 값을 넘겨주면 update를 해줘
		//문제는 다른 값들이 null로 변해버리는 문제가 있어.
		//그래서 update할때는 save를 쓰지 않아.
		//requestUser.setId(id);
		//userRepository.save(requestUser);
		/*
		User user = userRepository.findById(id).orElseThrow(()->{  //이때 영속화가 돼.
			return new IllegalArgumentException("수정 실패");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		userRepository.save(user);
		*/
		
		//더티체킹, @Transaction 어노테이션을 쓰면 save 함수를 안써도 update 돼...
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정 실패");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		
		return user;
	}	
	
	
	//전체 유저를 받을꺼야
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	//한 페이지당 2건에 데이터를 리턴받아 볼 예정
	/*
	@GetMapping("/dummy/user")
	public Page<User> pageList(@PageableDefault(size=2,sort="id",direction = Sort.Direction.DESC) Pageable pagealbe){
		Page<User> users =  userRepository.findAll(pagealbe);
		
		
		return users;  //page 정보가 들어가
	}
	*/
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=3,sort="id",direction = Sort.Direction.DESC) Pageable pagealbe){
		//List<User> users =  userRepository.findAll(pagealbe).getContent();
		
		Page<User> pagingUser = userRepository.findAll(pagealbe);
		
		//이런 처리를 할 수 있어 처음인지, 나중인지 제공하는 메서드들을 사용하면 돼.
		/*
		if(pagingUser.isFirst()) {
			
		}
		*/
		List<User> users = pagingUser.getContent();
		
		return users;  // user들만 들어가
	}
	
	//{id} 주소로 파라미터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id){
		//user/4을 찾으면 내가 데이터베이스에서 못찾아오게 되면 user가 null이 될것 아냐
		//그럼 return 할때 null이 리턴이 되자나.. 그럼 문제가 발생해
		//Optional로 너의 user 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해
		/*
		User user = userRepository.findById(id).orElseGet(new Supplier<User>(){
			@Override
			public User get() {

				return new User();
			}
		});
		*/
		/*
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {

			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다.id:"+id);
			}
			
		});		
		*/
		//람다식, 인터페이스는  new 할수없어, 익명객체를 선언해주면 돼
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당 사용자는 없습니다.");
		});
		
		//user 객체: 자바 오브젝트
		//변환 -> json (Gson 라이브러리)
		//스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
		//만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		//user 오브젝트를 json으로 변환해서 브라우저에게 던져줍니다.
		return user;
	}
	
	@PostMapping("/dummy/join")
	public String join(User user) { //key=value
		//id,password,email
		
		System.out.println("username:"+user.getUsername());
		System.out.println("password:"+user.getPassword());
		System.out.println("email:"+user.getEmail());
		System.out.println("id:"+user.getId());
		
		user.setRole(RoleType.USER);		
		
		userRepository.save(user);
		

		return "회원가입이 완료되었습니다.";
	}
}
