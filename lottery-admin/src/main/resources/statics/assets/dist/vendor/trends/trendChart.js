function TrendChart(t){this.options=t||{css:{noyl:"noyl",bline:"bline",lines:[".firL"]},lineWidth:2,lineColor:"#BB8569",ckbDistribute:"#chkYL",ckbSplit:"#chkBZX",chkZX:"#chkZX"},this.canvasStr="",this.canvas=null,this.isIE=navigator.userAgent.indexOf("MSIE"),this.lineColor={".firL":"#BB8569",".secL":"#836C90",".thiL":"#BB8569",".fouL":"#836C90",".fifL":"#BB8569",".sixL":"#836C90",".redBall":"#BB8569",".blueBall":"#836C90",".attr1":"#957EA2",".attr3":"#957EA2",".attr5":"#957EA2",".attr7":"#957EA2",".attr9":"#957EA2",".attr2":"#BB8569",".attr4":"#BB8569",".attr6":"#BB8569",".attr8":"#BB8569",".attr10":"#BB8569"}}$.cookie=function(t,e,i,s){var n,r,o,a,h,l,c,p,f,u,d,g,v,b,y;if("undefined"==typeof i){if(f=null,document.cookie&&""!=document.cookie)for(u=document.cookie.split(";"),d=0;d<u.length;d++)if(g=jQuery.trim(u[d]),g.substring(0,t.length+1)==t+"="){if(f=decodeURIComponent(g.substring(t.length+1)),"undefined"!=typeof e&&null!=e&&""!=e)for(v=f.toString().split("&"),b=0;b<v.length;b++){if(y=jQuery.trim(v[b]),y.substring(0,e.length+1)==e+"="){f=decodeURIComponent(y.substring(e.length+1));break}f=void 0}break}return f}if(s=s||{},null===i&&(i="",s.expires=-1),n="",s.expires&&("number"==typeof s.expires||s.expires.toUTCString)&&("number"==typeof s.expires?(r=new Date,r.setTime(r.getTime()+1e3*s.expires)):r=s.expires,n="; expires="+r.toUTCString()),o=s.path?"; path="+s.path:";path=/",a=s.domain?"; domain="+s.domain:"",h=s.secure?"; secure":"","object"==typeof i){l=0,c="";for(p in i)l>0&&(c+="&"),c+=p+"="+encodeURIComponent(i[p]),l++;i=c}else i=encodeURIComponent(i);document.cookie=[t,"=",i,n,o,a,h].join("")},Date.prototype.pattern=function(t){var e={"M+":this.getMonth()+1,"d+":this.getDate(),"h+":this.getHours()%12==0?12:this.getHours()%12,"H+":this.getHours(),"m+":this.getMinutes(),"s+":this.getSeconds(),"q+":Math.floor((this.getMonth()+3)/3),S:this.getMilliseconds()},i={0:"/u65e5",1:"/u4e00",2:"/u4e8c",3:"/u4e09",4:"/u56db",5:"/u4e94",6:"/u516d"};/(y+)/.test(t)&&(t=t.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length))),/(E+)/.test(t)&&(t=t.replace(RegExp.$1,(RegExp.$1.length>1?RegExp.$1.length>2?"/u661f/u671f":"/u5468":"")+i[this.getDay()+""]));for(var s in e)new RegExp("("+s+")").test(t)&&(t=t.replace(RegExp.$1,1==RegExp.$1.length?e[s]:("00"+e[s]).substr((""+e[s]).length)));return t},TrendChart.prototype.reDraw=function(){this.distributeDisplay(),this.splitDisplay(),this.lineDisplay()},TrendChart.prototype.distributeDisplay=function(){this.options.ckbDistribute&&($(this.options.ckbDistribute).hasClass("icp-checked")?$("#chartTable").removeClass(this.options.css.noyl):$("#chartTable").addClass(this.options.css.noyl))},TrendChart.prototype.splitDisplay=function(){if(this.options.ckbSplit){this.clearLines();var t=$("#chart tr").length,e=$("#chart tr").filter(function(e){return e>5&&(e-7)%5==0&&e!=t-1});$(this.options.ckbSplit).hasClass("icp-checked")&&e.after('<tr class="bline"> </tr>'),this.lineDisplay()}},TrendChart.prototype.lineDisplay=function(){!this.options.chkZX||$(this.options.chkZX).hasClass("icp-checked")?$("#canvas").length>0?$("#canvas").show():this.drawLine(this.options.css.lines,this.options.lineWidth,this.options.lineColor):$("#canvas").length>0&&$("#canvas").hide()},TrendChart.prototype.drawLine=function(t,e,i){if(t.length){var s=0,n=0,r=0,o=0;this_=this,$("#trendChart").append("<div id='canvas'>"),this.canvas=$("#canvas");for(var a=0;a<t.length;a++){var h=$(t[a]);$("#chart "+t[a]).each(function(t){t>0&&(lst=h.eq(t),fst=h.eq(t-1),o=lst.offset().top+lst.height()/2,r=lst.offset().left+lst.width()/2,n=fst.offset().top+fst.height()/2,s=fst.offset().left+fst.width()/2,this_.line(s,n,r,o,e,i))})}this.isIE>-1&&(this.canvas.html(this.canvasStr),this.canvasStr="")}},TrendChart.prototype.line=function(t,e,i,s,n,r){var o=$("#trendChart").offset();if(t-=o.left,e-=o.top,i-=o.left,s-=o.top,this.isIE>-1){var a=(i-t)/(s-e),h=a>1?1.8*(a-1)/a:0,l=5.2+h;t<i?(t+=l,e+=l/a,i-=l,s-=l/a):t>i?(t-=l,e-=l/a,i+=l,s+=l/a):(t=t,e+=7,i=i,s-=7);try{this.canvasStr+="<v:line strokecolor="+r+" from='"+t+"px,"+e+"px' to='"+i+"px,"+s+"px' strokeweight='"+n+"px' style='left:0; top:0; position:absolute;'/>"}catch(c){}}else{var p=t,f=e;i<t&&(p=i),s<e&&(f=s);var u=Math.abs(t-i),d=Math.abs(e-s);0==u&&(u=n),0==d&&(d=n);var g=this.createCanvas(),v=g.getContext("2d");$(g).attr("class","cvs"),$(g).css("left",p+"px"),$(g).css("top",f+"px"),$(g).attr("width",u),$(g).attr("height",d),v.strokeStyle=r,v.lineWidth=n,v.beginPath();var b=0,y=0,C=0,k=0,a=(i-t)/(s-e),h=a>1?1.5*(a-1)/a:0,l=5.5+h;t<i?(b=l,y=l/a,C=u-l,k=d-l/a):t>i?(b=u-l,y=-l/a,C=l,k=d+l/a):(b=0,y=7,C=0,k=d-7),v.moveTo(b,y),v.lineTo(C,k),v.stroke()}},TrendChart.prototype.createCanvas=function(){var t=document.createElement("canvas");return this.canvas.append(t),t},TrendChart.prototype.clearLines=function(){$("#canvas").remove(),$("#chart tr.bline").remove()};