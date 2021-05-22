package org.smile.report.excel.pdf;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.smile.report.poi.PoiSupport;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.ExtendedColor;
import com.lowagie.text.pdf.PdfPCell;
/**
 * 样式 转换工具
 * @author 胡真山
 *
 */
public class StyleConvertUtils {
	/**excel自动颜色*/
	private static final String autoColor = "0:0:0";

	/**
	 * 同步单元格样式 
	 * @param cell
	 * @param pdfcell
	 */
	public static void syncStyle(PdfPageSetting pageSetting,Cell cell, PdfPCell pdfcell) {
		syncAlign(cell, pdfcell);
		background(pageSetting,cell, pdfcell);
		synHeight(pageSetting, cell, pdfcell);
		font(pageSetting,cell, pdfcell);
	}
	
	
	public static void synHeight(PdfPageSetting pageSetting,Cell cell, PdfPCell pdfcell){
		//设置高度
		float h=cell.getRow().getHeight();
		h=PoiSupport.poi2PixelHeight(h)*pageSetting.getHeightRate();
		pdfcell.setFixedHeight(h);
	}
	/**
	 * 对其方式
	 * @param cell
	 * @param pdfcell
	 */
	public static void syncAlign(Cell cell, PdfPCell pdfcell) {
		CellStyle style = cell.getCellStyle();
		switch (style.getAlignment()) {
		case CellStyle.ALIGN_GENERAL:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_UNDEFINED);
			break;
		case CellStyle.ALIGN_LEFT:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			break;
		case CellStyle.ALIGN_CENTER:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			break;
		case CellStyle.ALIGN_RIGHT:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			break;
		case CellStyle.ALIGN_FILL:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED_ALL);
			break;
		case CellStyle.ALIGN_JUSTIFY:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
			break;
		case CellStyle.ALIGN_CENTER_SELECTION:
			pdfcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			break;
		default:
			break;
		}
		switch (style.getVerticalAlignment()) {
		case CellStyle.VERTICAL_BOTTOM:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			break;
		case CellStyle.VERTICAL_CENTER:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			break;
		case CellStyle.VERTICAL_JUSTIFY:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_BASELINE);
			break;
		case CellStyle.VERTICAL_TOP:
			pdfcell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			break;
		default:
			break;
		}
	}
	/**
	 * 背景色
	 * @param cell
	 * @param pdfcell
	 */
	public static void background(PdfPageSetting pageSetting,Cell cell, PdfPCell pdfcell) {
		CellStyle style = cell.getCellStyle();
		Color color = (Color) style.getFillForegroundColorColor();
		java.awt.Color baseColor = null;
		if (color != null) {
			if (color instanceof HSSFColor) {
				short[] hex = ((HSSFColor) color).getTriplet();
				if (!autoColor.equals(((HSSFColor) color).getHexString())) {
					baseColor = new java.awt.Color(ExtendedColor.TYPE_RGB,hex[0], hex[1], hex[2]);
					pdfcell.setBackgroundColor(baseColor);
				}
			} else if (color instanceof XSSFColor) {
				String hex = ((XSSFColor) color).getARGBHex();
				baseColor = new java.awt.Color(Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16), Integer.parseInt(hex.substring(6, 8), 16));
				pdfcell.setBackgroundColor(baseColor);
			}
			
		}
		//边框
		short leftBorder=style.getBorderLeft();
		pdfcell.setBorderWidthLeft(leftBorder/2f);
		short rightBorder=style.getBorderRight();
		pdfcell.setBorderWidthRight(rightBorder/2f);
		short topBorder=style.getBorderTop();
		pdfcell.setBorderWidthTop(topBorder/2f);
		short bottomBorder=style.getBorderBottom();
		pdfcell.setBorderWidthBottom(bottomBorder/2f);
	}
	/**
	 * 字体
	 * @param cell
	 * @param pdfcell
	 */
	public static void font(PdfPageSetting pageSetting,Cell cell, PdfPCell pdfcell) {
		CellStyle style = cell.getCellStyle();
		Workbook wk = cell.getSheet().getWorkbook();
		org.apache.poi.ss.usermodel.Font font = wk.getFontAt(style.getFontIndex());
		Font pdfFont = pdfcell.getPhrase().getFont();
		// set color 字体颜色
		pdfFont.setColor(createdColor(wk, font));
		// set style 字体样式
		pdfFont.setStyle(createdFontStyle(font));
		// set size  字体大小
		pdfFont.setSize(font.getFontHeightInPoints()*pageSetting.getHeightRate());
	}

	// 根据font转换为pdf的颜色
	private static java.awt.Color createdColor(Workbook wb, org.apache.poi.ss.usermodel.Font font) {
		if (font instanceof HSSFFont) {
			HSSFColor color = ((HSSFFont) font).getHSSFColor((HSSFWorkbook) wb);
			java.awt.Color baseColor;
			if (color != null) {
				short[] hex = ((HSSFColor) color).getTriplet();
				if (autoColor.equals(((HSSFColor) color).getHexString())) {
					hex[0] = 0;
					hex[1] = 0;
					hex[2] = 0;
				}
				baseColor = new java.awt.Color(hex[0], hex[1], hex[2]);
			} else {
				baseColor = new java.awt.Color(0, 0, 0);
			}
			return baseColor;
		} else {
			XSSFColor color = ((XSSFFont) font).getXSSFColor();
			java.awt.Color baseColor;
			if (color != null) {
				String hex = ((XSSFColor) color).getARGBHex();
				baseColor = new java.awt.Color(Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16), Integer.parseInt(hex.substring(6, 8), 16));
			} else {
				baseColor = new java.awt.Color(0, 0, 0);
			}
			return baseColor;
		}

	}

	// 根据font转换为pdf的font
	private static int createdFontStyle(org.apache.poi.ss.usermodel.Font font) {
		int style = Font.NORMAL;
		switch (font.getBoldweight()) {
			case org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD:
				style = Font.BOLD;
				if (font.getItalic()) {
					style |= Font.ITALIC;
				}
				if (font.getStrikeout()) {
					style |= Font.STRIKETHRU;
				}
				if (font.getUnderline() != org.apache.poi.ss.usermodel.Font.U_NONE) {
					style |= Font.UNDERLINE;
				}
				break;
			case org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_NORMAL:
				style = Font.NORMAL;
				if (font.getItalic()) {
					style |= Font.ITALIC;
				}
				if (font.getStrikeout()) {
					style |= Font.STRIKETHRU;
				}
				if (font.getUnderline() != org.apache.poi.ss.usermodel.Font.U_NONE) {
					style |= Font.UNDERLINE;
				}
				break;
			default: style=Font.NORMAL;
		}
		return style;
	}
}
