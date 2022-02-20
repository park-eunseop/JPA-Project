/**
 *  user javascript 파일
 */
let index = {
    init: function(){
        $("#btn-save").on("click",()=>{   //function(){} , ()=>{}를 쓰면 this를 바인딩하기 위해서
            console.log("click!");
            this.save();
        });
         $("#btn-login").on("click",()=>{   //function(){} , ()=>{}를 쓰면 this를 바인딩하기 위해서
            console.log("click!");
            this.login();
        });
    },
    save: function(){
        //alert('user의 save 함수 호출됨');
        let data = {
			username: $("#username").val(),
			email: $("#email").val(),
			password: $("#password").val()
		}
		console.log(data)
		
		//ajax 호출 시 default가 비동기 호출이야
		//ajax가 통신을 성공하고 json을 리턴해주면 서버가 자동으로 자바 오브젝트로 변환해주네요
		$.ajax({
			//회원가입 수행 요청
			type:"post",
			url:"/blog/api/user",
			data: JSON.stringify(data),  //json 문자열
			contentType:"application/json;charset=utf-8",//body data가 어떤 타입인지(mime)
			dataType:"json"//요청을 서버로해서 응답이 왔을 때(생긴게 json 이라면 javascript 오브젝트로 변경)
		}).done(function(response){
			//정상
			alert("회원가입이 완료되었습니다.");
			console.log(response);
			
			location.href="/blog";
			
		}).fail(function(error){
			//실패
			alert(JSON.stringify(error))
		});//통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청할거야
		/*
		회원가입시 ajax를 사용하는 2가지 이유
		1) 요청에 대한 응답을 html이 아닌 data(json)를 받기 위해서
		2) 비동기 통신을 하기 위해서
		*/
    },
    login: function(){
        //alert('user의 save 함수 호출됨');
        let data = {
			username: $("#username").val(),
			password: $("#password").val()
		}
		console.log(data)
		
		//ajax 호출 시 default가 비동기 호출이야
		//ajax가 통신을 성공하고 json을 리턴해주면 서버가 자동으로 자바 오브젝트로 변환해주네요
		$.ajax({
			//회원가입 수행 요청
			type:"post",
			url:"/blog/api/user/login",
			data: JSON.stringify(data),  //json 문자열
			contentType:"application/json;charset=utf-8",//body data가 어떤 타입인지(mime)
			dataType:"json"//요청을 서버로해서 응답이 왔을 때(생긴게 json 이라면 javascript 오브젝트로 변경)
		}).done(function(response){
			//정상
			alert("로그인이 완료되었습니다.");			
			location.href="/blog";
			
		}).fail(function(error){
			//실패
			alert(JSON.stringify(error))
		});//통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청할거야
		/*
		회원가입시 ajax를 사용하는 2가지 이유
		1) 요청에 대한 응답을 html이 아닌 data(json)를 받기 위해서
		2) 비동기 통신을 하기 위해서
		*/
    }
    
}

index.init();
