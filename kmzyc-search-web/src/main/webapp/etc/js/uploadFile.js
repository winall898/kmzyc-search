var commonFileArray = new Array();
//图片上传回调函数
// 0 id , 1 相对地址  ,2 类型 , 3 大小 , 4 名称 ， 转变后名称
function callbackUploadImage(fileArray,rootPath,relativePath,isDate,fileFlag,isMultiple,specPath){
	var _hidden = document.getElementById("hidden_"+fileFlag).value;
    var _show = document.getElementById("show_"+fileFlag).innerHTML;
	var _iframe = '<iframe  marginheight=0 marginwidth=0 scrolling=no  width=340 height=25 name=aa frameborder=0 src="/sys/gotoSysUploadImage.action?fileFlag='+fileFlag+'&rootPath='+rootPath+'&relativePath='+relativePath+'&isDate='+isDate+'&isMultiple='+isMultiple+'&specPath='+specPath+'"></iframe>';
	if(isMultiple!=null&&isMultiple!=""&&isMultiple=="1"){//多张图片
		if(fileArray!=null&&fileArray.length>0){
			for(var i = 0 ; i < fileArray.length ; i ++ ){
				if(_hidden==null||_hidden==""){
					_hidden = fileArray[i][0];
				}else{
					_hidden = _hidden + ","+fileArray[i][0];
				}
				if(_show==null||_show==""){
					_show = '<img src="'+fileArray[i][1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+fileFlag+'\','+fileArray[i][0]+')">删除</a>';
				}else{
					_show = _show + '<img src="'+fileArray[i][1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+fileFlag+'\','+fileArray[i][0]+')">删除</a>';
				}
				commonFileArray.push(fileArray[i]);
			}

		}
	}else{
		_hidden = "";
		_show = "";
		if(fileArray!=null&&fileArray.length>0){
			for(var i = 0 ; i < fileArray.length ; i ++ ){
				if(_hidden==null||_hidden==""){
					_hidden = fileArray[i][0];
				}else{
					_hidden = _hidden + ","+fileArray[i][0];
				}
				if(_show==null||_show==""){
					_show = '<img src="'+fileArray[i][1]+'" width="200">';
				}else{
					_show = _show + '<br><img src="'+fileArray[i][1]+'" width="200">';
				}
			}

		}

	}
	document.getElementById("hidden_"+fileFlag).value = _hidden;
	document.getElementById("show_"+fileFlag).innerHTML = _show;
	document.getElementById("iframe_"+fileFlag).innerHTML = _iframe;
}


//文件上传回调函数
// 0 id , 1 相对地址  ,2 类型 , 3 大小 , 4 名称 ， 转变后名称
function callbackUploadFile(fileArray,rootPath,relativePath,isDate,fileFlag,isMultiple,specPath){
	var _hidden = document.getElementById("hidden_"+fileFlag).value;
    var _show = document.getElementById("show_"+fileFlag).innerHTML;
	var _iframe = '<iframe  marginheight=0 marginwidth=0 scrolling=no  width=340 height=25 name=aa frameborder=0 src="/sys/gotoSysUploadFile.action?fileFlag='+fileFlag+'&rootPath='+rootPath+'&relativePath='+relativePath+'&isDate='+isDate+'&isMultiple='+isMultiple+'&specPath='+specPath+'"></iframe>';
	if(isMultiple!=null&&isMultiple!=""&&isMultiple=="1"){//
		if(fileArray!=null&&fileArray.length>0){
			for(var i = 0 ; i < fileArray.length ; i ++ ){
				if(_hidden==null||_hidden==""){
					_hidden = fileArray[i][0];
				}else{
					_hidden = _hidden + ","+fileArray[i][0];
				}
				if(_show==null||_show==""){
					_show = '<img src="/etc/images/foldericon.gif" >'+fileArray[i][4]+'('+fileArray[i][2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+fileFlag+'\','+fileArray[i][0]+')">删除</a>';
				}else{
					_show = _show + '<br><img src="/etc/images/foldericon.gif" >'+fileArray[i][4]+'('+fileArray[i][2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+fileFlag+'\','+fileArray[i][0]+')">删除</a>';
				}
				commonFileArray.push(fileArray[i]);
			}

		}
	}else{
		_hidden = "";
		_show = "";
		if(fileArray!=null&&fileArray.length>0){
			for(var i = 0 ; i < fileArray.length ; i ++ ){
				if(_hidden==null||_hidden==""){
					_hidden = fileArray[i][0];
				}else{
					_hidden = _hidden + ","+fileArray[i][0];
				}
				if(_show==null||_show==""){
					_show = '<img src="/etc/images/foldericon.gif" >'+fileArray[i][4]+'('+fileArray[i][2]+')';
				}else{
					_show = _show + '<br><img src="/etc/images/foldericon.gif" >'+fileArray[i][4]+'('+fileArray[i][2]+')';
				}
			}

		}
	}
	document.getElementById("hidden_"+fileFlag).value = _hidden;
	document.getElementById("show_"+fileFlag).innerHTML = _show;
	document.getElementById("iframe_"+fileFlag).innerHTML = _iframe;
}

//多附件的删除
function deleteThisFile(fi,flag,id){
	if(!confirm("您确定要删除该附件吗？")){
		return;
	}
	var _hidden = "";
    var _show = "";
	var thisFileIdStr = document.getElementById("hidden_"+flag).value;
	if(thisFileIdStr==null||thisFileIdStr==""){
		return;
	}
	var thisFileIdArray = new Array();
	thisFileIdArray = thisFileIdStr.split(",");
	if(thisFileIdArray!=null&&thisFileIdArray.length>0){
		for(var i = 0 ; i < thisFileIdArray.length ; i++){
			if(id == thisFileIdArray[i]){
				continue;
			}else{
				if(fi==1){//文件
					if(commonFileArray!=null&&commonFileArray.length>0){
						for(var j = 0 ; j < commonFileArray.length ; j++ ){
							if(thisFileIdArray[i] == commonFileArray[j][0]){
								if(_hidden==null||_hidden==""){
									_hidden = commonFileArray[j][0];
								}else{
									_hidden = _hidden + ","+commonFileArray[j][0];
								}
								if(_show==null||_show==""){
									_show = '<img src="/etc/images/foldericon.gif" >'+commonFileArray[j][4]+'('+commonFileArray[j][2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+flag+'\','+commonFileArray[j][0]+')">删除</a>';
								}else{
									_show = _show + '<br><img src="/etc/images/foldericon.gif" >'+commonFileArray[j][4]+'('+commonFileArray[j][2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+flag+'\','+commonFileArray[j][0]+')">删除</a>';
								}
							}
						}
					}
				}else if(fi==0){//图片
					if(commonFileArray!=null&&commonFileArray.length>0){
						for(var j = 0 ; j < commonFileArray.length ; j++ ){
							if(thisFileIdArray[i] == commonFileArray[j][0]){
								if(_hidden==null||_hidden==""){
									_hidden = commonFileArray[j][0];
								}else{
									_hidden = _hidden + ","+commonFileArray[j][0];
								}
								if(_show==null||_show==""){
									_show = '<img src="'+commonFileArray[j][1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+flag+'\','+commonFileArray[j][0]+')">删除</a>';
								}else{
									_show = _show + '<img src="'+commonFileArray[j][1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+flag+'\','+commonFileArray[j][0]+')">删除</a>';
								}
							}
						}
					}
				}
			}
		}
	}
	document.getElementById("hidden_"+flag).value = _hidden;
	document.getElementById("show_"+flag).innerHTML = _show;
	
}



//修改页面初始函数
function buildUploadedFileInfo(fileList,flag,fi){
	var _hidden = "";
    var _show = "";
    if(fileList!=null&&fileList.length>0){
    	for(var i = 0 ; i < fileList.length ; i ++){
    		var fileDetail = fileList[i];
    		commonFileArray.push(fileDetail);
    		if(fi=="file"){
    			if(_hidden==null||_hidden==""){
    				_hidden = fileDetail[0];
    			}else{
    				_hidden = _hidden + ","+fileDetail[0];
    			}
    			if(_show==null||_show==""){
    				_show = '<img src="/etc/images/foldericon.gif" >'+fileDetail[4]+'('+fileDetail[2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+flag+'\','+fileDetail[0]+')">删除</a>';
    			}else{
    				_show = _show + '<br><img src="/etc/images/foldericon.gif" >'+fileDetail[4]+'('+fileDetail[2]+')&nbsp;&nbsp;<a href="javascript:deleteThisFile(1,\''+flag+'\','+fileDetail[0]+')">删除</a>';
    			}
    		}else if(fi=="image"){
    			if(_hidden==null||_hidden==""){
    				_hidden = fileDetail[0];
    			}else{
    				_hidden = _hidden + ","+fileDetail[0];
    			}
    			if(_show==null||_show==""){
    				_show = '<img src="'+fileDetail[1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+flag+'\','+fileDetail[0]+')">删除</a>';
    			}else{
    				_show = _show + '<img src="'+fileDetail[1]+'" width="100">&nbsp;&nbsp;<a href="javascript:deleteThisFile(0,\''+flag+'\','+fileDetail[0]+')">删除</a>';
    			}
    		}
    	}
    }
	document.getElementById("hidden_"+flag).value = _hidden;
	document.getElementById("show_"+flag).innerHTML = _show;
}


//删除数组中的某一个下标值
function delArrayById(array, idx){
	if(isNaN(idx)||idx>array.length){
    	return array;
    }
    if(array.length==1){
    	array = new Array();
    }else{
    	for(var i=0,n=0;i<array.length;i++){ 
	        if(array[i]!=array[idx]){ 
	            array[n++]=array[i] ;
	        } 
	    } 
	    array.length-=1 ;
    }
    return array;
} 