//时间格式化
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
// 字符串以某个字符结尾
String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length) {
		return false;
	}
	if (this.substring(this.length - str.length)) {
		return true;
	} else {
		return false;
	}
	return true;
};
// 以某个字符开头
String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length) {
		return false;
	}
	if (this.substr(0, str.length) == str) {
		return true;
	} else {
		return false;
	}
	return true;
};