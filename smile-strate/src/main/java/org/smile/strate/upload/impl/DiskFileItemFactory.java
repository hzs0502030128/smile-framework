package org.smile.strate.upload.impl;

import java.io.File;

/**
 *
 * Factory for {@link DiskFileItem}.
 */
public class DiskFileItemFactory implements FileItemFactory {

	protected int sizeThreshold = 8192;
	protected File repository;
	protected boolean breakOnError;
	protected String[] fileExtensions;
	protected boolean allowFileExtensions = true;

	/**
	 * {@inheritDoc}
	 */
	public FileItem create(MultipartRequestInputStream input) {
		return new DiskFileItem(input, sizeThreshold, repository,breakOnError, fileExtensions, allowFileExtensions);
	}

	/**
	 * Specifies per file memory limit for keeping uploaded files in the memory.
	 */
	public DiskFileItemFactory setSizeThreshold(int memoryThreshold) {
		if (memoryThreshold >= 0) {
			this.sizeThreshold = memoryThreshold;
		}
		return this;
	}

	public File getUploadPath() {
		return repository;
	}

	/**
	 * Specifies the upload path. If set to <code>null</code> default
	 * system TEMP path will be used.
	 */
	public DiskFileItemFactory  setUploadPath(File uploadPath) {
		this.repository = uploadPath;
		return this;
	}

	public boolean isBreakOnError() {
		return breakOnError;
	}

	public DiskFileItemFactory setBreakOnError(boolean breakOnError) {
		this.breakOnError = breakOnError;
		return this;
	}

	/**
	 * Specifies if upload should break on error.
	 */
	public DiskFileItemFactory breakOnError(boolean breakOnError) {
		this.breakOnError = breakOnError;
		return this;
	}

	/**
	 * Allow or disallow set of file extensions. Only one rule can be active at time,
	 * which means user can only specify extensions that are either allowed or disallowed.
	 * Setting this value to <code>null</code> will turn this feature off.
	 */
	public DiskFileItemFactory setFileExtensions(String[] fileExtensions, boolean allow) {
		this.fileExtensions = fileExtensions;
		this.allowFileExtensions = allow;
		return this;
	}

	public void setRepository(File repository) {
		this.repository = repository;
	}

	
}
