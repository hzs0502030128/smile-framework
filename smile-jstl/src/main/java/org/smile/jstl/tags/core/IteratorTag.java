
package org.smile.jstl.tags.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.smile.beans.converter.BeanException;
import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.type.StringArrayConverter;
import org.smile.jstl.tags.PropertySupport;
import org.smile.jstl.tags.TagUtils;
import org.smile.web.scope.ScopeUtils;

/**
 * Iterator tag for iterating collections.
 *
 * @see org.smile.jstl.tags.core.IteratorStatus
 */
public class IteratorTag extends SimpleTagSupport {

	protected Object items;
	protected String var;
	protected String status;
	protected int modulus = 2;
	protected String scope;
	protected int from;
	protected int count = -1;

	protected IteratorStatus iteratorStatus;

	/**
	 * Specifies item collection.
	 */
	public void setItems(Object items) {
		this.items = items;
	}

	/**
	 * Specifies variable name that will be used for item during iteration.
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Specifies status variable name. If omitted, status will not be used.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Specifies modulus value for the iterator status
	 */
	public void setModulus(int modulus) {
		this.modulus = modulus;
	}

	/**
	 * Sets scope for all variables.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Sets starting index.
	 */
	public void setFrom(int from) {
		this.from = from;
	}

	/**
	 * Sets count as total number of items to iterate.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void doTag() throws JspException {
		if (items == null) {
			return;
		}
		JspFragment body = getJspBody();
		if (body == null) {
			return;
		}
		PageContext pageContext = (PageContext) getJspContext();

		// create an iterator status if the status attribute was set.
		if (status != null) {
			iteratorStatus = new IteratorStatus(this.modulus);
			ScopeUtils.setScopeAttribute(status, iteratorStatus, scope, pageContext);
		}
		if(PropertySupport.isExpression(items)){
			try {
				items=PropertySupport.getExpressionValue((String)items, pageContext);
			} catch (BeanException e) {
				throw new JspException(e);
			}
		}
		if (items instanceof Collection) {
			iterateCollection((Collection) items, from, count, pageContext);
		} else if (items.getClass().isArray()) {
			iterateArray((Object[]) items, from, count, pageContext);
		} else if (items instanceof String) {
			try {
				iterateArray(new StringArrayConverter().convert(items), from, count, pageContext);
			} catch (ConvertException e) {
				throw new JspException(e);
			}
		}else if(items instanceof Map){
			iterateCollection(((Map)items).entrySet(), from, count, pageContext);
		} else {
			throw new JspException("Provided items are not iterable");
		}

		// cleanup
		if (status != null) {
			ScopeUtils.removeScopeAttribute(status, scope, pageContext);
		}
		ScopeUtils.removeScopeAttribute(var, scope, pageContext);
	}

	/**
	 * Calculates 'TO'.
	 */
	protected int calculateTo(int from, int count, int size) {
		int to = size;
		if (count != -1) {
			to = from + count;
			if (to > size) {
				to = size;
			}
		}
		return to;
	}

	/**
	 * Iterates collection.
	 */
	protected void iterateCollection(Collection collection, int from, int count, PageContext pageContext) throws JspException {
		JspFragment body = getJspBody();
		Iterator iter = collection.iterator();
		int i = 0;
		int to = calculateTo(from, count, collection.size());
		while (i < to) {
			Object item = iter.next();
			if (i >= from) {
				if (status != null) {
					iteratorStatus.next(!iter.hasNext());
				}
				ScopeUtils.setScopeAttribute(var, item, scope, pageContext);
				TagUtils.invokeBody(body);
			}
			i++;
		}
	}

	/**
	 * Iterates arrays.
	 */
	protected void iterateArray(Object[] array, int from, int count, PageContext pageContext) throws JspException {
		JspFragment body = getJspBody();
		int len = array.length;
		int to = calculateTo(from, count, len);
		int last = to - 1;
		for (int i = from; i < to; i++) {
			Object item = array[i];
			if (status != null) {
				iteratorStatus.next(i == last);
			}
			ScopeUtils.setScopeAttribute(var, item, scope, pageContext);
			TagUtils.invokeBody(body);
		}
	}

}
