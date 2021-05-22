package org.smile.strate.upload.impl;


/**
 * {@link FileItem} factory for handling uploaded files. Implementations may
 * handle uploaded files differently: to store them to memory, directly to disk
 * or something else.
 */
public interface FileItemFactory {

	/**
	 * Creates new instance of {@link FileItem uploaded file}.
	 */
	FileItem create(MultipartRequestInputStream input);
}