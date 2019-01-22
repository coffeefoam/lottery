package io.lottery.common.utils;

import java.util.regex.Pattern;

/**
 * HTML去除标签
 * 
 * @author R6
 *
 */
public class HtmlUtils {

	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text e.getMessage())");
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 去除HTML字串中的控制字符及不可视字符
	 * 
	 * @param str
	 *            HTML字串
	 * @return 返回的字串
	 */
	public static String escapeHTML(String str) {
		int length = str.length();
		int newLength = length;
		boolean someCharacterEscaped = false;
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case 11:
				default:
					newLength--;
					someCharacterEscaped = true;
					break;

				case '\t':
				case '\n':
				case '\f':
				case '\r':
					break;
				}
			else
				switch (c) {
				case '"':
					newLength += 5;
					someCharacterEscaped = true;
					break;

				case '&':
				case '\'':
					newLength += 4;
					someCharacterEscaped = true;
					break;

				case '<':
				case '>':
					newLength += 3;
					someCharacterEscaped = true;
					break;
				}
		}
		if (!someCharacterEscaped)
			return str;

		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case '\t':
				case '\n':
				case '\f':
				case '\r':
					sb.append(c);
					break;
				}
			else
				switch (c) {
				case '"':
					sb.append("&quot;");
					break;

				case '\'':
					sb.append("&apos;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;

				default:
					sb.append(c);
					break;
				}
		}
		return sb.toString();
	}

}
