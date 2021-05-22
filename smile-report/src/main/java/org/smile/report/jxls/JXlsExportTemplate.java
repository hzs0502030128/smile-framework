package org.smile.report.jxls;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.report.XlsExportTemplateSupport;
/**
 * 使用jxls类库实现
 * @author 胡真山
 *
 */
public class JXlsExportTemplate implements XlsExportTemplateSupport{
	
	private InputStream inputStream;
	
	private OutputStream outPutStream;
	
	public JXlsExportTemplate(OutputStream outPutStream){
		this.outPutStream=outPutStream;
	}
	
	@Override
	public void loadXlsTemplate(String fileName) throws IOException {
		inputStream=new FileInputStream(fileName);
	}

	@Override
	public void fillDataSource(Collection<?> dataList) {
		Context context = new Context();
		context.putVar(MAIN_DATA_SOURCE_KEY, dataList);
		fillDataSource(context);
	}

	@Override
	public void fillDataSource(Map<String,Object> dataMap) {
		Context context = new Context();
		for(Map.Entry<String, Object> entry:dataMap.entrySet()){
			context.putVar(entry.getKey(), entry.getValue());
		}
		fillDataSource(context);
	}
	
	
	public void fillDataSource(Context context){
		try {
			JxlsHelper.getInstance().processTemplate(inputStream, outPutStream, context);
			outPutStream.flush();
		} catch (IOException e) {
			throw new SmileRunException("导出excel失败", e);
		}finally{
			IOUtils.close(inputStream);
			IOUtils.close(outPutStream);
		}
	}

}
