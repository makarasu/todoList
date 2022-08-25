/**
 * 
 */
  $(function(){
	$("#logOrder").change(function(){
		let token = $("#token").val();
		let bool = true;
		var order = $("option:selected").val();
		location.href = "http://localhost:8080/todoList/order?token=" + encodeURIComponent(token) + "&order=" + encodeURI(order) + "&bool=" + encodeURIComponent(bool);
	})
})