package org.smile.strate.upload.impl;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.smile.strate.Strate;
import org.smile.strate.upload.FieldItem;
import org.smile.strate.upload.FileUploadParser;
import org.smile.strate.upload.StrateUploadException;
import org.smile.strate.upload.UploadConstants;

public class MultipartUploadParser implements FileUploadParser {

	protected FileItemFactory fileUploadFactory;
	
	protected long sizeMax;
	
	protected long fileSizeMax;

	public MultipartUploadParser() {
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setRepository(new File(UploadConstants.saveDir));
		factory.setSizeThreshold(UploadConstants.sizeThreshold*UploadConstants.unit);
		this.fileUploadFactory = factory;
	}

	@Override
	public List<FieldItem> parseRequestItems(HttpServletRequest request) throws StrateUploadException {
		List<FieldItem> results=new LinkedList<FieldItem>();
		boolean successful=false;
		try {
			InputStream inputStream = request.getInputStream();
			MultipartRequestInputStream input = new MultipartRequestInputStream(inputStream);
			input.readBoundary();
			while (true) {
				FileItemHeader header = input.readDataHeader(Strate.encoding);
				if (header == null) {
					break;
				}
				
				FileItem fileItem = fileUploadFactory.create(input);
				if (header.isFile == true) {
					String fileName = header.fileName;
					if (fileName.length() > 0) {
						if (header.contentType.indexOf("application/x-macbinary") > 0) {
							input.skipBytes(128);
						}
					}
					fileItem.processStream(fileSizeMax);
					if (fileName.length() == 0) {
						// file was specified, but no name was provided,
						// therefore it was not uploaded
						if (fileItem.getSize() == 0) {
							fileItem.size = -1;
						}
					}
				} else {
					fileItem.processFormField();
				}
				results.add(fileItem);
				input.skipBytes(1);
				input.mark(1);

				// read byte, but may be end of stream
				int nextByte = input.read();
				if (nextByte == -1 || nextByte == '-') {
					input.reset();
					break;
				}
			}
			input.reset();
			successful=true;
			return results;
		} catch (Throwable e) {
			throw new StrateUploadException(e);
		}finally{
			if(!successful){
				for(FieldItem item:results){
					item.delete();
				}
			}
		}
	}

	@Override
	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	@Override
	public void setFileSizeMax(long sizeMax) {
		this.fileSizeMax = sizeMax;
	}

}
