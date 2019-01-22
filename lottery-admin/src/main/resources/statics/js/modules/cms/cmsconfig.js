$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'cms/config/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '首页标题', name: 'title', index: 'title', width: 80 }, 			
			{ label: '关键字', name: 'keywords', index: 'keywords', width: 80 }, 			
			{ label: '首页描述', name: 'description', index: 'description', width: 80 }, 			
			{ label: '生成HTML的文件夹路径', name: 'outHtmlPath', index: 'out_html_path', width: 80 }, 			
			{ label: '生成的HTML路径，相对路径', name: 'generateDir', index: 'generate_dir', width: 80 }, 			
			{ label: '创建时间', name: 'createDate', index: 'create_date', width: 80 ,formatter:function(item,index){
				 if (item) {
		            var date = new Date(item);
		            return dateFtt("yyyy-MM-dd hh:mm:ss",date);
		         }
			}}, 			
			{ label: '更新时间', name: 'updateDate', index: 'update_date', width: 80,formatter:function(item,index){
				 if (item) {
		            var date = new Date(item);
		            return dateFtt("yyyy-MM-dd hh:mm:ss",date);
		         }
			}}			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

function dateFtt(fmt,date){   
  var o = {   
    "M+" : date.getMonth()+1,                 //月份   
    "d+" : date.getDate(),                    //日   
    "h+" : date.getHours(),                   //小时   
    "m+" : date.getMinutes(),                 //分   
    "s+" : date.getSeconds(),                 //秒   
    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
    "S"  : date.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		cmsConfig: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.cmsConfig = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.cmsConfig.id == null ? "cms/config/save" : "cms/config/update";
			//非空验证
			if(!vm.cmsConfig.title){
				return alert("首页标题不能为空!");
			}
			if(!vm.cmsConfig.keywords){
				return alert("首页关键字不能为空!");
			}
			if(!vm.cmsConfig.description){
				return alert("首页描述不能为空!");
			}
			if(!vm.cmsConfig.outHtmlPath){
				return alert("输出HTML的文件夹路径不能为空!");
			}
			if(!vm.cmsConfig.generateDir){
				return alert("访问文件夹名称不能为空!");
			}		
			
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.cmsConfig),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "cms/config/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "cms/config/info/"+id, function(r){
                vm.cmsConfig = r.cmsConfig;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});