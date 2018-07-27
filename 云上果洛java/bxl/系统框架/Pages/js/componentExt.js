     var waitDialogShown = false;  
     var useTimerBeforeShowWaitDialog = true;  
     var waitDialogTimeout = 50;  
     var waitDialogTimer; 
     var fixTitleMsg="";
     var titleMsg="";
     var titleScrollSpeed = 1000; 
     var titleMsgud = " " + titleMsg;
     
     function titleScroll() { 
    	 
         if (titleMsgud.length < titleMsg.length) 
        	 titleMsgud += " - " + titleMsg;
         titleMsgud = titleMsgud.substring(1, titleMsgud.length);
         if (fixTitleMsg=='') {
        	 	document.title =titleMsgud.substring(0, titleMsg.length);
         	} else {
         		document.title = fixTitleMsg+"【"+titleMsgud.substring(0, titleMsg.length)+"】";
         	}
         window.setTimeout("titleScroll()", titleScrollSpeed); 
         } 
     
     function showWaitDialog() {  
       // avoid attempt to show it if it is already shown
        if (!waitDialogShown) {  
            Richfaces.showModalPanel('wait-dialog');  
            waitDialogShown = true;  
        }  
     }  
    
       
    
     function onRequestStart() {  
        if (useTimerBeforeShowWaitDialog) {  
            waitDialogTimer = setTimeout("showWaitDialog();", waitDialogTimeout);  
        } else {  
            showWaitDialog();  
        }  
     }  
    
     function onRequestEnd() {  
        if (waitDialogShown) {  
             Richfaces.hideModalPanel('wait-dialog');  
            waitDialogShown = false;  
         } else if (useTimerBeforeShowWaitDialog && waitDialogTimer) {  
            clearTimeout(waitDialogTimer);  
        }  
     } 

/* 组件扩展应用js支持 */
// 分割suggestionBox的ID和名称，分割符为 ^_^
function toSplit(id, inid) {
	var input = document.getElementById(id).value;
	ss = input.split("^_^");
	document.getElementById(inid).value = ss[1];
	document.getElementById(id).value = ss[0];
	return true;
}

function clearNV(id, inid) {
	document.getElementById(inid).value = '';
	document.getElementById(id).value = '';
}

// 判断隐藏域是否设置正确 如果没有设置 将选择域清空
function verifyHiddValue(showid, hiddid) {
	var hiddvalue = document.getElementById(hiddid).value;
	if (hiddvalue == '') {
		document.getElementById(showid).value = '';
	}
}
// 判断隐藏域是否设置正确 如果没有设置 不将将选择域清空 （伍佑臣，资质审批流程使用）
function verifyHiddValueNew(showid, hiddid) {
	var hiddvalue = document.getElementById(hiddid).value;
}
var fingertimer;
// 取用来比对的模板
function gFinger(fid,fmodel,btnid) {
	clearInterval(fingertimer);
	document.getElementById(fid).EndTransaction();
	document.getElementById(fid).CaptureFignerImage();
	fingertimer = setInterval(function(){sFinger(fid,fmodel,btnid);}, 500);
}
// 取用来比对的模板
function sFinger(fid,fmodel,btnid) {
    if (document.getElementById(fid).ResultID == 1) {
        var refdata;
        refdata = document.getElementById(fid).GetMatchTemplateEx();
        document.getElementById(fmodel).value = refdata;
        document.getElementById(btnid).disabled = true;
        clearInterval(fingertimer);
        return;
    }
}
// 取需要存储的模板
function getFinger(fid,fmodel,btnid) {
	clearInterval(fingertimer);
	document.getElementById(fid).EndTransaction();
	document.getElementById(fid).EnrollCount = 1;
	document.getElementById(fid).EnrollTemplateLocal();
	fingertimer = setInterval(function(){setFinger(fid,fmodel,btnid);}, 500);
}
// 取需要存储的模板
function setFinger(fid,fmodel,btnid) {
    if (document.getElementById(fid).ResultID == 2) {
        var refdata;
        refdata = document.getElementById(fid).GetReferenceTemplateEx();  
        document.getElementById(fmodel).value = refdata;
        document.getElementById(btnid).disabled = true;
        clearInterval(fingertimer);
        return;
    }
}


