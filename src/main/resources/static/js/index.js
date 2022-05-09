$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	// 先隐藏发布帖子的对话框
	$("#publishModal").modal("hide");

	// id选择器获取index.html中的标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求(POST)
	// 参数一: 访问的路径
	// 参数二: 要传给服务器的数据
	// 参数三: 回调函数，用来处理服务器返回过来的结果(将字符串转成JSON对象)
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function(data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后,自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);
}
