package org.smile.strate.upload;

import org.smile.strate.upload.impl.MultipartUploadParser;

/**
 * 文件上传配置常量信息
 * @author 胡真山
 * @Date 2016年1月26日
 */
public class UploadConstants {
	/**保存目录*/
	public static String saveDir="/temp";
	/**一次上传文件最大数值   最终的限制还与单位有关，默认kb数*/
	public static long sizeMax=2048*1024;
	/**一个文件上传文件最大值  最终的限制还与单位有关，默认kb数*/
	public static long fileSizeMax=1024*1024;
	/**最大内存保存*/
	public static int sizeThreshold=512;
	/**配置数量的单位   默认为1024 即KB*/
	public static int unit=1024;
	/**上传表单类型*/
	public static String MULTIPART_FORM_DATA = "multipart/form-data";
	/**用于封装文件名称*/
	public static String fileName="FileName";
	/**用于封闭文件类型*/
	public static String fileContentType="ContentType";
	/**用于封闭文件类型*/
	public static String fileFileType="FileType";
	/**上传表单解析器*/
	public static Class uploadParser=MultipartUploadParser.class;
}
