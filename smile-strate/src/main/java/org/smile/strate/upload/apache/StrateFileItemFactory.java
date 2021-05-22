package org.smile.strate.upload.apache;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileCleaningTracker;
import org.smile.strate.upload.UploadConstants;

public class StrateFileItemFactory extends DiskFileItemFactory {
	
	public StrateFileItemFactory(){
		super();
		setSizeThreshold(UploadConstants.sizeThreshold * UploadConstants.unit);
		File fileFoder = new File(UploadConstants.saveDir);
		if (!fileFoder.exists()) {
			fileFoder.mkdir();
		}
		setRepository(fileFoder);
	}
	
	@Override
	public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName)
	  {
	    StrateFileItem result = new StrateFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository());

	    FileCleaningTracker tracker = getFileCleaningTracker();
	    if (tracker != null) {
	      tracker.track(result.getTempFile(), result);
	    }
	    return result;
	  }
}
