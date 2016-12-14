
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

<title>Activiti Test</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

</head>
<body>
	<div class="page-header">
		<h2>activiti集成spring测试</h2>
	</div>
	
	<p>
		<button type="button" class="btn btn-lg btn-primary" id="btnDeployMent">deployment</button>
		<button type="button" class="btn btn-lg btn-primary" id="btnDeployMentWithZIP">deployMentWithZIP</button>
		<button type="button" class="btn btn-lg btn-default" id="binStart">启动流程</button>
		<button type="button" class="btn btn-lg btn-default" id="findTask">findTask</button>
		<button type="button" class="btn btn-lg btn-default" id="completeTask">completeTask</button>
	</p>
  <p>
		<button type="button" class="btn btn-lg btn-success" id="findProcessDefinition">findProcessDefinition</button>
		<button type="button" class="btn btn-lg btn-success" id="findLastProcessDefinition">findLastProcessDefinition</button>
		<button type="button" class="btn btn-lg btn-warning" id="deleteProcessDefinition">deleteProcessDefinition</button>
		<button type="button" class="btn btn-lg btn-warning" id="deleteProcessDefinitionByKey">deleteProcessDefinitionByKey</button>
</p>

	<p>
		<button type="button" class="btn btn-lg btn-default" id="queryProcessImage">queryProcessImage</button>
		<button type="button" class="btn btn-lg btn-default" id="isProcessEnd">isProcessEnd</button>
		<button type="button" class="btn btn-lg btn-default" id="findHistoryTask">findHistoryTask</button>
		<button type="button" class="btn btn-lg btn-default" id="findHistoryProcessInstance">findHistoryProcessInstance</button>
	</p>

	<p>
	          <h2>ExclusiveGateWay Test </h2>
	<!-- ExclusiveGateWay Test -->
		<button type="button" class="btn btn-lg btn-success" id="deployment">deployment</button>
		<button type="button" class="btn btn-lg btn-success" id="startProcessInstance">startProcessInstance</button>
		<button type="button" class="btn btn-lg btn-success" id="getPersionalTaskMessage">getPersionalTaskMessage</button>
		<button type="button" class="btn btn-lg btn-success" id="completeMyPersonalTask">completeMyPersonalTask</button>
		
		<!-- <button type="button" class="btn btn-lg btn-default" id="findHistoryTask">findHistoryTask</button>
		<button type="button" class="btn btn-lg btn-default" id="findHistoryProcessInstance">findHistoryProcessInstance</button> -->
	</p>


<h2>PARALLEL GATEWAY TEST</h2>
<p>
		<button type="button" class="btn btn-lg btn-warning" id="parallelGatewayDeploymentProcess">parallelGatewayDeploymentProcess</button>
		<button type="button" class="btn btn-lg btn-warning" id="parallelGatewayStartProcessInstance">parallelGatewayStartProcessInstance</button>
		<button type="button" class="btn btn-lg btn-warning" id="parallelGatewayFindMyPersonalTask">parallelGatewayFindMyPersonalTask</button>
		<button type="button" class="btn btn-lg btn-warning" id="parallelGatewayCompleteMyTask">parallelGatewayCompleteMyTask</button>


</p>




<h2>SHOW PROCESS</h2>
<p>
		<button type="button" class="btn btn-lg btn-warning" id="showProcess">showProcess</button>
<div id ="showProcess"><image id ="processImage" alt=''></div>

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
 
  
  
 var baseUrl='<%=basePath%>/';
