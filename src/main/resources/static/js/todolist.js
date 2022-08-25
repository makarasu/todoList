/**
 * 
 */
 $(function(){
	$("#order").change(function(){
		let token = $("#token").val();
		let bool = false;
		var order = $("option:selected").val();
		location.href = "http://localhost:8080/todoList/order?token=" + encodeURIComponent(token) + "&order=" + encodeURI(order) + "&bool=" + encodeURIComponent(bool);
	})
})
	
	function enforcement(){	
		let hostUrl = "http://localhost:8080/todoList/done";
		let token = $("#token").val();
		let enforcement = $("input:checked").val();
		let bool = false;
		var order = $("option:selected").val();
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
			location.href = "http://localhost:8080/todoList/order?token=" + encodeURIComponent(newToken) + "&order=" + encodeURI(order) + "&boolean=" + encodeURIComponent(bool);
		}).fail(function(XMLHttpRequest, textStatus, errorThrown){
			console.log("XMLHttpRequest" + XMLHttpRequest);
			console.log("textStatus" + textStatus);
			console.log("errorThrown" + errorThrown);
		});
	}