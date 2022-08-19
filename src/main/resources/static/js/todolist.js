/**
 * 
 */
 $(function(){
	})
	function enforcement(){	
		let hostUrl = "http://localhost:8080/todoList/done";
		let token = $("#token").val();
		let enforcement = $("input:checked").val();	
		$.ajax({
			url: hostUrl,
			type:"post",
			dataType:"json",
			data:{
				token: token,
				enforcement: enforcement
			},
			async:true
		}).done(function(data){
			let newToken = data.token;
			location.href = "http://localhost:8080/todoList/list?token=" + encodeURIComponent(newToken);
		}).fail(function(XMLHttpRequest, textStatus, errorThrown){
			console.log("XMLHttpRequest" + XMLHttpRequest);
			console.log("textStatus" + textStatus);
			console.log("errorThrown" + errorThrown);
		});
	}