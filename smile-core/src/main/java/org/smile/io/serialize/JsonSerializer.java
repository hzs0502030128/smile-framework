package org.smile.io.serialize;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.smile.commons.Strings;
import org.smile.io.ByteArrayOutputStream;
import org.smile.json.JSONAware;
import org.smile.json.JSONIOWriter;
import org.smile.json.JSONSerializer;
import org.smile.json.JSONWriter;
import org.smile.json.parser.JSONParseException;

public class JsonSerializer implements Serializer<JSONAware>{
	
	protected String charset=Strings.UTF_8;
	
	private JSONSerializer jsonEncoder=new JSONSerializer();

	@Override
	public JSONAware deserialize(byte[] datas) throws SerializeException {
		try {
			String jsonstr=new String(datas,charset);
			return jsonEncoder.parse(jsonstr);
		} catch (UnsupportedEncodingException e) {
			throw new SerializeException(e);
		} catch (JSONParseException e) {
			throw new SerializeException(e);
		}
	}

	@Override
	public byte[] serialize(JSONAware obj) throws SerializeException {
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		JSONWriter writer=new JSONIOWriter(new OutputStreamWriter(os));
		jsonEncoder.write(writer, obj);
		return os.toArray();
	}

	@Override
	public void setLoader(ClassLoader loader) {
		
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setJsonEncoder(JSONSerializer jsonEncoder) {
		this.jsonEncoder = jsonEncoder;
	}
	
}