/** 显示指纹图片 */
function showZhiWen(id,value){
	if(document.getElementById(value).value!="" &&  document.getElementById(value).value!=null){
		document.getElementById(id).SetFPImage(document.getElementById(value).value);
	}
}

/** 重新采集 */
function initCaiJi(btnid){
	document.getElementById(btnid).disabled = false;
}
// 大指纹仪采集取用来比对的模板
function gZhiWen(fid,fmodel,btnid,vid) {
	if(document.getElementById(fid).OpenDevice(0,0,0)==1){
		if(document.getElementById(fid).LinkDevice()==1){					
			document.getElementById(fid).GenCharEx();
			document.getElementById(vid).innerText="就绪";
			fingertimer = setInterval(function(){sZhiWen(fid,fmodel,btnid,vid);}, 500);
		}
		else
			alert("连接USB指纹仪失败");
	}
	else
		alert("打开USB指纹仪失败");
	// clearInterval(fingertimer);
}
// 大指纹仪采集取用来比对的模板
function sZhiWen(fid,fmodel,btnid,vid) {
	var ens = document.getElementById(vid);
	var istatus = document.getElementById(fid).GetWorkMsg();
	switch(istatus)
	{
	case 1:	
		ens.innerText="设备未连接";
		break;
	case 2:
		ens.innerText="请按手指";
		break;
	case 3:
		ens.innerText="请抬起手指";
		break;
	case 5:
		ens.innerText="采集指纹特征点完成";
		document.getElementById(fmodel).value = document.getElementById(fid).GetCharEx();
		clearTimeout(fingertimer);
		return;
		break;
	}
	// timer=setTimeout("Transaction()",500);

}

// 大指纹仪采集存储的指纹模板
function getZhiWen(fid,fmodel,btnid,vid,imodel){
	 if (document.getElementById(fid).OpenDevice(0, 0, 0) == 1) {
         if (document.getElementById(fid).LinkDevice() == 1) {
             document.getElementById(fid).EnrFptEx();
             document.getElementById(vid).innerText = '采集就绪';
             fingertimer = setInterval(function(){setZhiWen(fid,fmodel,btnid,vid,imodel);}, 500);
         }
         else
             alert("连接USB指纹仪失败");
     }
     else
         alert("打开USB指纹仪失败");
}

// 大指纹仪采集存储的指纹模板
function setZhiWen(fid,fmodel,btnid,vid,imodel) {
	 var ens = document.getElementById(vid);
     var istatus = document.getElementById(fid).GetWorkMsg();
     switch (istatus) {
         case 1:
             ens.innerText = "设备未连接";
             break;
         case 2:
             ens.innerText = "请按手指";
             break;
         case 3:
             ens.innerText = "请抬起手指";
             break;
         case 6:
             ens.innerText = "登记指纹特征点完成";
             document.getElementById(fmodel).value = document.getElementById(fid).GetFptEx();
             document.getElementById(btnid).disabled = true;
             clearInterval(fingertimer);
             document.getElementById(imodel).value = document.getElementById(fid).GetFPImage();
             return;
             break;
     }
}


// 读取UKEY信息
function readUkeyForLogin(uName,url,sof,has,pas,rea,hid,sid){
	 var ret;
	 var status;
	 if(document.getElementById('login:userType').options[document.getElementById('login:userType').selectedIndex].value == '企业机构' || document.getElementById('login:userType').options[document.getElementById('login:userType').selectedIndex].value == '工地登录'){
		 return readUkey(uName,url,sof,has,pas,rea,hid,sid);
	 }else{return true;}		
      
}

