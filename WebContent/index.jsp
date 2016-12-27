<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	/* 			+ path + "/"; */
%>
<!DOCTYPE html>
<html lang="zh-cn">
<base href="<%=basePath%>">

<title>请假审批流程测试</title>
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
		<h2>请假审批流程测试</h2>
	</div>
	<br>

	<div class="container">
		<div class="row">
			<p>
				<button type="button" id="deployMent" class="btn btn-success">部署流程(仅执行一次)</button>
			</p>
		</div>

		<div class="row">
		<div class="col-sm-4">
		     <p> 流程审批描述：</p>
		       <textarea rows="10" cols="20" id ="processDescribe"  disabled="disabled" readonly="readonly"></textarea>
		</div>
		</div>
		
		<div class="row">
		
		<div class="col-sm-8">
				<div class="form">
					<p>
					<h2>填写请假单</h2>
					</p>
					<p>
						姓名 ：<input type="text" name="name" id="name">
					</p>
					<p>
						请假天数 ：<input type="text" name="day" id="day">
					</p>
 					<p>
						请假原因 ：
					</p>
						<textarea rows="5" cols="10"></textarea>
					<p>
						<button type="button" class="btn btn-success" id="submit">提交</button>
					</p>
				</div>


				<!-- 展示流程进度 -->
				<div id="ProcessImage"></div>

				<!-- 展示审批人 -->
				<div id="shenPi"></div>

				<!-- <button type="button" onclick="showProcess()">查看流程图</button> -->
			</div>
			<!-- end col-sm-4 -->
		</div>
		<!-- end row -->

	</div>
	<!-- end container -->




	<!-- script文件 -->

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script
		src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>


<script type="text/javascript" async="async">

        var baseUrl = '<%=basePath%>/';
		var processInstanceId ="";
		var shenPiRen = "";
		var key = "handleLeaveProcess";
		var taskId="";
		var ProcessDescrible ="开始  -->";
		
		

		$(function() {
			$('#processDescribe').text("开始 -->");
			init();
		});//end function

		/* 初始化函数 */
		function init() {
			/* 部署流程 */
			deployMent();
		
			submit();
		}//end function

		/* 部署流程函数 */
		function deployMent() {
			$("#deployMent").click(function() {
				$.ajax({
					url : baseUrl + 'deployMent',
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						alert("deployMent ok key =" + data.key);

						key = data.key;
						
						$("#deployMent").hide();
					},
					error : function() {
						alert("deployMent error");
					}
				});//end ajax

			});//end click

		}//end function

		function submit() {

			$("#submit").click(function() {
				$.ajax({
					url : baseUrl + 'startProcessAndEndFirstTask',
					type : 'POST',
					dataType : 'json',
					data : {
						name : $("#name").val(),
						day : $("#day").val(),
						key :key
					},
					success : function(data) {
						//alert("ok processInstanceId = "+data.processInstanceId);
						processInstanceId = data.processInstanceId;
						
						/* 更新流程描述区域 */
						/* updateProcessDescrible(data.assigine); */
						
						//隐藏表单
						$("div[class='form']").hide();

						/*显示审批人信息 */
						showHandlePeople();
						
						/* 添加流程图元素并显示流程进度*/
						addProcessImgElement();
						
						
						/* 更新流程描述 */
						/* updateProcessDescrible($('#name').val()); */

					},
					error : function() {
						alert("error");
					}

				});//end ajax
			});//end click
		}//end submit	

		/* 更新流程描述区域 */
		function updateProcessDescrible(assigine){
           
			if('null'==assigine){
				ProcessDescrible +="结束 ";
           }else{
				ProcessDescrible +=assigine +" --> ";
           }
			
			$('#processDescribe').text(ProcessDescrible);
			
		}//end function
		
		
		
		/*显示审批人信息 */
		function showHandlePeople() {
			
			//alert("addActiv processInstanceId ="+processInstanceId);	
  			$.ajax({
  				url:baseUrl+'showHandlePeople',
  				type:'POST',
  				dataType:'json',
  				data:{processInstanceId : processInstanceId},
  				success:function(data){
  					/* alert("showHandlePeople ok assiginee ="+data.assiginee+" taskId ="+data.taskId); */
   					//设置全局变量的值			
  					shenPiRen = data.assiginee;
   					
   				    taskId = data.taskId;

   				  /* 添加审批按钮元素 */
   				   addHandleButton();			    
   	
					
					/* 更新流程描述 */
					 updateProcessDescrible(shenPiRen);  

   				  
   				  
   				  /* 为按钮添加点击事件 */
	  			  $("#handleButton").click(function(){
					
	  				  /* 结束当前任务 */
					  compeletTask();
	  				  
	  			  });//end click

	  			  
	  			  
	  			  
	  			  
	  			  
  				},
  				error:function(){alert("showHandlePeople error");}
  				
  			});//end ajax
  			
	}//end function
	
	//添加审批按钮

	function addHandleButton() {
		var str;

		if ('系统' == shenPiRen || '员工wbliu' == shenPiRen) {
			str = '<p id ="radioGroup">'
					+ shenPiRen
					+ ' 处理'
					+ '<input   id ="handleButton"   type ="button" class="btn btn-success" value = "提交" /></p>';

		}else if('null' == shenPiRen){
		
			str = '<p id ="radioGroup">'
					+ '<h2>流程处理结束！</h2>'
					+ '</p>';
			
					
					/* 隐藏进度图显示 */
					$('#ProcessImage').hide();
			
		}else {
			str = '<p id ="radioGroup">'
					+ shenPiRen
					+ ' 审批意见:'
					+ '<label class="checkbox-inline">'
					+ '<input type="radio" name="optionsRadiosinline" id="optionsRadios3" value="0" checked>同意'
					+ '</label>'
					+ '<label class="checkbox-inline">'
					+ '<input type="radio" name="optionsRadiosinline" id="optionsRadios4" value="-1">不同意'
					+ '</label>'
					+ '<input   id ="handleButton"   type ="button" class="btn btn-success" value = "提交" /></p>';
		}

		
		$("#shenPi").html(str);
	}//end function

	/* 结束当前任务 */
	function compeletTask() {
		/* 获得单选的内容 */

		$.ajax({
			url : baseUrl + 'completeTask',
			type : 'POST',
			dataType : 'json',
			data : {
			    taskId : taskId,
				processInstanceId : processInstanceId,
				radio : $('p input[name ="optionsRadiosinline"]:checked').val(),
				day  : $("#day").val()
			},
			success : function(data) {
				//显示当前任务的审批信息		
				showHandlePeople();

				//设置src的值
				changeImgSrc();
			},
			error : function() {
				alert("completeTask ok");
			}
		});//end ajax

	}//end function

	/*添加流程图元素*/
	function addProcessImgElement() {
		//$("#ProcessImage").empty();
		var imgTemp = "<img alt ='流程进度图' id='imgProcess' >";
		//add img element into html element
		$("#ProcessImage").html(imgTemp);

		//设置src的值    
		changeImgSrc();
	}//end fuction

	/* 设置img的src属性 */
	function changeImgSrc() {
	
		if('null'==shenPiRen){
	          return ;			
		}
		
		var src = baseUrl + "showProcess?processInstanceId="
				+ processInstanceId + "&&num=" + Math.random();
		$('#imgProcess').attr("src", src);
		
	
	}//end function
</script>
</body>

</html>