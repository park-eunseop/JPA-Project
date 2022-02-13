/**
 *  user javascript 파일
 */
let index = {
    init: function(){
        $("#btn-save").on("click",()=>{
            console.log("click!");
            this.save();
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
		
		$.ajax().done.fail();//통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청할거야
		/*
		회원가입시 ajax를 사용하는 2가지 이유
		1) 요청에 대한 응답을 html이 아닌 data(json)를 받기 위해서
		2) 비동기 통신을 하기 위해서
		*/
    }
    
}

index.init();
