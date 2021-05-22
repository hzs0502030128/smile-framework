package org.smile.db.criteria;

import java.io.Serializable;

public abstract class MatchMode implements Serializable {
	
	private final String name;

	protected MatchMode(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public static final MatchMode EXACT = new MatchMode("EXACT") {
		public String toMatchString(String pattern) {
			return pattern;
		}
	};
	public static final MatchMode START = new MatchMode("START") {
		public String toMatchString(String pattern) {
			return pattern + '%';
		}
	};
	public static final MatchMode END = new MatchMode("END") {
		public String toMatchString(String pattern) {
			return '%' + pattern;
		}
	};
	public static final MatchMode ANYWHERE = new MatchMode("ANYWHERE") {
		public String toMatchString(String pattern) {
			return '%' + pattern + '%';
		}
	};

	public abstract String toMatchString(String paramString);
}