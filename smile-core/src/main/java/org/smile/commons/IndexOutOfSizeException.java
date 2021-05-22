package org.smile.commons;

public class IndexOutOfSizeException extends IndexOutOfBoundsException{
	/**
	 */
	private static final long serialVersionUID = 4679797358699075093L;

	public IndexOutOfSizeException(int size,int index){
		super("Index "+index+" out of size "+size);
	}
}
