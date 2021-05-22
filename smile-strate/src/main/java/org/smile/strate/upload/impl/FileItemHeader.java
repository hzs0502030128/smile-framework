package org.smile.strate.upload.impl;

import org.smile.commons.Strings;
import org.smile.io.FileNameUtils;


/**
 * Parses file upload header.
 */
public class FileItemHeader {

	String dataHeader;
	/***
	 * 表单字段名
	 */
	String formFieldName;
	/**文件名*/
	String formFileName;
	/**路径名*/
	String path;
	/**单文件名*/
	String fileName;

	boolean isFile;
	
	String contentType;
	
	String mimeType;
	
	String mimeSubtype;
	
	String contentDisposition;


	FileItemHeader(String dataHeader) {
		this.dataHeader = dataHeader;
		isFile = dataHeader.indexOf("filename") > 0;
		formFieldName = getDataFieldValue(dataHeader, "name");
		if (isFile) {
			formFileName = getDataFieldValue(dataHeader, "filename");
			if (formFileName == null) {
				return;
			}
			if (formFileName.length() == 0) {
				path = Strings.EMPTY;
				fileName = Strings.EMPTY;
			}
			
			int ls = FileNameUtils.indexOfLastSeparator(formFileName);
			if (ls == -1) {
				path = Strings.EMPTY;
				fileName = formFileName;
			} else {
				path = formFileName.substring(0, ls);
				fileName = formFileName.substring(ls + 1);
			}
			if (fileName.length() > 0) {
				this.contentType = getContentType(dataHeader);
				mimeType = getMimeType(contentType);
				mimeSubtype = getMimeSubtype(contentType);
				contentDisposition = getContentDisposition(dataHeader);
			}
		}
	}

	/**
	 * Gets value of data field or <code>null</code> if field not found.
	 */
	private String getDataFieldValue(String dataHeader, String fieldName) {
		String value = null;
		String token = String.valueOf((new StringBuffer(String.valueOf(fieldName))).append('=').append('"'));
		int pos = dataHeader.indexOf(token);
		if (pos > 0) {
			int start = pos + token.length();
			int end = dataHeader.indexOf('"', start);
			if ((start > 0) && (end > 0)) {
				value = dataHeader.substring(start, end);
			}
		}
		return value;
	}

	/**
	 * Strips content type information from requests data header.
	 * @param dataHeader data header string
	 * @return content type or an empty string if no content type defined
	 */
	private String getContentType(String dataHeader) {
		String token = "Content-Type:";
		int start = dataHeader.indexOf(token);
		if (start == -1) {
			return Strings.EMPTY;
		}
		start += token.length();
		return dataHeader.substring(start);
	}

	private String getContentDisposition(String dataHeader) {
		int start = dataHeader.indexOf(':') + 1;
		int end = dataHeader.indexOf(';');
		return dataHeader.substring(start, end);
	}

	private String getMimeType(String ContentType) {
		int pos = ContentType.indexOf('/');
		if (pos == -1) {
			return ContentType;
		}
		return ContentType.substring(1, pos);
	}

	private String getMimeSubtype(String ContentType) {
		int start = ContentType.indexOf('/');
		if (start == -1) {
			return ContentType;
		}
		start++;
		return ContentType.substring(start);
	}

	/**
	 * Returns <code>true</code> if uploaded data are correctly marked as a file.
	 * This is true if header contains string 'filename'.
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * Returns form field name.
	 */
	public String getFormFieldName() {
		return formFieldName;
	}

	/**
	 * Returns complete file name as specified at client side.
	 */
	public String getFormFilename() {
		return formFileName;
	}

	/**
	 * Returns file name (base name and extension, without full path data).
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns uploaded content type. It is usually in the following form:<br>
	 * mime_type/mime_subtype.
	 *
	 * @see #getMimeType()
	 * @see #getMimeSubtype()
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns file types MIME.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns file sub type MIME.
	 */
	public String getMimeSubtype() {
		return mimeSubtype;
	}

	/**
	 * Returns content disposition. Usually it is 'form-data'.
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}
}
