package org.smile.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.smile.commons.StringBand;
import org.smile.commons.Strings;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
import org.smile.util.StringUtils;

/***
 * code 128 实现类
 * @author 胡真山
 * 2015年9月14日
 */
public abstract class Code128 {
	
	protected static final Logger logger=LoggerFactory.getLogger(Code128.class);
	/**奇数位标签*/
	protected  static String[] TagB = { "", "b", "bb", "bbb", "bbbb" };
	/**偶数位标签*/
	protected  static String[] TagS = { "", "s", "ss", "sss", "ssss" };
	
	protected static  String C_START="bbsbssbbbss"; 
	protected static  String B_START="bbsbssbssss";
	protected static  String A_START="bbsbssssbss";
	
	protected static final char TYPE_A='A';
	protected static final char TYPE_B='B';
	protected static final char TYPE_C='C';
	/**验证符*/
	protected static final int CHECK_A=103;
	protected static final int CHECK_B=104;
	protected static final int CHECK_C=105;
	/**编码转换符*/
	protected static final int CODE_C=99;
	protected static final int CODE_B=100;
	protected static final int CODE_A=101;
	/**结束符编码*/
	protected static final int CODE_STOP=106;
	/**校验字符通过MOD103算法*/
	protected static final int CHECK_CODE_MOD=103;
	/**结束位*/        
    protected static final String stop = "bbsssbbbsbsbb";
	/**
	 * 默认图片高度
	 */
	protected int height=30;
	/**空白设置*/
	protected int paddingLeft=1;
	protected int paddingRight=1;
	protected int paddingTop=1;
	protected int paddingBottom=1;
	/**空白地方透明*/
	protected boolean paddingHyaline=false;
	/***
	 * 默认图片格式
	 */
	protected String imageType="bmp";
	/**前景颜色*/
	private Color foregroundColor=Color.BLACK;
	
    /**一位占几个像素*/
    protected int bitPix=1;
	
	protected static String[] Code128List = new String[] { 
			"212222", "222122", "222221", "121223", "121322", "131222", "122213", "122312", "132212", "221213", "221312", "231212", "112232", "122132", "122231",
			"113222", "123122", "123221", "223211", "221132", "221231", "213212", "223112", "312131", "311222", "321122", "321221", "312212", "322112", "322211", "212123", "212321", "232121",
			"111323", "131123", "131321", "112313", "132113", "132311", "211313", "231113", "231311", "112133", "112331", "132131", "113123", "113321", "133121", "313121", "211331", "231131",
			"213113", "213311", "213131", "311123", "311321", "331121", "312113", "312311", "332111", "314111", "221411", "431111", "111224", "111422", "121124", "121421", "141122", "141221",
			"112214", "112412", "122114", "122411", "142112", "142211", "241211", "221114", "413111", "241112", "134111", "111242", "121142", "121241", "114212", "124112", "124211", "411212",
			"421112", "421211", "212141", "214121", "412121", "111143", "111341", "131141", "114113", "114311", "411113", "411311", "113141", "114131", "311141", "411131", "211412", "211214",
			"211232","211412","211214","211232","2331112"};
	/**
	 * 解析好的代码
	 * */
	protected String barcode;
	/**文本*/
	protected String text;
	/***
	 * 校验码
	 */
	protected int checkcode=0;
	
	public Code128(String text){
		this.text=StringUtils.trim(text);
	}
	/***
	 * 校验码
	 * @return
	 */
	protected abstract int getCheckValue();
	/***
	 * 起始编码
	 * @return
	 */
	protected abstract String getStart();
	/***
	 * 创建barcode代码
	 */
	protected void createBarcode()      
	{         
		checkcode=getCheckValue();
		//码头
		String start= getStart() ;
		//长形码      
		StringBand bodyCode=new StringBand(start);
		List<CodeSplit> splits=getSplit();
		logger.debug(splits);
		int count=1;
		boolean first=true;
		for(CodeSplit split:splits){
			//添加转换符  
			if(first){
				//第一个不用转换符
				first=false;
			}else{
				int shift=split.getShift(start);
				bodyCode.append(getOneTagCode(shift));
				checkcode += (shift*count++);    
			}
			//循环添加码身,计算码检         
			for (Integer index:split.getCodeIndex())            
			{                
				checkcode += (index * count++);      
				bodyCode.append(getOneTagCode(index));
			}
		}
		//码检值计算          
		String check = getOneTagCode(Double.valueOf(checkcode % CHECK_CODE_MOD).intValue());  
		bodyCode.append(check).append(stop);
		barcode=bodyCode.toString();        
	}
	/**
	 * 为了统一使用分段
	 * @return
	 */
	public abstract List<CodeSplit> getSplit();
	
