package org.smile.strate;

/**
 * strate 配置常量
 */
import org.smile.Smile;
import org.smile.strate.i18n.I18nConstants;
import org.smile.strate.upload.UploadConstants;
import org.smile.validate.ValidateConstants;
/**
 * 用于保存静态常量信息
 * @author 胡真山
 * @Date 2016年1月18日
 */
public class Strate {
	/**字符编码*/
	public static String encoding=Smile.ENCODE;
	/**url decode*/
	public static String decoding="ISO8859-1";
	/**action 常量信息*/
	public static ActionConstants action;
	/**类型转换文件扩展名*/
	public static String conversion="-conversion.properties";
	/**全局转换文件*/
	public static String globaconversion="strate"+conversion;
	/**国际化资源*/
	public static I18nConstants i18n;
	/**上传信息配置常量*/
	public static UploadConstants upload;
	/**验证信息常量*/
	public static ValidateConstants validate;
	/**下载文件显示名称参数*/
	public static String downloadNameParamName = "downloadName";
	/**下载对象参数*/
	public static String downloadTargetParamName = "download";
	
}
