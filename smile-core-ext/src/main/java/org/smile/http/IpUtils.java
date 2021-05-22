package org.smile.http;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.smile.commons.Strings;
import org.smile.io.BufferedReader;
import org.smile.log.LoggerHandler;

public class IpUtils implements LoggerHandler{
	
	/**本地ip*/
	public static String getLocalIp() {
		String ip = Strings.BLANK;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("获取本地ip异常",e);
		}
		return ip;
	}
	/**客户端ip*/
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		return ip;
	}

	/**
	 * 判断是否ip地址
	 * @param ipAddress
	 * @return
	 */
	public static boolean isIpAdress(String ipAddress) {
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}
	/***
	 * 网页内容
	 * @param urlStr
	 * @return
	 */
	public static String getUrlContent(String urlStr,String charset) {
		StringBuffer add = new StringBuffer();
		try {
			String city = null;
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(conn.getInputStream(),charset);
			while ((city = reader.readLine()) != null) {
				add.append(city);
			}
			reader.close();
		} catch (Exception e) {
			logger.error("connection ulr "+urlStr +" error charset :"+charset,e);
		}
		return add.toString();
	}
	/**
	 * 调用第三方接口，根据IP地址获取城市信息，如果第三方接口不可用了请更改此方法
	 * http://whois.pconline.com.cn/ip.jsp?ip= ipAdress
	 * @param ipAdress
	 * @return
	 */
	public static String getCityByIpName(String ipAdress){
		String city = getUrlContent("http://whois.pconline.com.cn/ip.jsp?ip="+ipAdress,"GBK");
		String nul[] = city.split("市");
		if (nul.length > 1)
			city = nul[0];
		String all[] = city.split("省");
		if (all.length > 1)
			city = all[1];
		if (city.equals("局域网对方和您在同一内部网"))
			city = "内网";
		return city;
	}

}
