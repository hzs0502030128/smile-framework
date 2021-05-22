package org.smile.log.record;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.smile.io.IOUtils;
import org.smile.util.StringUtils;
import org.smile.util.SysUtils;



public class FileHandler extends AbstractHandler{

	private static DateFormat df;
	
	protected String pattern;
	
	private PrintWriter cacheWriter;
	
	private String cacheKey;
	/**定时写入日志*/
	private Timer executeTimer=new Timer();
	/**最大缓冲日志个数*/
	private int cacheSize=50;
	/**写入文件缓冲时间间隔**/
	private int writeDelay=5000;
	/**
	 * 运行的任务
	 */
	private Queue<Runnable> fileJobs=new ConcurrentLinkedQueue<Runnable>();
	/**写入文件任务*/
	private TimerTask task;
	
	public FileHandler(String pattern){
		this.pattern=pattern;
		task=new TimerTask() {
			@Override
			public void run() {
				synchronized (this) {
					Runnable run;
					while((run=fileJobs.poll())!=null){
						run.run();
					}
				}
			}
		};
		executeTimer.schedule(task, writeDelay,writeDelay);
	}
	@Override
	public void handle(final LogRecord record,final Throwable throwable) {
		if(needLog(record)){
			Runnable runnable=new Runnable() {
				@Override
				public void run() {
					try{
						PrintWriter os=openWriter();
						os.println(formatter.format(record));
						if(throwable!=null){
							throwable.printStackTrace(os);
						}
						os.flush();
					}catch(Throwable e){
						SysUtils.log("write file log error", throwable);
					}
				}
			};
			//如果超过缓冲数量直接写入文件
			if(fileJobs.size()>cacheSize){
				task.run();
			}else{
				fileJobs.add(runnable);
			}
		}
	}
	
	
	private PrintWriter openWriter() throws IOException{
		String fileName=parsePatter();
		if(cacheKey==null){
			cacheKey=fileName;
			Writer os=new OutputStreamWriter(IOUtils.openOutputStream(new File(fileName), true));
			cacheWriter=new PrintWriter(os,true);
		}else if(!fileName.equals(cacheKey)){
			Writer os=new OutputStreamWriter(IOUtils.openOutputStream(new File(fileName), true));
			cacheWriter.close();
			cacheWriter=new PrintWriter(os,true);
		}
		return cacheWriter;
	}
	
	/***
	 * 解析文件名命名样式
	 * @return
	 */
	private String parsePatter(){
		String dateFormat= StringUtils.substringBetween(pattern, "'");
		if(dateFormat!=null){
			if(df==null){
				df=new SimpleDateFormat(dateFormat);
			}
			return pattern.replaceAll(dateFormat, df.format(new Date()));
		}
		return pattern;
	}
	
}
