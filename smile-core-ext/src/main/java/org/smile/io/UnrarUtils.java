package org.smile.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.smile.collection.ArrayUtils;
import org.smile.util.StringUtils;
import org.smile.util.Wildcard;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.NativeStorage;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

/***
 * rar解压工具
 * @author 胡真山
 *
 */
public class UnrarUtils {
	
	public static void unrar(File rarFileName) throws IOException{
		unrar(rarFileName, null);
	}
	/**
	 * 解压一个rar文件 
	 * @param rarFileName
	 * @param outFilePath
	 * @param patterns 排除的路径
	 * @throws Exception
	 */
	public static void unrar(File rarFileName, String outFilePath,String... patterns) throws IOException {
		Archive archive=null;
		try {
			archive = new Archive(new NativeStorage(rarFileName));
			if (archive.isEncrypted()) {
				throw new IOException(rarFileName + " IS ENCRYPTED!");
			}
			if(StringUtils.isEmpty(outFilePath)){
				outFilePath=rarFileName.getParent();
			}
			List<FileHeader> files = archive.getFileHeaders();
			for (FileHeader fh : files) {
				if (fh.isEncrypted()) {
					continue;
				}
				String fileName = fh.getFileNameString();
				if (ArrayUtils.notEmpty(patterns)) {
					if (Wildcard.matchPathOne(fileName, patterns)<0) {
						continue;
					}
				}
				if (!StringUtils.isEmptyAfterTrim(fileName)) {
					String saveName=fh.getFileNameW();
					if(StringUtils.isEmptyAfterTrim(saveName)){
						saveName=fileName;
					}
					String saveFileName = outFilePath + File.separator + saveName;
					File saveFile = new File(saveFileName);
					File parent = saveFile.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					if (!saveFile.exists()) {
						saveFile.createNewFile();
					}
					if(!fh.isDirectory()){
						FileOutputStream fos = new FileOutputStream(saveFile);
						try {
							archive.extractFile(fh, fos);
							fos.flush();
						} finally {
							fos.close();
						}
					}
				}
			}
		} catch (RarException e) {
			throw new IOException("unrar "+rarFileName+" excepion ",e);
		} finally {
			IOUtils.close(archive);
		}
	}

}
