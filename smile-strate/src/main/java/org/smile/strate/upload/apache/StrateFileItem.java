package org.smile.strate.upload.apache;

import java.io.File;
import java.io.IOException;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.smile.io.FileUtils;

public class StrateFileItem extends DiskFileItem{

	public StrateFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold, File repository) {
		super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
	}

	@Override
	public File getTempFile() {
		return super.getTempFile();
	}

	@Override
	public void delete() {
		File file=getTempFile();
		try {
			FileUtils.deleteFile(file);
		} catch (IOException e) {}
	}

	@Override
	public String toString() {
		return getFieldName()+":"+getTempFile();
	}
	
}
