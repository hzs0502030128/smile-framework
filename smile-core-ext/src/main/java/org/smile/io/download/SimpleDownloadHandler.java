package org.smile.io.download;

import javax.servlet.http.HttpServletRequest;

import org.smile.log.LoggerHandler;
import org.smile.util.StringUtils;

public class SimpleDownloadHandler implements DownloadHandler,LoggerHandler {

	@Override
	public DownloadRange getDownloadArea(HttpServletRequest request) {
		//请求的格式是: //Range: bytes=[文件块的开始字节]-
		String range = request.getHeader("Range");
		range.substring(range.indexOf("=")+1);
		long start=0;
		long end=0;
		try {
			String[] ranges=StringUtils.split(range, "-");
			if(ranges.length>0){
				start= Long.parseLong(ranges[0]);
			}
			if(ranges.length>1){
				end = Long.parseLong(ranges[1]);
			}
		} catch (Exception e) {
			logger.error("获取下载区块信息失败", e);
		}
		return new DownloadRange(start, end);
	}

	@Override
	public boolean isUseRange(HttpServletRequest request) {
		String range=request.getHeader("Range");
		return StringUtils.notEmpty(range);
	}

}
