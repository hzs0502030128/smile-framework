
package org.smile.commons;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.smile.io.IOUtils;
import org.smile.util.StringUtils;

public class MimeTypes {

	public static final String MIME_APPLICATION_ATOM_XML 		= "application/atom+xml";
	public static final String MIME_APPLICATION_JAVASCRIPT		= "application/javascript";
	public static final String MIME_APPLICATION_JSON 			= "application/json";
	public static final String MIME_APPLICATION_OCTET_STREAM	= "application/octet-stream";
	public static final String MIME_APPLICATION_XML 			= "application/xml";
	public static final String MIME_APPLICATION_PDF 			= "application/pdf";
	public static final String MIME_TEXT_CSS					= "text/css";
	public static final String MIME_TEXT_PLAIN 					= "text/plain";
	public static final String MIME_TEXT_HTML					= "text/html";

	private static final HashMap<String, String> MIME_TYPE_MAP;	

	static {
		Properties mimes = new Properties();

		InputStream is = MimeTypes.class.getClassLoader().getResourceAsStream(MimeTypes.class.getSimpleName() + ".properties");
		if (is == null) {
			throw new IllegalStateException("Mime types file missing");
		}
		try {
			mimes.load(is);
		}
		catch (IOException ioex) {
			throw new IllegalStateException(ioex.getMessage());
		} finally {
			IOUtils.close(is);
		}

		MIME_TYPE_MAP = new HashMap<String, String>(mimes.size() * 2);

		Enumeration keys = mimes.propertyNames();
		while (keys.hasMoreElements()) {
			String mimeType = (String) keys.nextElement();
			String extensions = mimes.getProperty(mimeType);

			if (mimeType.startsWith("/")) {
				mimeType = "application" + mimeType;
			} else if (mimeType.startsWith("a/")) {
				mimeType = "audio" + mimeType.substring(1);
			} else if (mimeType.startsWith("i/")) {
				mimeType = "image" + mimeType.substring(1);
			} else if (mimeType.startsWith("t/")) {
				mimeType = "text" + mimeType.substring(1);
			} else if (mimeType.startsWith("v/")) {
				mimeType = "video" + mimeType.substring(1);
			}

			String[] allExtensions = StringUtils.split(extensions, String.valueOf(' '));

			for (String extension : allExtensions) {
				if (MIME_TYPE_MAP.put(extension, mimeType) != null) {
					throw new IllegalArgumentException("Duplicated extension: " + extension);
				}
			}
		}
	}

	/**
	 * Registers MIME type for provided extension. Existing extension type will be overridden.
	 */
	public static void registerMimeType(String ext, String mimeType) {
		MIME_TYPE_MAP.put(ext, mimeType);
	}

	/**
	 * Returns the corresponding MIME type to the given extension.
	 * If no MIME type was found it returns <code>application/octet-stream</code> type.
	 */
	public static String getMimeType(String ext) {
		String mimeType = lookupMimeType(ext);
		if (mimeType == null) {
			mimeType = MIME_APPLICATION_OCTET_STREAM;
		}
		return mimeType;
	}

	/**
	 * Simply returns MIME type or <code>null</code> if no type is found.
	 */
	public static String lookupMimeType(String ext) {
		return MIME_TYPE_MAP.get(ext.toLowerCase());
	}

	
}