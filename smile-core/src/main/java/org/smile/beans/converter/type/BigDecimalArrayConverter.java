package org.smile.beans.converter.type;

import java.math.BigDecimal;

public class BigDecimalArrayConverter extends ArrayConverter<BigDecimal>{

	@Override
	protected BigDecimal[] defaultValue(int len) {
		return new BigDecimal[len];
	}

	@Override
	public Class<BigDecimal[]> getType() {
		return BigDecimal[].class;
	}

	@Override
	protected Class<BigDecimal> getOneType() {
		return BigDecimal.class;
	}

	
	
}
