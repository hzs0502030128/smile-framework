package org.smile.json;

import org.smile.io.buff.CharBand;
import org.smile.json.format.SerializeConfig;

public class JSONStringWriter extends JSONWriter{

	/**写入的字符内容*/
	protected CharBand chars=new CharBand();
	
	public JSONStringWriter(SerializeConfig config){
		super(config);
	}
	
	@Override
	public JSONWriter write(String str) {
		chars.append(str);
		return this;
	}

	@Override
	public JSONWriter write(char chr) {
		chars.append(chr);
		return this;
	}

	@Override
	public JSONWriter write(CharSequence cs) {
		chars.append(cs);
		return this;
	}

	@Override
	public String toJSONString() {
		return chars.toString();
	}

	@Override
	public String toString() {
		return chars.toString();
	}

	@Override
	public JSONWriter writeDouble(Double d) {
		chars.append(d.toString());
		return this;
	}

	@Override
	public JSONWriter writeFloat(Float f) {
		chars.append(f.toString());
		return this;
	}

}
