package org.smile.log.jvm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.StringTokenizer;

import org.smile.io.BufferedReader;
import org.smile.io.IOUtils;

import com.sun.management.OperatingSystemMXBean;

/***
 * jvm信息监控
 * @author 胡真山
 *
 */
public class JVMInfoMonitor {

	private static String linuxVersion = null;
	
	private static int kb = 1024;

	/** 
	 * 获得当前的监控对象.
	 * @return 返回构造好的监控对象
	 * @throws Exception
	 * @author GuoHuang
	 */
	public JVMInfo getJVMInfo() throws Exception {
		// 可使用内存
		long totalMemory = Runtime.getRuntime().totalMemory() / kb;
		// 剩余内存
		long freeMemory = Runtime.getRuntime().freeMemory() / kb;
		// 最大可使用内存
		long maxMemory = Runtime.getRuntime().maxMemory() / kb;

		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// 操作系统
		String osName = System.getProperty("os.name");
		// 总的物理内存
		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
		// 剩余的物理内存
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
		// 已使用的物理内存
		long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;

		// 获得线程总数
		ThreadGroup parentThread;
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; parentThread = parentThread.getParent())
			;
		int totalThread = parentThread.activeCount();

		double cpuRatio = 0;
		if (osName.toLowerCase().startsWith("windows")) {
		} else {
			cpuRatio = this.getCpuRateForLinux();
		}
		// 构造返回对象
		JVMInfo infoBean = new JVMInfo();
		infoBean.setJvmFreeMemory(freeMemory);
		infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
		infoBean.setJvmMaxMemory(maxMemory);
		infoBean.setOsName(osName);
		infoBean.setJvmTotalMemory(totalMemory);
		infoBean.setTotalPhysicalMemorySize(totalMemorySize);
		infoBean.setTotalThread(totalThread);
		infoBean.setUsedPhysicalMemory(usedMemory);
		infoBean.setCpuRatio(cpuRatio);
		return infoBean;
	}

	/**
	 * 进程id
	 * @return
	 */
	public String processId() {
		String pid = ManagementFactory.getRuntimeMXBean().getName();
		int indexOf = pid.indexOf('@');
		if (indexOf > 0) {
			pid = pid.substring(0, indexOf);
		}
		return pid;
	}

	/***
	 * Cpu使用率
	 * @return
	 */
	private  double getCpuRateForLinux() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try {
			Process process = Runtime.getRuntime().exec("top -b -n 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);

			if (linuxVersion.equals("2.4")) {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				String user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();

				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));

				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();

				return (userUsage + systemUsage + niceUsage) / 100;
			} else {
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				tokenStat.nextToken();
				String cpuUsage = tokenStat.nextToken();

				Float usage = new Float(cpuUsage.substring(0, cpuUsage.indexOf("%")));

				return (1 - usage.floatValue() / 100);
			}

		} catch (IOException ioe) {
			freeResource(is, isr, brStat);
			return 1;
		} finally {
			freeResource(is, isr, brStat);
		}

	}

	private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {
		IOUtils.close(is);
		IOUtils.close(isr);
		IOUtils.close(br);
	}
}