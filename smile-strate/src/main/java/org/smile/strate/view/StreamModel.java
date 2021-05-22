package org.smile.strate.view;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.smile.io.FileNameUtils;
import org.smile.strate.Strate;
import org.smile.strate.jump.JumpType;

public class StreamModel extends AbstractModel<Map<String, Object>> {

	public StreamModel() {
		this.data = new HashMap<String, Object>();
	}
	/**
	 * 以流形式返回
	 * @param name
	 * @param is
	 */
	public StreamModel(String name,InputStream is) {
		this();
		setDownloadName(name);
		setInputStream(is);
	}
	/**
	 * 可自定义文件名
	 * @param name
	 * @param file
	 */
	public StreamModel(String name,File file) {
		this();
		setDownloadName(name);
		setFile(file);
	}
	/**
	 * 以文件返回
	 * @param file
	 */
	public StreamModel(File file){
		this(FileNameUtils.getName(file.getName()), file);
	}

	public void setFile(File file) {
		this.data.put(Strate.downloadTargetParamName, file);
	}

	public void setInputStream(InputStream is) {
		this.data.put(Strate.downloadTargetParamName, is);
	}

	public void setDownloadName(String downloadName) {
		this.data.put(Strate.downloadNameParamName, downloadName);
	}

	@Override
	protected JumpType defaultJumpType() {
		return JumpType.stream;
	}
}
