package org.smile.io.download;

import javax.servlet.http.HttpServletRequest;

public interface DownloadHandler {
	
	/**
	 * 下载区间
	 * @author 胡真山
	 *
	 */
	class DownloadRange{
		
		protected long start;
		
		protected long end;
		
		DownloadRange(){}
		
		DownloadRange(long start,long end){
			this.start=start;
			this.end=end;
		}
		
		public long getStart() {
			return start;
		}
		public void setStart(long start) {
			this.start = start;
		}
		public long getEnd() {
			return end;
		}
		public void setEnd(long end) {
			this.end = end;
		}
		
		public long getLength(){
			return end-start+1;
		}
		
		public String getRangeString(){
			return "bytes=" + start + "-"+ end+ "/"+ getLength();
		}
	}
	/**
	 * 下载区间
	 * @param request
	 * @return
	 */
	public DownloadRange getDownloadArea(HttpServletRequest request);
	/**
	 * 是否是使用了断点续传下载
	 * @param request
	 * @return
	 */
	public boolean isUseRange(HttpServletRequest request);
	
}