// 读取UKEY信息
function readUkey(uName,url,sof,has,pas,rea,hid,sid){
	 var ret;
	 var status;
	 document.getElementById(sid).innerText = '正在读取UKEY信息,请稍后....';
	 document.getElementById(uName).URLSoftwareFirms = url+sof;
	 document.getElementById(uName).URLHasPassword = url+has;   
	 document.getElementById(uName).URLPasswordRandom = url+pas;
	 document.getElementById(uName).URLBuffer = url+rea;
	 ret = document.getElementById(uName).Read_Main(0,179); 
	 status = document.getElementById(uName).CurStatus;  
	 if (status !=0 ){ 
	   	 document.getElementById(sid).innerText = '';
	     alert("请插入加密锁!");
	     return false;
	 } else {
	  	 document.getElementById(sid).innerText = '';
	  	 document.getElementById(hid).value = ret;	  	
	   	 return true;
	 }
}

// 写入UKEY
function writeUkey(uName,url,sof,has,pas,rea,wri,hid,sid){
	var key= encryption()+"";
	return writeUkeyKey(uName,url,sof,has,pas,rea,wri,hid,sid,key);
}

// 写入UKEY
function writeUkeyKey(uName,url,sof,has,pas,rea,wri,hid,sid,key){
	// if(readUkey(uName,url,sof,has,pas,rea) == true){
		var ret;
		var status;		
		document.getElementById(sid).innerText = '正在读取UKEY信息,请稍后....';
		try {				
			document.getElementById(uName).URLSoftwareFirms =url+sof;
			document.getElementById(uName).URLHasPassword = url+has;      
			document.getElementById(uName).URLPasswordRandom = url+pas;            
			document.getElementById(uName).URLBuffer = url+wri;      
			document.getElementById(hid).value = key;  
		    ret = document.getElementById(uName).Write_Main(0,179,key);
		  	status = document.getElementById(uName).CurStatus;  
		    if (status !=0 ){ 
		         alert("请插入加密锁!");
		         return false;
		    } 
		  	// alert(document.getElementById(hid).value);
		  	if (!document.getElementById(uName).CurStatus == 0 ){  
		  		document.getElementById(sid).innerText ='';
		  		alert("加密锁写入失败!");
				return false;
		  	} else {
		  		document.getElementById(sid).innerText = '';
				return true;
			}	
		} catch (e) {
	  		alert("加密锁读取异常，请先安装驱动后重试！"+e.message);
			return false;
		}
	// }else{ return false;}
}

// cloneUKEY
function cloneUkey(uName,url,sof,has,pas,rea,wri,hid,sid){
	var key= document.getElementById(hid).value;
	writeUkeyKey(uName,url,sof,has,pas,rea,wri,hid,sid,key);
}

// 获取随机数
function encryption(){
	var date = new Date(); 
	var times1978 = date.getTime(); 
	var times = date.getDate() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds(); 
	var encrypt = times * times1978; 
	if(arguments.length == 1){ 
		return arguments[0] + encrypt; 
	}else{ 
		return encrypt; 
	} 
} 

function justDiagImageSize() {
    if (jQuery(window).height() -
            jQuery(".diag_graphicImage").height() < 30) {
	        jQuery(".diag_image").css('height', jQuery(window).height() - 75).css('overflow-y','auto');	        
        }
    if (jQuery(window).width() -
            jQuery(".diag_graphicImage").width() < 50) {
	        jQuery(".diag_image").css('width', jQuery(window).width() - 100).css('overflow-x','auto');	        
        }
    }

// 调整流程进度图
function justProcessDiagImageSize() {
    var img=window.document.getElementById('incpgd:processGraph_image');
    var div=window.document.getElementById('incpgd:diag_processGraph');
    if (   jQuery(window).height() - jQuery(img).height() < 30) {
	        jQuery(div).css('height', jQuery(window).height() - 75).css('overflow-y','auto');		        
        }
    if (   jQuery(window).width() -
            jQuery(img).width() < 80 ) {
	        jQuery(div).css('width', jQuery(window).width() - 100).css('overflow-x','auto');	        
        }
    }

function justProcessDiavImageSize() {
    var img=window.document.getElementById('incpgv:processGraph_image');
    var div=window.document.getElementById('incpgv:diag_processGraph');    
       
    if (jQuery(window).height() - jQuery(img).height() < 30) {    		
	        jQuery(div).css('height', jQuery(window).height() - 75).css('overflow-y','auto');
        }
    if (jQuery(window).width() -
            jQuery(img).width() < 80 ) {
	        jQuery(div).css('width', jQuery(window).width() - 100).css('overflow-x','auto');	 
        }
    }

