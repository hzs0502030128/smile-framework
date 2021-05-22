package org.smile.json;

import java.io.IOException;
import java.io.Writer;

import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.json.format.SerializeConfig;

public class JSONIOWriter extends JSONWriter{

	protected Writer writer;
	
	public JSONIOWriter(Writer writer){
		super();
		this.writer=writer;
	}
	
	public JSONIOWriter(Writer writer,SerializeConfig config){
		super(config);
		this.writer=writer;
	}
	
	@Override
	public JSONWriter write(String str) {
		try {
			writer.write(str);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return this;
	}

	@Override
	public JSONWriter write(char chr) {
		try {
			writer.write(chr);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return this;
	}

	@Override
	public JSONWriter write(CharSequence cs) {
		try {
			writer.append(cs);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
		return this;
	}

	@Override
	public String toJSONString() {
		throw new NotImplementedException("unsupport this method ,you can use "+JSONStringWriter.class.getName());
	}

	@Override
	public void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public JSONWriter writeDouble(Double d) {
		write(d.toString());
		return this;
	}

	@Override
	public JSONWriter writeFloat(Float f) {
		return write(f.toString());
	}

	
}
