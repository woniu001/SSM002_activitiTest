
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<base href="<%=basePath%>">

<title>spring+mybatis Test</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet"
	href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

</head>
<body>
	<div class="page-header">
		<h2>BOOTSTRAP</h2>
	</div>
	<div class="page-header">
		<h1>Buttons</h1>
	</div>

	<p>
		<button type="button" class="btn btn-lg btn-default" id="btnGet">获取人员GET</button>
		<button type="button" class="btn btn-lg btn-primary" id="btnAdd">添加人员POST</button>
		<button type="button" class="btn btn-lg btn-success" id="btnUpdate">更新人员PUT</button>
		<button type="button" class="btn btn-lg btn-info" id="btnDel">删除人员DELETE</button>
		<button type="button" class="btn btn-lg btn-warning" id="btnList">查询列表PATCH</button>
		<!-- <button type="button" class="btn btn-lg btn-danger">Danger</button>
        <button type="button" class="btn btn-lg btn-link">Link</button> -->
	</p>

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script
		src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">
  $(function(){
	  init();
  });//end jquery
 
  
  
 var baseUrl='<%=basePath%>rest/';

 //初始化按钮
 function init(){
		$("#btnGet").click(function(){getPerson();});
	 	$("#btnAdd").click(function(){addPerson();});
		$("#btnDel").click(function(){delPerson()});
		$("#btnUpdate").click(function(){updatePerson()});
		$("#btnList").click(function(){listPerson()});
  }
 
  /* 查询 */
 function getPerson (){
	  
		$.ajax({
			url: baseUrl + 'person/101/',
			type: 'GET',
			dataType: 'json',
			success:function(data){
				alert("查询 get "+data.name+"  "+data.id);
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
  /* 添加 */
 function addPerson (){
	 var person={id:1,name:'张三',sex:'男',age:23};
	  
	  $.ajax({
			url: baseUrl + 'person',
			type: 'POST',
			dataType: 'json',
			data: person,
			success:function(data){
				alert("添加  post "+data.msg+"  ");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
  
 /* 删除 */
 function delPerson (){
		$.ajax({
			url: baseUrl + 'person/109',
			type: 'DELETE',
			dataType: 'json',
			success:function(data){
				alert("删除  delete  "+data.msg+"  ");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 

 /* 更新 */
 function updatePerson (){
    var  person = {id:221,name:'王五',sex:'男',age:23};
		
		$.ajax({
			url: baseUrl + 'person',
			type: 'put',//注意在传参数时，加：_method:'PUT'　将对应后台的PUT请求方法
			dataType: 'json',
			data: person,
			success:function(data){
				alert("全属性更新  update  "+data.msg+"  ");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
 /* 部分属性更新 */
 function listPerson (){
		$.ajax({
			url: baseUrl + 'person',
			type: 'patch',
			dataType: 'json',
			data: {name:'张三'},
			success:function(data){
				var names = "";
				$.each(data,function(index,element){
					names += element.name+" ";
				});
				
				alert("部分属性更新  patch  "+names);
			
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 

 </script>
</body>
</html>