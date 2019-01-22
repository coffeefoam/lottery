$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL ,
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: 'id', width: 50, key: true },
			{ label: '生成内容', name: 'content', index: 'content', width: 80 }, 			
			{ label: '创建时间', name: 'createtime', index: 'createtime', width: 80 }			
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
        },
        ondblClickRow:function(id){
        	var rowData = $("#jqGrid").jqGrid('getRowData',id);
        	alert(rowData.content);
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		opFeedback: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		rebuild: function(){
			var date = $("#datestr").val();
			if(!date){
				alert("请先选择日期");
				return;
			}
			var url = "scheduledManager/rebuild";			
			url += "?date=" + date;
			
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    success: function(r){
			    	alert(r.msg);
				}
			});
		},	
		getDataBydate: function(){
			var date = $("#datestr").val();
			if(!date){
				alert("请先选择日期");
				return;
			}
			var url = "scheduledManager/getDataBydate";			
			url += "?date=" + date;
			
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    success: function(r){
			    	alert(r.msg);
				}
			});
		},
				
	}
});
