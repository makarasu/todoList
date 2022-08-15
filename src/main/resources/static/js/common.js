/**
 * 
 */
 $(function(){
	$(document).ready(() => {
		$("#button").prop("disabled", true);
	})
	$(document).on("keyup", "#checkPassword", function(){
		if($("#password").val() == $("#checkPassword").val()){
			$("#noMatchPassword").remove();
			$("#button").removeAttr("disabled");
		} else{
			$("#noMatchPassword").text("パスワードと確認用パスワードが一致しません");
			$("#button").prop("disabled", true);
		}
	})	
})