function clearInnerHtml(elm) {
	try {
		jQuery(elm).empty();   
	} catch (e) {
		
	}
}
// 查看附件图片明细
function popOpenWin(url,ebcn,keyProp,keyValue,dtName,changeReRender,type){
	var action;
	if(type == "jpg"){
		action ='docLinksA4jImage.htm';
	}else if(type == "doc"){
		action ='docLinksA4jDoc.htm';
	}
	window.open(url+'/common/linkdocs/'+action+'?ebcn='+ebcn+'&keyProp='+keyProp+'&keyValue='+keyValue+'&dtName='+dtName+'&changeReRender='+changeReRender);
	
}
// 图片左旋转
function rotateLeft(img){
	var o = document.getElementById(img);
	if(window.attachEvent){// IE
		var currentFilter = o.currentStyle.filter;
		if (currentFilter){
			var filterMatch = currentFilter.match(/rotation=(\d)+/);
			var r = parseInt(filterMatch[1]) - 1;
			if(r < 0)
				r = 3;
			o.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + r + ')';
		}
		else{
			o.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=3)';
		}
	}
	else{// 非IE
		var currentFilter = o.style.MozTransform;
		if (currentFilter){
			var filterMatch = currentFilter.match(/rotate\(([\-]\d+)deg\)/);
 
			var r = parseInt(filterMatch[1]) - 90;
			if (r < -360)
				r = -90;
 
			o.style.MozTransform = 'rotate(' + r + 'deg)';
		}
		else{
			o.style.MozTransform = 'rotate(-90deg)';
		}
	}
 
}
 
// 图片右旋转
function rotateRight(img){
	var o = document.getElementById(img);
	if(window.attachEvent){// IE
		var currentFilter = o.currentStyle.filter;
		if (currentFilter){
			var filterMatch = currentFilter.match(/rotation=(\d)+/);
			var r = parseInt(filterMatch[1]) + 1;
			if(r > 3)
				r = 0;
			o.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + r + ')';
		}
		else{
			o.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=1)';
		}
		// document.getElementById('log').innerHTML = currentFilter;
	}
	else{// 非IE
		var currentFilter = o.style.MozTransform;
		// console.log(currentFilter);
		if (currentFilter){
			console.log(currentFilter);
			var filterMatch = currentFilter.match(/rotate\(([\-]?\d+)deg\)/);
 
			var r = parseInt(filterMatch[1]) + 90;
			if (r > 0)
				r = -270;
			o.style.MozTransform = 'rotate(' + r + 'deg)';
		}
		else{
			o.style.MozTransform = 'rotate(-270deg)';
		}
	}
 
}

// 初始化签名组件上下文
function setSignContext(ocxId,empId,empName,ecn,eid,epn) {
	try  
	  {  
	  ocx=window.document.getElementById(ocxId);
	  ocx.EmployeeId=empId;
	  ocx.EmployeeName=empName;
	  ocx.EntityClassName=ecn;
	  ocx.EntityId=eid;
	  ocx.EntityPropertyName=epn;		 			 	  
	  }  
	catch(err)  
	  {  
	  txt="设置组件属性发生错误！\n\n";
	  txt+=err.description + "\n\n";  
	  alert(txt);
	  }  
}

// 上传签名
function uploadSign(ocxId) {
	try  
	  {  
	  ocx=window.document.getElementById(ocxId);
	  ocx.sign();
	  }  
	catch(err)  
	  {  
	  txt="上传签名发生错误！\n\n";
	  txt+=err.description + "\n\n";  
	  alert(txt);
	  }  
}
// 初始化拍照组件上下文
function setImageCapContext(ocxId,organId,doctype,ecn,keyProperty,keyValue) {
	try  
	  {  
 	  ocx=window.document.getElementById(ocxId);
 	  ocx.OrganId=organId;
 	  ocx.DocType=doctype;
 	  ocx.Ecn=ecn;
 	  ocx.KeyProperty=keyProperty;
 	  ocx.KeyValue=keyValue;
 	  ocx.openVideoDevice();
	  }  
	catch(err)  
	  {  
	  txt="设置组件属性发生错误！\n\n";
	  txt+=err.description + "\n\n";  
	  alert(txt);
	  }  
 	}
