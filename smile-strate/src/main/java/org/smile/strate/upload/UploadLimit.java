package org.smile.strate.upload;


public class UploadLimit {
	/**一次上传文件最大数值   最终的限制还与单位有关，默认kb数*/
	public  long sizeMax=2048*1024;
	/**一个文件上传文件最大值  最终的限制还与单位有关，默认kb数*/
	public  long fileSizeMax=1024*1024;
	/**
	 * @param sizeMax 一次上传限制大小
	 * @param fileSizeMax 每个文件上传限制大小
	 */
	public UploadLimit(long sizeMax,long fileSizeMax){
		this.sizeMax=sizeMax;
		this.fileSizeMax=fileSizeMax;
	}
	/**
	 * 设置限制
	 * @param upload
	 */
	public void limit(FileUploadParser upload){
		upload.setSizeMax(sizeMax* UploadConstants.unit);
		upload.setFileSizeMax(fileSizeMax * UploadConstants.unit);
	}
}
