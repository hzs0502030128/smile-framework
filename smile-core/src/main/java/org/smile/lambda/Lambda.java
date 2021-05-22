package org.smile.lambda;

import java.io.Serializable;
import java.util.function.Function;

public interface Lambda<T,R> extends Function<T, R>,Serializable{
	
}