// 列表单选按钮选中处理
function radioButton(radio) {      
    var id = radio.name.substring(radio.name.lastIndexOf(':'));
    var el = radio.form.elements;
    for (var i = 0; i < el.length; i++) {  
        if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {
            el[i].checked = false; 
            } 
         }
     radio.checked = true; 
     }
     
// 替换编辑值
function setValue(ida,idb){
	document.getElementById(ida).value = document.getElementById(idb).value;
}

// 值处理
function setIdb(ida,idb){
	var a = document.getElementById(ida).value;
	if(a != ''){
		document.getElementById(idb).value = document.getElementById(ida).value;
	}
}
     
// 标注地图
function markMap(contextPath,longitudeId,latitudeId,defLongitude,defLatitude){
    var arg=new Object();// 传递进去的参数
    arg.win=window;// 把当前窗口的引用当参数传进去
    // arg.arr=null;//要传进去的其他参数
                
    if(document.getElementById(longitudeId).value != null && document.getElementById(latitudeId).value != null
    && document.getElementById(longitudeId).value != '' && document.getElementById(latitudeId).value!=''
    ){
        arg.arr = new Array( document.getElementById(latitudeId).value 
                , document.getElementById(longitudeId).value
                , latitudeId
                , longitudeId);
        var ret = window.showModalDialog(contextPath+"/common/eng/map.do", arg, "dialogHeight:580px;dialogWidth:800px;status:0;help:0;scroll:1");
    }
    else{
        arg.arr = new Array( defLatitude,defLongitude,latitudeId,longitudeId);
        var ret = window.showModalDialog(contextPath+"/common/eng/map.do", arg, "dialogHeight:580px;dialogWidth:800px;status:0;help:0;scroll:1");
    }
        // document.getElementById("entityForm:long:longitude").value=ret[1];
        // window.document.getElementById("entityForm:lat:latitude").value=ret[0]
		// ;
}

// 标注地图
function showMap(contextPath,longitude,latitude,defLongitude,defLatitude){
    var arg=new Object();// 传递进去的参数
    arg.win=window;// 把当前窗口的引用当参数传进去
    // arg.arr=null;//要传进去的其他参数
                

    if(longitude != null && latitude != null
    && longitude != '' && latitude!=''
    ){
        arg.arr = new Array( latitude, longitude);
        var ret = window.showModalDialog(contextPath+"/common/eng/mapView.do", arg, "dialogHeight:580px;dialogWidth:800px;status:0;help:0;scroll:1");
    }
    else{
    	arg.arr = new Array( latitude, longitude);
        var ret = window.showModalDialog(contextPath+"/common/eng/mapView.do", arg, "dialogHeight:580px;dialogWidth:800px;status:0;help:0;scroll:1");
    }
        // document.getElementById("entityForm:long:longitude").value=ret[1];
        // window.document.getElementById("entityForm:lat:latitude").value=ret[0]
		// ;
}

function autoHeight(clazz) {
 jQuery(clazz).each( function(){
 sh=jQuery(this).attr('scrollHeight');
 if (sh<15) {
 sh=15;
 }
 jQuery(this).css('posHeight',sh+'');
 });
    }

function addAutoHeightEvent(clazz) {
 jQuery(clazz).each( function(){
 jQuery(this).unbind('blur').bind('blur', function(){ autoHeight(clazz); });
 jQuery(this).unbind('keyup').bind('keyup', function(){ autoHeight(clazz) ;});
 jQuery(this).unbind('keydown').bind('keydown', function(){ editTab(); });
 });
    }

