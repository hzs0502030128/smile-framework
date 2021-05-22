package org.smile.collection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.smile.json.JSONArray;
import org.smile.json.JSONObject;
/**
 * 把一个map装饰成一个ResultMap
 * @author 胡真山
 *
 */
public class DecoratedResultMap extends AbstractMapDecorator<String, Object> implements ResultMap{

	protected DecoratedResultMap(Map<String, Object> map) {
		super(map);
	}

	@Override
	public Long getLong(String key) {
		return MapUtils.getLong(this.realMap, key);
	}

	@Override
	public Float getFloat(String key) {
		return MapUtils.getFloat(this.realMap, key);
	}

	@Override
	public Double getDouble(String key) {
		return MapUtils.getDouble(this.realMap, key);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return MapUtils.getBigDecimal(this.realMap, key);
	}

	@Override
	public String getString(String key) {
		return MapUtils.getString(this.realMap, key);
	}

	@Override
	public Date getDate(String key) {
		return MapUtils.getDate(this.realMap, key);
	}

	@Override
	public Integer getInt(String key) {
		return MapUtils.getInt(this.realMap, key);
	}

	@Override
	public Byte getByte(String key) {
		return MapUtils.getByte(this.realMap, key);
	}

	@Override
	public Short getShort(String key) {
		return MapUtils.getShort(this.realMap, key);
	}

	@Override
	public <T> T get(String key, Class<T> resultClass) {
		return MapUtils.getObject(this.realMap, key,resultClass);
	}

	@Override
	public JSONObject getJSONObject(String key) {
		return MapUtils.getJSONObject(this.realMap, key);
	}

	@Override
	public JSONArray getJSONArray(String key) {
		return MapUtils.getJSONArray(this.realMap, key);
	}

	@Override
	public Boolean getBoolean(String key) {
		return MapUtils.getBoolean(this.realMap, key);
	}

}
