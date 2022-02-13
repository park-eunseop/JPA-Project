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
