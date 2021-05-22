package org.smile.web.scope;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScopeMap extends AbstractMap<String, Object> implements ScopeSupport {

	protected ScopeSupport scope;

	protected Set<Entry<String, Object>> entries;

	public ScopeMap(ScopeSupport scope) {
		this.scope = scope;
	}

	@Override
	public Object getAttribute(String key) {
		return get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		put(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		remove(key);
	}

	@Override
	public Object get(Object key) {
		synchronized (scope) {
			return this.scope.getAttribute(key.toString());
		}
	}

	@Override
	public Object put(String key, Object value) {
		synchronized (scope) {
			Object oldValue = get(key);
			this.entries = null;
			this.scope.setAttribute(key.toString(), value);
			return oldValue;
		}
	}

	@Override
	public Object remove(Object key) {
		synchronized (scope) {
			this.entries = null;
			Object value = get(key);
			this.scope.removeAttribute(key.toString());
			return value;
		}
	}

	@Override
	public void clear() {
		synchronized (scope) {
			entries = null;
			Enumeration attributeNamesEnum = this.scope.getAttributes();
			while (attributeNamesEnum.hasMoreElements()){
				this.scope.removeAttribute((String) attributeNamesEnum.nextElement());
			}
		}
	}

	@Override
	public Enumeration<String> getAttributes() {
		synchronized (scope) {
			return scope.getAttributes();
		}
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		synchronized (scope) {
			if (this.entries == null) {
				this.entries = new HashSet();

				Enumeration enumeration = this.scope.getAttributes();

				while (enumeration.hasMoreElements()) {
					final String key = enumeration.nextElement().toString();
					final Object value = this.scope.getAttribute(key);
					this.entries.add(new Entry() {
						public boolean equals(Object obj) {
							if (!(obj instanceof Map.Entry)) {
								return false;
							}
							Entry entry = (Entry) obj;

							return (key == null ? entry.getKey() == null : key.equals(entry.getKey())) && (value == null ? entry.getValue() == null : value.equals(entry.getValue()));
						}

						public int hashCode() {
							return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
						}

						public String getKey() {
							return key;
						}

						public Object getValue() {
							return value;
						}

						public Object setValue(Object obj) {
							scope.setAttribute(key, obj);
							return value;
						}
					});
				}
			}
		}
		return this.entries;
	}

}
