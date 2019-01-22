var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		cmsCategory: {},
		cmsShowModes:{},
		cmsModuleList:{}
	},
	methods: {
		getCategory: function(id){            
			//加载栏目树
            $.ajax({
				url: baseURL + "cms/category/list",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					ztree = $.fn.zTree.init($("#categoryTree"), setting, r);
					var node = ztree.getNodeByParam("id", vm.cmsCategory.parentId);
					if(node){
						ztree.selectNode(node);
		                vm.cmsCategory.parentName = node.name;
					}
					ztree.expandAll(true);
				}
			});            
        },
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.cmsCategory = {};
			vm.getCategory();
			vm.getCmsModule();			
		},
		update: function (event) {
			var id = getCategoryId();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";            
            vm.getInfo(id);
            vm.getCategory();
		},
		saveOrUpdate: function (event) {
			var url = vm.cmsCategory.id == null ? "cms/category/save" : "cms/category/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.cmsCategory),
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
			var ids = getCategoryId();
			if(ids == null){
				return ;
			}			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "cms/category/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			//获取详情信息
			$.ajax({
				url: baseURL + "cms/category/info/" + id,
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					vm.cmsCategory = r.cmsCategory;
				}
			});
			vm.getCmsModule();
		},
		getCmsModule:function(){
			//获取字典值模型类型列表
			$.ajax({
				url: baseURL + "sys/dict/listData?type=cms_module",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					vm.cmsModuleList = r;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			Category.table.refresh();
		},
		categoryTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择父级栏目",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#categoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();                   
                    //选择上级菜单
                    vm.cmsCategory.parentId = node[0].id;
                    vm.cmsCategory.parentName = node[0].name;
                    vm.$forceUpdate();
                    layer.close(index);                    
                }
            });
        },
        viewAll: function(){
        	window.open(baseURL + 'jiqiao/index.html');
        },
        spiderContent: function(id){
        	$("#spiderUrl").val("");
			var rowData = $('#categoryTable').bootstrapTreeTable('getSelections');
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
            			catId: id
                	};                	
                	$.ajax({
        				url: baseURL + "cms/category/spiderContent",
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
        getSpiderStatus: function(){
        	$.ajax({
				url: baseURL + "cms/category/getSpiderStatus",
				async: false,
				type: 'GET',  
				dataType: "json",
				success: function (r) {
					if(r && r.code == 0){
						if(r.tasks && r.tasks.length >0){
							var str = "";
							var tasks = r.tasks;
							for(var i=0;i<r.tasks.length;i++){
								str += "栏目【" + tasks[i].catName + "】" + tasks[i].status+"<br/>";
							}
							alert(str);
						}else{
							alert("没有执行批量采集任务");
						}						
					}else{
						alert("查询接口出现异常!");
					}										
				}
			});
        }
	}
});


var Category = {
    id: "categoryTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Category.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '栏目ID', field: 'id', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '栏目名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '280px'},
        {title: '栏目英文名称', field: 'alias', align: 'center', valign: 'middle', sortable: true, width: '120px'},
        {title: '排序', field: 'sort', align: 'center', valign: 'middle', sortable: true, width: '50px'},
        {title: '导航菜单显示', field: 'inMenu', align: 'center', valign: 'middle', sortable: true, width: '100px' ,formatter: function(item, index){
            return item.inMenu=='1'?"显示":"隐藏";
        }},
        {title: '栏目列表显示', field: 'inList', align: 'center', valign: 'middle', sortable: true, width: '100px' ,formatter: function(item, index){
            return item.inList=='1'?"显示":"隐藏";
        }},                
        {title: '操作', field: 'nofield', align: 'center', valign: 'middle',width: '200px' , formatter: function(item, index){
        	if(item.parentId!=0){
        		var str	='<a style="cursor:pointer;" href="'+ baseURL +'jiqiao/list/'+ item.id +'-1.html" target="_blank">预览</a>';
        	 	str +='&nbsp;&nbsp;<a style="cursor:pointer;" onclick="vm.spiderContent('+ item.id +')" >批量抓取栏目</a>';
        	 	return str;
        	}else{
        		return "";
        	}        	
        }}
        ]
    return columns;
};


function getCategoryId () {
    var selected = $('#categoryTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return null;
    } else {
        return selected[0].id;
    }
}


$(function () {
    var colunms = Category.initColumn();
    var table = new TreeTable(Category.id, baseURL + "cms/category/list", colunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(true);
    table.init();
    Category.table = table;
});