function addInEvent() {
    jQuery('input').focus(function(){    	
    	jQuery(this).css("background-color","LightYellow");
    	jQuery(this).css("background-attachment","fixed");
    	jQuery(this).css("background-attachment","fixed");    	
    	});
    jQuery('input').blur(function(){    	
    	jQuery(this).css("background-color","#fff");
    	jQuery(this).css("background-attachment","fixed");
    	});
    jQuery('textarea').focus(function(){    	
    	jQuery(this).css("background-color","LightYellow");
    	jQuery(this).css("background-attachment","fixed");
    	});
    jQuery('textarea').blur(function(){    	
    	jQuery(this).css("background-color","#fff");
    	jQuery(this).css("background-attachment","fixed");
    	});
    

    
    }

function processAutoHeight(clazz) {
 autoHeight(clazz);
 addAutoHeightEvent(clazz);
	addInEvent();
	}
function ajaxPageInit() {
	
}

function pageInit() {
	autoHeight('.autoHeight');
    addAutoHeightEvent('.autoHeight');
    addInEvent();
    menuClick('.menuLink');
	// 禁止后退键 作用于Firefox、Opera
// document.onkeypress=banBackSpace;
	// 禁止后退键 作用于IE、Chrome
// document.onkeydown=banBackSpace;
	}



// 显示浮动Tree
function showTree(e,treeId,treeimgid){
    var tp=  jQuery('#'+treeId)[0];    
    setDivPos(e,tp,treeimgid);
    jQuery(tp).css('display','block').css('visibility','visible');
    jQuery(document).bind('mousedown.treepanel', function(e) { return _hide.call(tp,e); });                     
	}

// div 定位
function setDivPos(tt,div,treeimgid){
	var y = jQuery('#'+treeimgid).offset().top;
	var x = jQuery('#'+treeimgid).offset().left;	
	var divB = div;
	divB.style.left = x;
	divB.style.top = y;
}


// 隐藏浮动Tree
function _hide(e) {
    if (!_isChildOf(this, e.target, this)) {
    	jQuery('.treepanel').each( function(){
    		jQuery(this).css('visibility','hidden');
        	});
        jQuery(document).unbind('mousedown.treepanel');
        }
            
	}

// 判断子组件
function  _isChildOf(parentEl, el, container) {
	if (parentEl == el) {
		return true;
        }
	if (parentEl.contains) {
		return parentEl.contains(el);
        }            
	if ( parentEl.compareDocumentPosition ) {
		return !!(parentEl.compareDocumentPosition(el) & 16);
		}
	var prEl = el.parentNode;
    while(prEl && prEl != container) {
    	if (prEl == parentEl) return true;
            prEl = prEl.parentNode;
        }
    return false;
	}

function selectOne(clazz,ck) {
    jQuery(clazz).each( function(){
    	if (this!=ck)
    	   { 
    			jQuery(this).attr("checked",false);	 
    		} else
    		{
    			jQuery(this).attr("checked",true);
    		}	
 		});
    }

function setChecked(clazz, isChecked) {	
	jQuery(clazz).each(function() {
		jQuery(this).attr("checked", isChecked);
	});
}

function selectFirstOne(clazz) {
	hasChecked = false;
	// 查找是否有已选择的，如果有将后面的设置选择为false
	jQuery(clazz).each(function() {
		if (hasChecked) {
			jQuery(this).attr("checked", false);
		}
		if (jQuery(this).attr("checked") == true) {
			hasChecked = true;
		}
	});
	if (hasChecked)
		return;
	jQuery(clazz + ':first').attr("checked", true);
}

function menuClick(clazz) {
    jQuery(clazz).each( function(){
	    	jQuery(this).unbind('click').bind('click', function(){ 
	        	
	    		if (jQuery(this).find('a').attr('target')=='_blank'){
	      	      window.open( jQuery(this).find('a').attr('href'));
	      	    } else
	      	    {
	      	      location.href = jQuery(this).find('a').attr('href');
	      	    }
	    	})
	    });  
    }
