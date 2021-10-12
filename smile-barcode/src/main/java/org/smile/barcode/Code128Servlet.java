package org.smile.barcode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smile.file.ContentType;
import org.smile.http.RequestUtils;
import org.smile.util.StringUtils;

public class Code128Servlet extends HttpServlet {
	
	protected static final String default_image_type="bmp";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CodeBuilder builder=new CodeBuilder();
		RequestUtils.requestToBean(request, builder);
		BufferedImage im=builder.buildImage();
		String image=builder.getImgType();
		if(StringUtils.isEmpty(image)){
			image=default_image_type;
		}
		response.setContentType(ContentType.getContextType(image));
		ImageIO.write(im,image,response.getOutputStream());
	}
	
}
