$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'lottery/liuhe/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '搅珠日期', name: 'time', index: 'time', width: 80 ,
				formatter:function(item){
					if(item){
						return formatDate(new Date(item),"yyyy-MM-dd");
					}else{
						return "";
					}					
				}				
			}, 			
			{ label: '期数', name: 'periods', index: 'periods', width: 80 }, 			
			{ label: '平码1', name: 'num1', index: 'num1', width: 80 }, 			
			{ label: '平码2', name: 'num2', index: 'num2', width: 80 }, 			
			{ label: '平码3', name: 'num3', index: 'num3', width: 80 }, 			
			{ label: '平码4', name: 'num4', index: 'num4', width: 80 }, 			
			{ label: '平码5', name: 'num5', index: 'num5', width: 80 }, 			
			{ label: '平码6', name: 'num6', index: 'num6', width: 80 }, 			
			{ label: '特码', name: 'num7', index: 'num7', width: 80 }, 			
			{ label: '创建时间', name: 'createtime', index: 'createtime', width: 80 ,
				formatter:function(item){
					if(item){
						return formatDate(new Date(item),"yyyy-MM-dd");
					}else{
						return "";
					}					
				}
			}, 			
			{ label: '加工时间', name: 'processtime', index: 'processtime', width: 80,
				formatter:function(item){
					if(item){
						return formatDate(new Date(item),"yyyy-MM-dd");
					}else{
						return "";
					}					
				}
			}, 			
			{ label: '开奖时间', name: 'starttime', index: 'starttime', width: 80,
				formatter:function(item){
					if(item){
						return formatDate(new Date(item),"yyyy-MM-dd");
					}else{
						return "";
					}					
				}
			}			
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		liuhe: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.liuhe = {};
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
			var url = vm.liuhe.id == null ? "lottery/liuhe/save" : "lottery/liuhe/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.liuhe),
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
				    url: baseURL + "lottery/liuhe/delete",
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
			$.get(baseURL + "lottery/liuhe/info/"+id, function(r){
                vm.liuhe = r.liuhe;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		caiji: function (event) {
			$("#spiderUrl").val("");
//			var rowData = $('#categoryTable').bootstrapTreeTable('getSelections');
			layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "填写要采集文章列表页",
                area: ['500px', '250px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#spiderContent-div"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                	var url = $("#spiderUrl").val();
                	if(!url){
                		return alert("请输入栏目地址!");
                	}
                	var params = {
            			url: url,
                	};                	
                	
                	$.ajax({
        				url: baseURL + "lottery/liuhe/caiji",
        				async: true,
        				type: 'POST',  
        				dataType: "json",
        				data: params,
        				success: function (r) {
        					if(r){
        						alert(r.msg);
        						layer.close(index);
        					}        					
        				}
        			});
                }
            });			
		},
		genKjjl: function(){
			$.ajax({
				url: baseURL + "lottery/liuhe/genKjjl",
				async: true,
				type: 'GET',  
				dataType: "json",
				success: function (r) {
					if(r){
						alert(r.msg);
						layer.close(index);
					}        					
				}
			});
		}
	}
});
function formatDate(date, fmt) {
	var o = {
		"M+" : date.getMonth() + 1, // 月份
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
		"S" : date.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}