KF_JSLC="流程结束后将不能恢复,是否继续操作？";
KF_GQ="是否挂起流程？";
KF_HF="是否恢复流程？";
KF_DEL="确认删除么？";
KF_ZF="确认作废么？";
KF_HFSJ="确认恢复么？";
KF_SELECT="确认选择？";
KF_RWXD="确认下达任务？";
KF_RWJS="确认接受任务？";
KF_RWTH="确认接受任务？";
KF_RWCL="确认处理任务？";

KF_QTLSBC=".qtlsbc";
// 利用对话框返回的值 （true 或者 false）
function firm(msg)  {
    	if (msg==null) {
    		_msg="是否进行操作？";
    	} else {
    		_msg=msg;
    	}
    	return confirm(_msg)
	}

// 利用对话框返回的值 （true 或者 false）
function firmH(msg)  {
    	if (msg==null) {
    		_msg="是否进行操作？";
    	} else {
    		_msg=msg;
    	}
    	return !confirm(_msg)
	}

  function over(aid,bid){
	document.getElementById(aid).className ='lfover1'; 
	document.getElementById(bid).className ='lfover2';
	}
	
	function out(aid,bid){
		document.getElementById(aid).className ='lfout1'; 
		document.getElementById(bid).className ='lfout2';
	} 

	function editTab() 
	{ 

	    var code, sel, tmp, r 
	    var tabs="" 
	    event.returnValue = false 
	    sel =event.srcElement.document.selection.createRange() 
	    r = event.srcElement.createTextRange() 

	    switch (event.keyCode) 
	    { 
	        case (8)    : 
	            if (!(sel.getClientRects().length > 1)) 
	            { 
	                event.returnValue = true 
	                return 
	            } 
	            code = sel.text 
	            tmp = sel.duplicate() 
	            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top) 
	            sel.setEndPoint("startToStart", tmp) 
	            sel.text = sel.text.replace(/^\t/gm, "") 
	            code = code.replace(/^\t/gm, "").replace(/\r\n/g, "\r") 
	            r.findText(code) 
	            r.select() 
	            break 
	        case (9)    : 
	            if (sel.getClientRects().length > 1) 
	            { 
	                code = sel.text 
	                tmp = sel.duplicate() 
	                tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top) 
	                sel.setEndPoint("startToStart", tmp) 
	                sel.text = "\t"+sel.text.replace(/\r\n/g, "\r\t") 
	                code = code.replace(/\r\n/g, "\r\t") 
	                r.findText(code) 
	                r.select() 
	            } 
	            else 
	            { 
	                sel.text = "\t" 
	                sel.select() 
	            } 
	            break 
	        case (13)    : 
	            tmp = sel.duplicate() 
	            tmp.moveToPoint(r.getBoundingClientRect().left, sel.getClientRects()[0].top) 
	            tmp.setEndPoint("endToEnd", sel) 

	            for (var i=0; tmp.text.match(/^[\t]+/g) && i<tmp.text.match(/^[\t]+/g)[0].length; i++)    tabs += "\t" 
	            sel.text = "\r\n"+tabs 
	            sel.select() 
	            break 
	        default        : 
	            event.returnValue = true 
	            break 
	    } 
	} 
	
	  function checkall(itemSize,tableid,allid,sid){
		   	 var chkallvalue = document.getElementById(tableid+':'+allid).checked;
		   	 for(var index =0; index < itemSize;index++ ){
		   		 var obj = document.getElementById(tableid+':'+index+':'+sid);
		   		 if(obj!=null){
		   			 obj.checked = chkallvalue;
		   		 }
		   	 }
	    }   
	
	function procTableEffect(tableId) {
		procOddRowEffect(tableId);
		procEvenRowEffect(tableId);
		procMouseOverRowEffect(tableId);
		procMouseOutRowEffect(tableId);
		procOutRowMark(tableId,'rowZf','zf-row');
	}  

	function procTableEffectcolorgreen(tableId) {
		procOddRowEffect(tableId);
		procEvenRowEffect(tableId);
		procMouseOverRowEffect(tableId);
		procMouseOutRowEffect(tableId);
		procOutRowMark(tableId,'jdrowZf','jdzf-row');
	}  	
	
	function procOddRowEffect(tableId) {
	       var selector = "#"+tableId+" tr:odd.rich-table-row";
	       try {
	               selector = eval("#"+tableId+" tr:odd.rich-table-row");
	       } catch (e) {}
	       jQuery(selector).addClass('odd-row');
	}

	function procEvenRowEffect(tableId) {
		       var selector = "#"+tableId+" tr:even.rich-table-row";
		       try {
		               selector = eval("#"+tableId+" tr:even.rich-table-row");
		       } catch (e) {}
		       jQuery(selector).addClass('odd-even');
		}
	function procMouseOverRowEffect(tableId) {
	       var selector = "#"+tableId+" tr:.rich-table-row";
	       try {
	               selector = eval("#"+tableId+" tr:.rich-table-row");
	       } catch (e) {}
	       jQuery(selector).mouseover(function(){jQuery(this).addClass('active-row');});
	}

	function procMouseOutRowEffect(tableId) {
	    var selector = "#"+tableId+" tr:.rich-table-row";
	    try {
	            selector = eval("#"+tableId+" tr:.rich-table-row");
	    } catch (e) {}
	    jQuery(selector).mouseout(function(){jQuery(this).removeClass('active-row');});
	}

	function procOutRowMark(tableId,colMark,styleClass) {
	    var selector = "#"+tableId+" ."+colMark;
	    try {
	            selector = eval("#"+tableId+" ."+colMark);
	    } catch (e) {}
	    jQuery(selector).parent().addClass(styleClass);
	}

	function getLodop(oOBJECT,oEMBED){
		        var strHtml1="<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop.exe'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
		        var strHtml2="<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop.exe'>执行升级</a>,升级后请重新进入。</font>";
		        var strHtml3="<br><br><font color='#FF00FF'>注意：<br>1：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它;<br>2：如果浏览器表现出停滞不动等异常，建议关闭其“plugin-container”(网上搜关闭方法)功能;</font>";
		        var LODOP=oEMBED;		
			try{		     
			     if (navigator.appVersion.indexOf("MSIE")>=0) LODOP=oOBJECT;

			     if ((LODOP==null)||(typeof(LODOP.VERSION)=="undefined")) {
				 if (navigator.userAgent.indexOf('Firefox')>=0)
		  	         document.documentElement.innerHTML=strHtml3+document.documentElement.innerHTML;
				 if (navigator.appVersion.indexOf("MSIE")>=0) document.write(strHtml1); else
				 document.documentElement.innerHTML=strHtml1+document.documentElement.innerHTML;
				 return LODOP; 
			     } else if (LODOP.VERSION<"6.0.5.6") {
				 if (navigator.appVersion.indexOf("MSIE")>=0) document.write(strHtml2); else
				 document.documentElement.innerHTML=strHtml2+document.documentElement.innerHTML; 
				 return LODOP;
			     }
			     // *****如下空白位置适合调用统一功能:*********


			     // *******************************************
			     return LODOP; 
			}catch(err){
			     document.documentElement.innerHTML="Error:"+strHtml1+document.documentElement.innerHTML;
			     return LODOP; 
			}
		}

	
// // 处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
// function banBackSpace(e){
// var ev = e || window.event;// 获取event对象
// var obj = ev.target || ev.srcElement;// 获取事件源
//	      
// var t = obj.type || obj.getAttribute('type');// 获取事件源类型
//	      
// // 获取作为判断条件的事件类型
// var vReadOnly = obj.getAttribute('readonly');
// var vEnabled = obj.getAttribute('enabled');
// // 处理null值情况
// vReadOnly = (vReadOnly == null) ? false : vReadOnly;
// vEnabled = (vEnabled == null) ? true : vEnabled;
//	      
// // 当敲Backspace键时，事件源类型为密码或单行、多行文本的，
// // 并且readonly属性为true或enabled属性为false的，则退格键失效
// var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea")
// && (vReadOnly==true || vEnabled!=true))?true:false;
//     
// // 当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
// var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t !=
// "textarea")
// ?true:false;
//	      
// // 判断
// if(flag2){
// return false;
// }
// if(flag1){
// return false;
// }
// }
	