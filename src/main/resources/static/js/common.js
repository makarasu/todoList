/**
 * 
 */
 $(function(){
	$(document).ready(() => {
		$("#submit").prop("disabled", true);
	})
	$(document).on("keyup", "#checkPassword", function(){
		if($("#password").val() == $("#checkPassword").val()){
			$("#noMatchPassword").remove();
			$("#submit").removeAttr("disabled");
		} else{
			$("#noMatchPassword").text("パスワードと確認用パスワードが一致しません");
			$("#submit").prop("disabled", true);
		}
	})	
})