<%--  var baseUrl='<%=basePath%>rest/'; --%>

 //初始化按钮
 function init(){
		$("#btnDeployMent").click(function(){DeployMent();});
		$("#btnDeployMentWithZIP").click(function(){DeployMentWithZIP();});
		$("#binStart").click(function(){startProcess();});
		$("#findTask").click(function(){findTask();});
		$("#completeTask").click(function(){completeTask();});
		$("#findProcessDefinition").click(function(){findProcessDefinition();});
		$("#findLastProcessDefinition").click(function(){findLastProcessDefinition();});
		$("#deleteProcessDefinition").click(function(){deleteProcessDefinition();});
		$("#deleteProcessDefinitionByKey").click(function(){deleteProcessDefinitionByKey();});
		$("#queryProcessImage").click(function(){queryProcessImage();});
		$("#isProcessEnd").click(function(){isProcessEnd();});
		$("#findHistoryTask").click(function(){findHistoryTask();});
		$("#findHistoryProcessInstance").click(function(){findHistoryProcessInstance();});
	 
		
		
		/* EclusiveGateWay Test */
		
		$("#deployment").click(function(){deployment();});
		$("#startProcessInstance").click(function(){startProcessInstance();});
		$("#getPersionalTaskMessage").click(function(){getPersionalTaskMessage();});
		$("#completeMyPersonalTask").click(function(){completeMyPersonalTask();});
		
		
		/* parallelGateWayTest */
		
		$("#parallelGatewayDeploymentProcess").click(function(){parallelGatewayDeploymentProcess();});
		$("#parallelGatewayStartProcessInstance").click(function(){parallelGatewayStartProcessInstance();});
		$("#parallelGatewayFindMyPersonalTask").click(function(){parallelGatewayFindMyPersonalTask();});
		$("#parallelGatewayCompleteMyTask").click(function(){parallelGatewayCompleteMyTask();});
		
		
		
		
		/* showProcess */
		$("#showProcess").click(function(){showProcess();});
		
		
		/* 
		$("#btnDel").click(function(){delPerson()});
		$("#btnUpdate").click(function(){updatePerson()});
		$("#btnList").click(function(){listPerson()});
 */  }
 
  /* processDeployMent*/
 function DeployMent (){
	  
		$.ajax({
			url: baseUrl + 'ac_deployMent',
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

  /* processDeployMentWithZIP*/
 function DeployMentWithZIP (){
	  
		$.ajax({
			url: baseUrl + 'ac_deployMentWithZIP',
			type: 'GET',
			dataType: 'json',
			success:function(data){
				alert("deployMentWithZIP  OK");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
  /*启动流程*/
 function startProcess (){
	 var person={id:1,name:'张三',sex:'男',age:23};
	  
	  $.ajax({
			url: baseUrl + 'ac_startProcess',
			type: 'POST',
			dataType: 'json',
			/* data: person, */
			success:function(data){
				alert("OK");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
  
  
  
 /*findTask*/
 function findTask (){
		$.ajax({
			url: baseUrl + 'ac_findTask',
			type: 'DELETE',
			dataType: 'json',
			success:function(data){
				alert("findTask  ok ");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
 /* completeTask */
 function completeTask (){
    var  person = {id:221,name:'王五',sex:'男',age:23};
		
		$.ajax({
			url: baseUrl + 'ac_completeTask',
			type: 'put',//注意在传参数时，加：_method:'PUT'　将对应后台的PUT请求方法
			dataType: 'json',
			/* data: person, */
			success:function(data){
				alert("completeTask ok ");
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
 
 
 
 
 /*findProcessDefinition*/
 function findProcessDefinition(){
		$.ajax({
			url: baseUrl + 'ac_findProcessDefinition',
			type: 'patch',
			dataType: 'json',
			/* data: {name:'张三'}, */
			success:function(){
				
				alert("findProcessDefinition  ok");
			
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 
 
 
 /*findLastProcessDefinition*/
 function findLastProcessDefinition(){
		$.ajax({
			url: baseUrl + 'ac_findLastProcessDefinition',
			type: 'patch',
			dataType: 'json',
			/* data: {name:'张三'}, */
			success:function(){
				
				alert("findLastProcessDefinition  ok");
			
			},
			error:function(data){
				alert("error");
			}
			
		})//end ajax
	} 

 /* deleteProcessDefinition */
 function deleteProcessDefinition(){
	 
	 $.ajax({
		url : baseUrl + 'ac_deleteProcessDefinition',
		type: 'POST',
		success:function(data){
			alert("deleteProcessDefinition  ok");
		},
		error:function(){
			alert("deleteProcessDefinition  error");
		}
	 });
			 
 }
 
 /* deleteProcessDefinitionByKey */
 function deleteProcessDefinitionByKey(){
	 $.ajax({
		 url :baseUrl + 'ac_deleteProcessDefinitionByKey' ,
		 type: 'POST',
		 success :function(data){ alert("deleteProcessDefinitionByKey ok");},
		 error: function(){alert("deleteProcessDefinitionByKey error");}
	 });
 }
 
 /* queryProcessImagemyProcess */
 function queryProcessImage(){
	 $.ajax({
		 url: baseUrl+'ac_queryProcessImage',
		 type :'POST',
		 success :function(data){alert("queryProcessImgae ok");},
		 errror :function(){alert("queryProcessImage error");}
	 });
 }
 
  /* isProcessEnd */
  function isProcessEnd(){
	  $.ajax({
		  url:baseUrl+'ac_isProcessEnd',
		  type :'POST',
		  suceess: function(data){alert("isProcessEnd ok");},
		  error: function(){alert("isProcessEnd erro");}
		  
	  });
	  
	  
	  
	  
  }
  
  /* findHistoryTask */
  function findHistoryTask(){
	  $.ajax({
		  url:baseUrl+'ac_findHistoryTask',
		  type :'POST',
		  success:function(data){alter("findHistoryTask ok");},
		  error:function(){alter("findHistoryTask error");}
	  });
  }
  
  
  
  /* findHistoryProcessInstance */
  function findHistoryProcessInstance(){
	  $.ajax({
		  url:baseUrl+'ac_findHistoryProcessInstance',
		  type : 'POST',
		  success:function(data){alert("findHistoryProcessInstance ok");},
		  error:function(){alert("findHistoryProcessInstance error ");}
	  });
	  
  }//end function
  
  
  /* ExclusiveGateWay Test */
  
  
  /* deployment */
  function deployment(){
	  $.ajax({
		  url:baseUrl+'ac_ExclusiveGateWayDeployment',
		  type :'POST',
		  success:function(data){alert("ExclusiveGateWayDeployment  ok");},
		  error:function(){alert("ExclusiveGateWayDeployment  error");}
	  });
  }//end function
  
  
  /* startProcessInstance */
  function startProcessInstance(){
	  $.ajax({
		  url:baseUrl+'ac_ExclusiveGateWayStartProcessInstance',
		  type :'POST',
		  success:function(data){alert("ExclusiveGateWayStartProcessInstance  ok");},
		  error:function(){alert("ExclusiveGateWayStartProcessInstance  error");}
	  });
  }//end function
  
  
  
  /* get persionalTaskMessage */
  function getPersionalTaskMessage(){
	  $.ajax({
		  url:baseUrl+'ac_getPersionalTaskMessage',
		  type:'POST',
		  success:function(data){alert("getPersionalTaskMessage..... ok");},
		  error:function(){alert("getPersionalTaskMessage..... error");}
		  
		  
	  });
	  
	  
	  
	  
  }//end function
  
  
  /* completeMyPersonalTask */
  function completeMyPersonalTask(){
	  $.ajax({
		  url:baseUrl+'ac_completeMyPersonalTask',
		  type:'POST',
		  success :function(data){alert("completeMyPersonalTask ......ok");},
		  error :function(){alert("completeMyPersonalTask ......error");}
	  });
	  
	  
	  
  }//end function
  
  
  
 
  
  
  
 /* parallelGetway */

 /* deloymentProcess */
 
function parallelGatewayDeploymentProcess(){
 $.ajax({
	 url:baseUrl+'ac_parallelGatewayDeploymentProcess',
	 type:'POST',
	 success:function (data){alert("parallelGateway.....ok");},
	 error:function (){alert("parallelGateway.....error");}
 });	  
	  
	  
	  
  }//end function
 
 
  /* parallelGatewayStartProcessInstance */
  function parallelGatewayStartProcessInstance(){
	  $.ajax({
		  url:baseUrl+'ac_parallelGatewayStartProcessInstance',
		  type:'POST',
		  success:function(data){alert("parallelGatewayStartProcessInstance .....ok");},
		  error:function(data){alert("parallelGatewayStartProcessInstance .....error");},
		  
		  
		  
	  });
	  
	  
	  
  }//end function
  
  
  /* parallelGatewayFindMyPersonalTask */
  function parallelGatewayFindMyPersonalTask(){
	  $.ajax({
		  url:baseUrl+'ac_parallelGatewayFindMyPersonalTask',
		  type:'POST',
		  success:function(data){alert("parallelGatewayFindMyPersonalTask...ok");},
		  error:function(){alert("parallelGatewayFindMyPersonalTask...error");}
		  
		  
	  });
	  
	  
	  
	  
	  
  }//end function
  
  /* parallelGatewayCompleteMyTask */
  function parallelGatewayCompleteMyTask(){
	  $.ajax({
		  url:baseUrl+'ac_parallelGatewayCompleteMyTask',
		  type :'POST',
		  success:function(data){alert("parallelGatewayCompleteMyTask...ok");},
		  error:function(){alert("parallelGatewayCompleteMyTask...error");}
		  
	  });
  }//end function

  
  
  /* showProcess */
  function showProcess(){

	  $("#processImage").attr('src',baseUrl+'ac_showProcess');
	  
	  
	  
	  /* 	  
	  $.ajax({
		 url:baseUrl+'ac_showProcess',
         type :'POST',
         dataType:'json',
         success :function(data){
        	  alert("showProcess.....ok filePath ="+data);
        	  
         },
         error :function(data){alert("showProcess.....error");}
		  
	  });
 */	  
	  
  }//end function
  
  
 </script>
</body>
</html>