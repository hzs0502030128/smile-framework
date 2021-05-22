package org.smile.strate.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传的元素
 * @author 胡真山
 *
 */
public interface FieldItem {
	/**是否是表单字段*/
	public boolean isFormField();
	/**是否缓存在内存中*/
	public boolean isInMemory();
	/**打开输入流用于读取文件内容
	 * @throws IOException */
	public InputStream getInputStream() throws IOException;
	/**表单字段内容*/
	public String getString();
	/**表单字段内容*/
	public String getString(String encoding);
	/**上传表单字段名**/
	public String getFieldName();
	/**存入磁盘的文件*/
	public File getFile();
	/**删除临时文件*/
	public void delete();
	/**上传的文件类型*/
	public String getContentType();
	/**上传文件的文件名**/
	public String getFileName();
	/**上传磁盘保存临时文件
	 * @throws IOException */
	public File getTempFile();
	/**内存中缓存的文件内容*/
	public byte[] getMemoryBytes();
}
