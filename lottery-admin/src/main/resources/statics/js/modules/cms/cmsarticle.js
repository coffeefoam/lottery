var ztree;
var ztree2;
$(function () {
	vm.getCategory();
	ztree.expandAll(true); 
    $("#jqGrid").jqGrid({
        url: baseURL + 'cms/article/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: 'id', width: 50, key: true },
			{ label: '标题', name: 'title', index: 'title', width: 300 }, 
			{ label: '栏目名称', name: 'categoryName', index: 'category_name', width: 150 },			
			{ label: '关键字', name: 'keywords', index: 'keywords', width: 180 }, 			
			{ label: '点击数', name: 'hits', index: 'hits', width: 60 }, 
			{ label: '操作', name: 'id', index: 'id', width: 100 ,formatter: function(item,index){
	        	var str = '<a style="cursor:pointer;" href="'+ baseURL +'jiqiao/article/'+ item +'.html" target="_blank">预览</a>';
	            return str;
			}},
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
    },
    callback: {
        beforeClick: function(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("tree");            
            var searchUrl = "";
            var categoryId = treeNode.id;
            var postJson = {
            	categoryId : categoryId
            };
			//提交post并刷新表格
			$("#jqGrid").jqGrid("setGridParam",{postData:postJson,page:1,search:true}).trigger("reloadGrid");
        }
    }
};

var setting2 = {
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
	      title: null
	    },
		showList: true,
		title: null,
		cmsArticle: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.cmsArticle = {};		
			vm.cmsArticle.articleData = {};
			vm.getCategory2();
			
			//清空编辑器数据
            var ue = UE.getEditor('editor');
			ue.ready(function() {//编辑器初始化完成再赋值  
	            ue.setContent("");  //赋值给UEditor  
	        });
			$("#cmsArticleArticleDataCopyfrom").val("");
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id);
            vm.getCategory2(id);
		},
		saveOrUpdate: function (event) {
			var url = vm.cmsArticle.id == null ? "cms/article/save" : "cms/article/update";
			//获取文章大的值
			var ue = UE.getEditor('editor');
			if(ue.hasContents()){
				var contents = ue.getContent();
				//对内容进行编码加密
				contents = encodeURIComponent(contents);
				contents = encodeURIComponent(contents);
				vm.cmsArticle.articleData.content = contents;				
			}else{
				alert("请输入文章内容");
			}
			//获取文章来源的值
			var copyfrom = $("#cmsArticleArticleDataCopyfrom").val();
			vm.cmsArticle.articleData.copyfrom = copyfrom;
			
			console.info(JSON.stringify(vm.cmsArticle));
			
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.cmsArticle),
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
				    url: baseURL + "cms/article/delete",
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
			 $.ajax({
				url: baseURL + "cms/article/info/" + id,
				async: true,
				type: 'GET',  
				dataType: "json",
				success: function (r) {
					vm.cmsArticle = r.cmsArticle;
	                $("#articleDataContent").val(r.cmsArticle.articleData.content);
					//初始化编辑器
	                var ue = UE.getEditor('editor');
					ue.ready(function() {//编辑器初始化完成再赋值  
			            ue.setContent(r.cmsArticle.articleData.content);  //赋值给UEditor  
			        }); 
					//初始化值
					$("#cmsArticleArticleDataCopyfrom").val(r.cmsArticle.articleData.copyfrom);
				}
			});			
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData:{'title': vm.q.title}
            }).trigger("reloadGrid");
		},
		getCategory: function(id){            
			//加载栏目树
            $.ajax({
				url: baseURL + "cms/category/list",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					ztree = $.fn.zTree.init($("#categoryTree"), setting, r);					
				}
			});            
        },
        getCategory2: function(id){            
			//加载栏目树
            $.ajax({
				url: baseURL + "cms/category/list",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					ztree2 = $.fn.zTree.init($("#categoryTree2"), setting2, r);
					if(id){
						var node = ztree2.getNodeByParam("id", id);
						if(node){
							ztree2.selectNode(node);
			                vm.cmsArticle.categoryName = node.name;
						}
					}
					ztree2.expandAll(true);
				}
			});            
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
                    var node = ztree2.getSelectedNodes();                   
                    //选择上级菜单
                    vm.cmsArticle.categoryId = node[0].id;
                    vm.cmsArticle.categoryName = node[0].name;
                    vm.$forceUpdate();
                    layer.close(index);                    
                }
            });
        },
        batchHtml: function(){
        	$.ajax({
				url: baseURL + "cms/article/batchHtml",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					if(r && r.code == 0){
						alert(r.msg);
					}										
				}
			});        	
        },
        spiderContent: function(){
        	$.ajax({
				url: baseURL + "cms/article/spiderContent",
				async: false,
				type: 'POST',  
				dataType: "json",
				success: function (r) {
					if(r && r.code == 0){
						alert("爬取内容成功");
					}										
				}
			});        	
        },
        getStatus: function(){
        	$.ajax({
				url: baseURL + "cms/article/getBatchHtmlStatus",
				async: false,
				type: 'GET',  
				dataType: "json",
				success: function (r) {
					if(r && r.code == 0){
						if(r.status){
							alert("当前生成HTML进度：" + r.status + "，已生成：" + r.currentNum + "，总数：" + r.total);
						}else{
							alert("没有执行批量生成任务");
						}						
					}else{
						alert("查询接口出现异常!");
					}										
				}
			});        	
        }
	}
});