	/**
	 * 增加一个条码标记
	 * @param CodeIndex
	 * @return
	 */
	protected String getOneTagCode(int CodeIndex) {
		String tag = Code128List[CodeIndex];
		return convert2SbCode(tag);
	}
	/***
	 * 转换成sb字符编码
	 * @param tag
	 * @return
	 */
	protected  String convert2SbCode(String tag){
		String res = Strings.BLANK;
		for (int i = 0; i < tag.length(); i++) {
			String temp = Strings.BLANK;
			int num = tag.charAt(i)-48;//转成数字
			if (i % 2 == 0) {
				temp = TagB[num];
			} else {
				temp = TagS[num];
			}
			res += temp;
		}
		return res;
	}
	/***
	 * 以默认高度写到文件中
	 * @param file
	 * @throws IOException
	 */
	public void writeToFile(String file) throws IOException{
		BufferedImage image=createImage(height);
		ImageIO.write(image, imageType, new File(file));
	}
	/**
	 * 以默认高度写入到流中
	 * @param os
	 * @throws IOException
	 */
	public void write(OutputStream os) throws IOException{
		BufferedImage image=createImage(height);
		ImageIO.write(image, imageType,os);
	}
	
	/***
	 * 生成图片
	 * @param height 高度
	 * @return
	 */
	public BufferedImage createImage(int height) {
		try {
			BufferedImage image;
			Graphics2D g ;
			char[] cs = barcode.toCharArray();
			if(paddingHyaline){
				ColorModel cm = ColorModel.getRGBdefault();
				WritableRaster wr = cm.createCompatibleWritableRaster(barcode.length()*bitPix+paddingLeft+paddingRight, height+paddingTop+paddingBottom);
				image = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
				g= image.createGraphics();
			}else{
				image=new BufferedImage(barcode.length()*bitPix+paddingLeft+paddingRight, height+paddingTop+paddingBottom, BufferedImage.TYPE_INT_BGR);
				g= image.createGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0,0, image.getWidth(), image.getHeight());
			}
			for (int i = 0; i < cs.length;i++) {
				if(cs[i] == 'b'){
					g.setColor(foregroundColor);
				}else{
					g.setColor(Color.WHITE);
				}
				g.fillRect(paddingLeft+i*bitPix, paddingTop, bitPix, height);
			}
			g.dispose();
			return image;
		} catch (Exception e) {
			throw new RuntimeException("生成barcode图片失败",e);
		}
	}
	/**
	 * 使用设置的高度生成图像
	 * @return
	 */
	public BufferedImage createImage(){
		return createImage(height);
	}
	/**
	 * 设置一位占几个像素
	 * @param bitPix
	 */
	public void setBitPix(int bitPix) {
		this.bitPix = bitPix;
	}
	/**
	 * 设置生成图片的高度
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * 设置前景色
	 * @param foregroundColor
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	
	@Override
	public String toString() {
		return getSplit().toString();
	}
	
	/**解析好的代码*/
	public String getBarCode(){
		return barcode;
	}
	
	
	/**设置空白地的透明色*/
	public void setPaddingHyaline(boolean paddingHyaline) {
		this.paddingHyaline = paddingHyaline;
	}
	
	public void setPadding(int padding) {
		this.paddingBottom = padding;
		this.paddingTop = padding;
		this.paddingLeft = padding;
		this.paddingRight = padding;
	}
	/***
	 * 设置左边的空白像素
	 * @param paddingLeft
	 */
	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	/**
	 * 设置右边的空白像素
	 * @param paddingRight
	 */
	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}
	/**
	 * 设置上面的空白像素
	 * @param paddingTop
	 */
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}
	/**
	 * 设置下面的空白像素
	 * @param paddingBottom
	 */
	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	/**
	 * 设置输出图片时的格式 [bmp,png]
	 * @param imageType
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
}
