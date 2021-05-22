package org.smile.dataset;

import org.smile.beans.BeanUtils;
import org.smile.beans.PropertiesGetter;
import org.smile.beans.converter.BasicConverter;
import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.commons.SmileRunException;
import org.smile.util.StringUtils;

public abstract class AbstractDataResult implements DataResult,PropertiesGetter<String, Object> {

	@Override
	public String getString(int col) {
		Object value = get(col);
		if (value != null) {
			if (value instanceof String) {
				return (String) value;
			} else {
				return String.valueOf(value);
			}
		}
		return null;
	}

	@Override
	public Integer getInteger(int col) {
		Object value = get(col);
		if (StringUtils.isNotNull(value)) {
			if (value instanceof Integer) {
				return (Integer) value;
			} else if (value instanceof Number) {
				return ((Number) value).intValue();
			} else {
				return Integer.valueOf(String.valueOf(value));
			}
		}
		return null;
	}

	@Override
	public String getString(String column) {
		return getString(getColumnIndex(column));
	}

	@Override
	public Integer getInteger(String column) {
		return getInteger(getColumnIndex(column));
	}

	@Override
	public Long getLong(String column) {
		return getLong(getColumnIndex(column));
	}

	@Override
	public Short getShort(String column) {
		return getShort(getColumnIndex(column));
	}

	@Override
	public Double getDouble(String column) {
		return getDouble(getColumnIndex(column));
	}

	@Override
	public Float getFloat(String column) {
		return getFloat(getColumnIndex(column));
	}

	@Override
	public Long getLong(int column) {
		Object value = get(column);
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (value instanceof Long) {
			return (Long) value;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return Long.getLong(String.valueOf(value));
	}

	@Override
	public Short getShort(int column) {
		Object value = get(column);
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (value instanceof Short) {
			return (Short) value;
		}
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		return Short.parseShort(String.valueOf(value));
	}

	@Override
	public Double getDouble(int column) {
		Object value = get(column);
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return Double.parseDouble(String.valueOf(value));
	}

	@Override
	public Float getFloat(int column) {
		Object value = get(column);
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (value instanceof Float) {
			return (Float) value;
		}
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		return Float.parseFloat(String.valueOf(value));
	}

	@Override
	public Object getColumn(int column, Class type) {
		Object value=get(column);
		try {
			return BasicConverter.getInstance().convert( type,value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}
	
	@Override
	public Object getColumn(String column, Class type) {
		Object value=get(column);
		try {
			return BasicConverter.getInstance().convert( type,value);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}

	@Override
	public Object getValue(String name) {
		if(this.getColumnIndex(name)!=-1){
			return get(name);
		}
		try {
			return BeanUtils.getValue(this, name);
		} catch (BeanException e) {
			throw new SmileRunException(e);
		}
	}
	
	
}
