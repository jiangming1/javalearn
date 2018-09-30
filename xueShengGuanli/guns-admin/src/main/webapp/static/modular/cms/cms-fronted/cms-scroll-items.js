// JavaScript Document
window.onload=function()

{     "use strict";
	  var oDiv=document.getElementById('scroll-div');
	  var oUl=oDiv.getElementsByTagName('ul')[0];
	  oUl.innerHTML=oUl.innerHTML+oUl.innerHTML;
	  var aLi=oUl.getElementsByTagName('li');
	  oUl.style.height=aLi[0].offsetHeight*aLi.length+'px';
	  setInterval(function(){
		  if(oUl.offsetTop<-oUl.offsetHeight/2)
		  {  oUl.style.top='0';     }
		oUl.style.top=oUl.offsetTop-2+'px';//在指定时间内移动的距离
		},300);//定时器运动的时间间隔
	
	
	
};