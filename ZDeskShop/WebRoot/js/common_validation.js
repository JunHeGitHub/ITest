/**
 * 手机验证
 * @param {} num
 * @return {Boolean}
 */
function V_MobileNumberCheck(num){ 
    if(/^1[3|5|8][0-9]\d{4,8}$/.test(num)){
        return true; 
    }else{
    	return false;
    }
}
/**
 * 整数验证
 * @param {} num
 * @return {Boolean}
 */
function V_ZhengshuCheck(num){
if(/^-?\d+$/.test(num)){
		return true; 
	}else{
		return false;
	}
}
/**
 * 数字验证
 * @param {} num
 * @return {Boolean}
 */
function V_NumberCheck(num){
	if(/^[0-9]*$/.test(num)){
		return true; 
	}else{
		return false;
	}
}
/**
 * 字母加数字
 * @param {} num
 * @return {Boolean}
 * */
function V_CodeAndNumberCheck(num){
	if(/^[0-9a-zA-Z]*$/.test(num)){
		return true;
	}else{
		return false;
	}
}
/**
 * EMail地址验证
 * @param {} address
 * @return {Boolean}
 */
function V_EMailCheck(address){
	if(/^(?:\w+\.?)*\w+@(?:\w+\.)+\w+$/.test(address)){
		return true;
	}else{
		return false;
	}
}

//日期比较
function Datacompare(begindata, enddata,day) { 
 //begindata,enddata 日期格式 2013-01-01 ；
    var aDate, oDate1, oDate2, iDays;  
    if(begindata.length>10){
	    
    }
      
    aDate = begindata.split("-");   
    oDate1 = new Date(aDate[0],aDate[1]-1,aDate[2]);   
    aDate = enddata.split("-");   
    oDate2 = new Date(aDate[0],aDate[1]-1,aDate[2]);    
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24);    
    if(oDate1>oDate2){
      alert('起始日期不能大于截止时期');
      return false;
    }   
    if(iDays<=day){
	   return  true;
	} 
	alert('只能统计'+day+'天内的');
    return false;
	
}
//带时间的日期比较
function Timecompare(begindata, enddata,day) { 
 //begindata,enddata 日期格式 2013-01-01 12:20:01 ；
    var aDate, oDate1, oDate2, iDays;  
	       begindata = begindata.replace(' ', "-");
	       begindata=begindata.replace(/:/g, "-");
	       enddata=enddata.replace(' ', "-");
	       enddata=enddata.replace(/:/g, "-");
	       aDate = begindata.split("-");   
	       oDate1 = new Date(aDate[0],aDate[1]-1,aDate[2],aDate[3],aDate[4],aDate[5]);   
	       aDate = enddata.split("-");   
	       oDate2 =new Date(aDate[0],aDate[1]-1,aDate[2],aDate[3],aDate[4],aDate[5]);    
	       iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24);    
	       if(oDate1>oDate2){
	       alert('起始日期不能大于截止时期');
	       return false;
	       }   
          if(iDays<=day){
	       return  true;
	       } 
	       alert('只能统计'+day+'天内的');
           return false;
}

/**
 * 验证是否为中中，为中文返回false，否则true
 * @param {} str
 * @return {Boolean}
 */
function V_checkIsChinese(str){  
	var re = /[^\u4e00-\u9fa5]/;  
	return re.test(str); 
}
