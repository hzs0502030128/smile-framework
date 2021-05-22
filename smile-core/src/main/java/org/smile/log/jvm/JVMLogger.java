package org.smile.log.jvm;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Map;

import org.smile.Smile;
import org.smile.commons.StringBand;
import org.smile.commons.Strings;
import org.smile.io.IOUtils;
import org.smile.json.JSONFormatter;
import org.smile.json.JSONObject;
import org.smile.log.LoggerHandler;
import org.smile.util.DateUtils;

/**
 * JVM中的日志
 * @author 胡真山
 */
public class JVMLogger implements LoggerHandler {
	/***
	 * jvm中的堆栈信息
	 * @return
	 */
	public static String dumpInfo() {
		long now = System.currentTimeMillis();
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] in = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), Integer.MAX_VALUE);
		String st = System.getProperty("line.separator");
		StringBuilder infos = new StringBuilder(1000);
		infos.append(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "  JVM堆栈信息：");
		String processId = new JVMInfoMonitor().processId();
		infos.append("进程ID=").append(processId).append(st);
		for (ThreadInfo info : in) {
			StackTraceElement[] el = info.getStackTrace();
			infos.append("线程名称：" + info.getThreadId() + "-" + info.getThreadName()).append(st).append(info.getThreadState()).append(st);
			for (StackTraceElement e : el) {
				infos.append(Strings.TAB).append( e.getClassName()).append(Strings.DOT).append(e.getMethodName()).append("(").append(e.getFileName()).append(Strings.DOT_JAVA).append(Strings.SPACE).append(e.getLineNumber()).append(")").append(st);
			}
		}
		infos.append("dump线程完成，累计消耗时间:" + (System.currentTimeMillis() - now) + "毫秒");
		return infos.toString();
	}

	/**
	 * 获取线程信息
	 * @return
	 */
	public static String getThreadInfo() {
		Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
		String st = System.getProperty("line.separator");
		StringBand infos = new StringBand(30);
		long now = System.currentTimeMillis();
		infos.append(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "  JVM线程信息：");
		String processId = new JVMInfoMonitor().processId();
		infos.append("进程ID=").append(processId).append(st);
		for (Map.Entry<Thread, StackTraceElement[]> entry : threads.entrySet()) {
			StackTraceElement[] el = entry.getValue();
			Thread info = entry.getKey();
			infos.append("线程名称：" + info.getId() + "-" + info.getName()).append(st).append(info.getState()).append(st);
			for (StackTraceElement e : el) {
				infos.append(Strings.TAB).append( e.getClassName()).append(Strings.DOT).append(e.getMethodName()).append("(").append(e.getFileName()).append(Strings.DOT_JAVA).append(Strings.SPACE).append(e.getLineNumber()).append(")").append(st);
			}
		}
		infos.append("dump线程完成，累计消耗时间:" + (System.currentTimeMillis() - now) + "毫秒");
		return infos.toString();
	}

	/***
	 * 获取jvm信息
	 * @return
	 */
	public static String jvmInfo() {
		try {
			String st = System.getProperty("line.separator");
			StringBand string=new StringBand();
			String jsonString=JSONObject.toJSONString(new JVMInfoMonitor().getJVMInfo());
			string.append("jvm信息：").append(st).append(JSONFormatter.formatJSON(jsonString));
			return  string.toString() ;
		} catch (Throwable e) {
			logger.error("获取jvm信息出错", e);
			return e.getMessage();
		}
	}

	/**
	 * 进程id
	 * @return
	 */
	public static String jstackInfo() {
		StringBuilder info = new StringBuilder(1000);
		info.append("JVM  线程信息:");
		try {
			String processId = new JVMInfoMonitor().processId();
			info.append("进程id=" + processId).append(Smile.LINE_SEPARATOR);
			Process p = Runtime.getRuntime().exec("jstack -l " + processId);
			InputStream is = p.getInputStream();
			byte[] bytes = IOUtils.stream2byte(is);
			info.append(new String(bytes));
		} catch (Throwable e) {
			logger.error("获取jvm stack 信息出错", e);
		}
		return info.toString();
	}

	public static String jmapInfo() {
		StringBuilder info = new StringBuilder(1000);
		info.append("JVM  内存信息:");
		try {
			String processId = new JVMInfoMonitor().processId();
			info.append("进程id=" + processId).append(Smile.LINE_SEPARATOR);
			Process p = Runtime.getRuntime().exec("jmap -histo " + processId);
			InputStream is = p.getInputStream();
			byte[] bytes = IOUtils.stream2byte(is);
			info.append(new String(bytes));
		} catch (Throwable e) {
			logger.error("获取jvm jmap 信息出错", e);
		}
		return info.toString();
	}
	
	public static String getHeapInfo(){
		StringBuilder info = new StringBuilder(1000);
		info.append("HEAP  内存信息:");
		try {
			String processId = new JVMInfoMonitor().processId();
			info.append("进程id=" + processId).append(Smile.LINE_SEPARATOR);
			Process p = Runtime.getRuntime().exec("jmap -heap " + processId);
			InputStream is = p.getInputStream();
			byte[] bytes = IOUtils.stream2byte(is);
			info.append(new String(bytes));
		} catch (Throwable e) {
			logger.error("获取jvm heap 信息出错", e);
		}
		return info.toString();
	}

	/**
	 * 创建jvm 复本 dump 文件
	 * @return
	 */
	public static String createDumpFile() {
		StringBuilder info = new StringBuilder(1000);
		info.append("JVM  dump文件:");
		try {
			String processId = new JVMInfoMonitor().processId();
			String filename = "dump-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".hprof";
			info.append("进程id=" + processId).append(Smile.LINE_SEPARATOR);
			Process p = Runtime.getRuntime().exec("jmap -dump:format=b,file=" + filename + " " + processId);
			InputStream is = p.getInputStream();
			byte[] bytes = IOUtils.stream2byte(is);
			info.append(new String(bytes));
			info.append("文件名：" + filename);
		} catch (Throwable e) {
			logger.error("获取jvm jmap 信息出错", e);
		}
		return info.toString();
	}
}
