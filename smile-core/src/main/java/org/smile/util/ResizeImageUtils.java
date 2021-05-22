package org.smile.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.smile.io.FileNameUtils;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
  
/**
 * 对图片文件进行缩放功能的工具类
 * @author 胡真山
 *
 */ 
public class ResizeImageUtils {  
	
	protected static final Logger logger=LoggerFactory.getLogger(ResizeImageUtils.class);
    /** 
     * @param im            原始图像 
     * @param resizeTimes   需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小 
     * @return              返回处理后的图像 
     */  
    public static BufferedImage resizeImage(BufferedImage im, float resizeTimes) {  
        /*原始图像的宽度和高度*/  
        int width = im.getWidth();  
        int height = im.getHeight();  
  
        /*调整后的图片的宽度和高度*/  
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);  
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);  
  
        /*新生成结果图片*/  
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);  
  
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
        return result;  
    }  
    /** 
     * @param im            原始图像 
     * @param width 		缩小后的宽度 
     * @param width 		缩小后的高度
     * @return              返回处理后的图像 
     */  
    public static BufferedImage resizeImage(BufferedImage im, int  width,int height) {  
        /*新生成结果图片*/  
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        result.getGraphics().drawImage(im.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
        return result;  
    }  
     /** 
     * @param im            原始图像 
     * @param resizeTimes   倍数,比如0.5就是缩小一半,0.98等等float类型 
     * @return              返回处理后的图像 
     */  
    public static BufferedImage zoomImage(BufferedImage im, float resizeTimes) {  
        /*原始图像的宽度和高度*/  
        int width = im.getWidth();  
        int height = im.getHeight();  
        /*调整后的图片的宽度和高度*/  
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);  
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);  
        /*新生成结果图片*/  
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);  
        //把原图写入
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
        return result;  
    }  
  
    /**
     * 读取一个图片文件
     * @param path 文件的目录
     * @param filename 文件的名称
     * @return
     * @throws IOException
     */
    public static BufferedImage readImage(String path,String filename) throws IOException{
    	File file=new File(path+filename);
    	return javax.imageio.ImageIO.read(file);
    }
    
    /** 
     * 把图片写到磁盘上 
  	*/
    public static boolean writeToDisk(BufferedImage im, String path, String fileName) {  
       return writeToDisk(im, path, fileName, "jpg");
    }  
    /** 
     * 把图片写到磁盘上 
     * @param image 
     * @param path     eg: C://home// 图片写入的文件夹地址 
     * @param fileName DCM1987.jpg  写入图片的名字 
     * @return 
     */  
    public static boolean writeToDisk(BufferedImage image, String path, String fileName,String formatName) {  
        File f = new File(path + fileName);  
        try {  
            ImageIO.write(image, formatName, f);  
            image.flush();  
            return true;  
        } catch (IOException e) { 
        	logger.error(e);
            return false;  
        }  
    } 
  
    /**
	 * 创建缩小版的图片在同一个文件夹中
	 * @param path
	 */
	public static  void createMinImages(String path,List<String> fileNames){
		//从配置读取文件目录 
		for(int i=0;i<fileNames.size();i++){
			String fileName=(String)fileNames.get(i);
			//转后的图片
			BufferedImage image;
			try {
				image=readImage(path,fileName);
				image = resizeImage(image,300,300*image.getHeight()/image.getWidth());
				String realFileName=FileNameUtils.getBaseName(fileName);
				String extendsName=FileNameUtils.getExtension(fileName);
				String minFileName=realFileName+"_min";
				String newFileExtName="jpg";
				//新文件名
				if(StringUtils.notEmpty(extendsName)){
					minFileName+="."+extendsName;
					newFileExtName=extendsName;
				}
				writeToDisk(image,path,minFileName,newFileExtName);
			} catch (Exception e) {
				logger.error("转换文件大小"+fileName+"出错",e);
				continue;
			}
		}
	}
} 