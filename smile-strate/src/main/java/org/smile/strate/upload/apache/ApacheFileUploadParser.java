package org.smile.strate.upload.apache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.util.Streams;
import org.smile.strate.Strate;
import org.smile.strate.upload.FieldItem;
import org.smile.strate.upload.FileUploadParser;
import org.smile.strate.upload.StrateUploadException;

/**
 * 上传文件处理
 * 
 * @author 胡真山
 * @Date 2016年1月26日
 */
public class ApacheFileUploadParser extends FileUpload implements FileUploadParser{
	/** 上传信息factory */
	private static DiskFileItemFactory factory = new StrateFileItemFactory();

	public ApacheFileUploadParser() {
		setFileItemFactory(factory);
		setHeaderEncoding(Strate.encoding);
	}

	@Override
	public List<FieldItem> parseRequestItems(HttpServletRequest request) throws StrateUploadException {
		RequestContext ctx = new ServletRequestContext(request);
		List<FieldItem> items = new ArrayList<FieldItem>();
		boolean successful = false;
		try {
			FileItemIterator iter = getItemIterator(ctx);
			FileItemStream item;
			while (iter.hasNext()) {
				item = iter.next();

				String fileName = item.getName();

				FileItem fileItem = factory.createItem(item.getFieldName(), item.getContentType(), item.isFormField(), fileName);

				items.add(new ApacheFieldItem((StrateFileItem)fileItem));
				try {
					Streams.copy(item.openStream(), fileItem.getOutputStream(), true);
				} catch (FileUploadIOException e) {
					throw ((FileUploadException) e.getCause());
				} catch (IOException e) {
					throw new IOFileUploadException(String.format("Processing of %s request failed. %s", new Object[] { "multipart/form-data", e.getMessage() }), e);
				}
				FileItemHeaders fih = item.getHeaders();
				fileItem.setHeaders(fih);
			}
			successful = true;
			return items;
		} catch (FileUploadException e) {
			throw new StrateUploadException((FileUploadException) e.getCause());
		} catch (IOException e) {
			throw new StrateUploadException(e.getMessage(), e);
		} finally {
			if (!successful) {//失败时删除上传的文件
				for (FieldItem fileItem : items){
					try {
						fileItem.delete();
					} catch (Throwable e) {}
				}
			}
		}
	}
}
