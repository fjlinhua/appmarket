<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>应用市场</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/easyui/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript">
	    function doSearch() {
	    	$('#appTable').datagrid('load',{
	    		appKey: $("#appKey").combobox("getValue"),
	    		appPlatform: $("#appPlatform").combobox("getValue"),
	    		appEnv: $("#appEnv").combobox("getValue")
	        });
	    }

	    function doRemove() {
	    	var ids = [];
            var rows = $('#appTable').datagrid('getChecked');
            for(var i=0; i < rows.length; i++){
                ids.push(rows[i].APP_ID);
            }
            if(ids.length == 0) {
            	$.messager.alert('错误信息', '请选择待删除的版本', 'error');
                return;
            }
            $.messager.confirm('Confirm','确定要删除已选中的版本?',function(r){
                if (r){
		            $('#appIds').val(ids);
		            $('#deleteForm').form('submit',{
		            	url: '<%=request.getContextPath() %>/deleteAppVersion.html',
		                success: function(result){
		                    var result = eval('('+result+')');
		                    if (result.errcode == 0){
		                    	$('#deleteForm').form('clear');
		                    	$('#appTable').datagrid('reload');
		                    } else {
		                    	$.messager.alert('错误信息',result.errormsg,'error');
		                    }
		                }
		            }); 
                }
            });
		}
	    
	    function doClear() {
	    	$('#appKey').combobox('setValue', null);
	    	$('#appPlatform').combobox('setValue', null);
	    	$('#appEnv').combobox('setValue', null);
		}
	    
	    function submitForm() {
	    	$('#dataForm').form('submit',{
	    		onSubmit:function(){
                    return $(this).form('enableValidation').form('validate');
                },
                url: '<%=request.getContextPath() %>/uploadApp.html',
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.errcode == 0){
                        $('#appTable').datagrid('reload');
                        $('#dataForm').form('clear');
                        $('#addNewVersion').window('close');
                    } else {
                    	$.messager.alert('错误信息',result.errormsg,'error');
                    }
                }
            }); 
		}
	</script>
</head>

<body>
    <table id="appTable" class="easyui-datagrid" title="APP版本列表" style="height:770px;"
        data-options="loadMsg:'数据加载中,请稍后...',rownumbers:true,singleSelect:false,url:'<%=request.getContextPath() %>/listApp.html',method:'get',toolbar:'#tb'">
        <thead>
            <tr>
                <th data-options="field:'APP_ID',checkbox:true"></th>
                <th data-options="field:'APP_NAME',width:100,align:'center'">APP名称</th>
                <th data-options="field:'APP_PLATFORM',width:100,align:'center'">APP平台</th>
                <th data-options="field:'RELEASE_NAME',width:100,align:'center'">版本类型</th>
                <th data-options="field:'VERSION_NUM',width:100,align:'center'">版本号</th>
                <th data-options="field:'RELEASE_NOTE',width:500,align:'left'">版本更新信息</th>
                <th data-options="field:'APP_SIZE',width:100,align:'center'">文件大小</th>
                <th data-options="field:'FILE_NAME',width:150,align:'center'">文件名</th>
                <th data-options="field:'CREATE_TIME',width:130,align:'center'">创建时间</th>
            </tr>
        </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$('#addNewVersion').window('open')">上传新版本</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="doRemove();">删除版本</a>
        </div>
        <div>
            APP名称: 
            <select id="appKey" class="easyui-combobox" style="width:180px" data-options="
                    url:'<%=request.getContextPath() %>/queryAppKeySelector.html',
                    method:'get',
                    valueField:'APP_KEY',
                    textField:'APP_NAME',
                    panelHeight:'auto'
            ">
            </select>
            &nbsp;&nbsp;
            APP平台: 
            <select id="appPlatform" class="easyui-combobox" panelHeight="auto" style="width:100px">
                <option value="Android">Android</option>
                <option value="IOS">IOS</option>
            </select>
            &nbsp;&nbsp;
                               版本类型: 
            <select id="appEnv" class="easyui-combobox" style="width:100px" data-options="
                    url:'<%=request.getContextPath() %>/queryAppReleaseSelector.html',
                    method:'get',
                    valueField:'RELEASE_KEY',
                    textField:'RELEASE_NAME',
                    panelHeight:'auto'
            ">
            </select>
            &nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch();">查询</a>
            &nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-clear" onclick="doClear();">重置</a>
        </div>
    </div>
    <form id="deleteForm" method="post"><input type="hidden" id="appIds" name="appIds" value="" /></form>
    <div id="addNewVersion" class="easyui-window" title="上传新版本" data-options="modal:true,closed:true,iconCls:'icon-save'" style="width:442px;height:445px;padding:10px;">
        <div class="easyui-panel" title="版本信息" style="width:100%;height:100%">
	        <div style="padding:10px 60px 20px 60px">
	            <form id="dataForm" class="easyui-form" method="post" enctype="multipart/form-data" data-options="novalidate:true">
	                <table cellpadding="5">
	                    <tr>
	                        <td>APP名称：</td>
	                        <td>
	                            <select id="formAppKey" name="appKey" class="easyui-combobox" style="width:180px" data-options="
					                    url:'<%=request.getContextPath() %>/queryAppKeySelector.html',
					                    method:'get',
					                    valueField:'APP_KEY',
					                    textField:'APP_NAME',
					                    panelHeight:'auto',
					                    required:true
					            ">
	                            </select>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>APP平台：</td>
	                        <td>
	                            <select name="appPlatform" class="easyui-combobox" panelHeight="auto" data-options="required:true" style="width:180px">
					                <option value="Android">Android</option>
					                <option value="IOS">IOS</option>
					            </select>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>版本类型：</td>
	                        <td>
					            <select id="formAppEnv" name="appEnv" class="easyui-combobox" style="width:180px" data-options="
					                    url:'<%=request.getContextPath() %>/queryAppReleaseSelector.html',
					                    method:'get',
					                    valueField:'RELEASE_KEY',
					                    textField:'RELEASE_NAME',
					                    panelHeight:'auto',
					                    required:true
					            ">
					            </select>                        
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>版本号：</td>
	                        <td><input class="easyui-textbox" type="text" name="versionNum" data-options="required:true" style="width:180px"></input></td>
	                    </tr>
	                    <tr>
	                        <td>文件上传：</td>
	                        <td>
	                           <input class="easyui-filebox" name="app" data-options="prompt:'浏览...', required:true" style="width:180px">
	                           <input type="hidden" name="appFileName" />
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>版本说明：</td>
	                        <td><input class="easyui-textbox" type="text" name="releaseNote" data-options="multiline:true,required:true" style="width:180px;height:80px"></input></td>
	                    </tr>
	                </table>
	            </form>
	            <div style="text-align:center;padding:5px">
	                <a href="#" class="easyui-linkbutton" onclick="submitForm();">提交</a>
	                <a href="#" class="easyui-linkbutton" onclick="$('#dataForm').form('clear');">清除</a>
	            </div>
	        </div>
        </div>
    </div>    
</body>
</html>