
/* This is an example of how to cancel all the files queued up.  It's made somewhat generic.  Just pass your SWFUpload
object in to this method and it loops through cancelling the uploads. */
function cancelQueue(instance) {
	document.getElementById(instance.customSettings.cancelButtonId).disabled = true;
	instance.stopUpload();
	var stats;
	
	do {
		stats = instance.getStats();
		instance.cancelUpload();
	} while (stats.files_queued !== 0);
	
}

/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */
function fileDialogStart() {
	/* I don't need to do anything here */
}
function fileQueued(file) {
	try {
		// You might include code here that prevents the form from being submitted while the upload is in
		// progress.  Then you'll want to put code in the Queue Complete handler to "unblock" the form
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("请等待...");
		progress.toggleCancel(true, this);
	} catch (ex) {
		this.debug(ex);
	}

}

function fileQueueError(file, errorCode, message) {
	try {
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("上传文件太多.\n" + (message === 0 ? "你已经超过了上传限制." : "你最多可以" + (message > 1 ? "选择 " + message + " 个文件." : "一个文件.")));
			return;
		}

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			progress.setStatus("文件太大.");
			this.debug("错误编码: 此文件太大, 文件名: " + file.name + ", 文件大小: " + file.size + ", 消息: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			progress.setStatus("Cannot upload Zero Byte files.");
			this.debug("Error Code: Zero byte file, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			progress.setStatus("Invalid File Type.");
			this.debug("Error Code: Invalid File Type, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("你选择了太多文件.  " +  (message > 1 ? "You may only add " +  message + " more files" : "你不能再添加文件."));
			break;
		default:
			if (file !== null) {
				progress.setStatus("Unhandled Error");
			}
			this.debug("错误编码: " + errorCode + ", 文件名: " + file.name + ", 文件大小: " + file.size + ", 消息: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	
	try {
		if (this.getStats().files_queued > 0) {
			document.getElementById(this.customSettings.cancelButtonId).disabled = false;
			document.getElementById(this.customSettings.uploadButtonId).disabled = false;
		}
		
		/* I want auto start and I can do that here */
		//this.startUpload();
		
	} catch (ex)  {
        this.debug(ex);
	}
}

function tuzDemoStartUpload() {
	this.startUpload();
}

function uploadStart(file) {
	try {
		/* I don't want to do any file validation or anything,  I'll just update the UI and return true to indicate that the upload should start */
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("正在上传,请等候...");
		progress.toggleCancel(true, this);
	}
	catch (ex) {
	}
	
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {

	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setProgress(percent);
		progress.setStatus("正在上传,请等候...");
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setComplete();
		progress.setStatus("上传成功.");
		progress.toggleCancel(false);
		uploadImagePreview(serverData);
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	try {
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued === 0) {
			document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			document.getElementById(this.customSettings.uploadButtonId).disabled = true;
		} else {	
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}

}

function uploadError(file, errorCode, message) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);
		
		if(message==500){
			message = "请联系管理员！";
		}
		if(message==404){
			message = "图片找不到！";
		}
		if(message==999){
			message = "一个商品最多只能有10张图片！";
		}
		
		if(message==800){
			message = "图片像素至少为800*800！";
		}
		
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("上传错误: " + message);
			this.debug("错误编码: HTTP 错误, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			progress.setStatus("配置错误");
			this.debug("错误编码: No backend file, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("上传失败.");
			this.debug("错误编码: 上传失败, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("服务器 (IO) 错误");
			this.debug("错误编码: IO 错误, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("安全错误");
			this.debug("错误编码: 安全错误, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			progress.setStatus("Upload limit exceeded.");
			this.debug("错误编码: Upload Limit Exceeded, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
			progress.setStatus("文件没有找到.");
			this.debug("错误编码: 这个文件没有找到, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			progress.setStatus("验证失败.  跳过上传.");
			this.debug("错误编码: 文件验证失败, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
				document.getElementById(this.customSettings.uploadButtonId).disabled = true;
			}
			progress.setStatus("取消");
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("停止");
			break;
		default:
			progress.setStatus("Unhandled Error: " + error_code);
			this.debug("错误编码: " + errorCode + ", 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}