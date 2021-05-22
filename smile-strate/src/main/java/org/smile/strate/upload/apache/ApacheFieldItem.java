package org.smile.strate.upload.apache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.smile.commons.SmileRunException;
import org.smile.strate.upload.FieldItem;

public class ApacheFieldItem implements FieldItem{
	
	private StrateFileItem fileItem;
	
	public ApacheFieldItem(StrateFileItem item){
		this.fileItem=item;
	}
	
	@Override
	public boolean isFormField() {
		return fileItem.isFormField();
	}

	@Override
	public boolean isInMemory() {
		return fileItem.isInMemory();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return fileItem.getInputStream();
	}

	@Override
	public String getString() {
		return fileItem.getString();
	}

	@Override
	public String getString(String encoding) {
		try {
			return fileItem.getString(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public String getFieldName() {
		return fileItem.getFieldName();
	}

	@Override
	public File getFile() {
		return fileItem.getTempFile();
	}

	@Override
	public void delete() {
		fileItem.delete();
	}
	@Override
	public String getContentType() {
		return fileItem.getContentType();
	}
	@Override
	public String getFileName() {
		return fileItem.getName();
	}
	@Override
	public File getTempFile() {
		return fileItem.getTempFile();
	}
	@Override
	public byte[] getMemoryBytes() {
		return fileItem.get();
	}

}
