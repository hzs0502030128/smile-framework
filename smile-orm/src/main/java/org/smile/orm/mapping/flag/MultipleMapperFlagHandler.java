package org.smile.orm.mapping.flag;

import java.lang.reflect.Field;
import java.util.Set;
/**
 * 用于多个配置同是存在的情况
 * @author 胡真山
 *
 */
public class MultipleMapperFlagHandler implements MapperFlagHandler {

	private Set<MapperFlagHandler> handlers;

	@Override
	public TableFlag getTableFlag(Class clazz) {
		TableFlag table = null;
		for (MapperFlagHandler h : handlers) {
			table = h.getTableFlag(clazz);
			if (table != null && table.isFlaged()) {
				return table;
			}
		}
		return null;
	}

	@Override
	public PropertyFlag getPropertyFlag(TableFlag tableFlag,Field field) {
		PropertyFlag property = null;
		for (MapperFlagHandler h : handlers) {
			property = h.getPropertyFlag(tableFlag,field);
			if (property != null && property.isFlaged()) {
				return property;
			}
		}
		return null;
	}

	public void setHandlers(Set<MapperFlagHandler> handlers) {
		this.handlers = handlers;
	}

}
