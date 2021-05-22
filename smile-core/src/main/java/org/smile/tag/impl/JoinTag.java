package org.smile.tag.impl;

import org.smile.commons.ann.Attribute;
import org.smile.util.StringUtils;

/**
 * 用于连接集合、数组、map
 * 
 * <c:join open="(" close=")" items="${list}" separator="," status="status"  var="stu">
 *	'${stu.name}'
 *	</c:join>
 * @author 胡真山
 *
 */
public class JoinTag extends IteratorTag{
	@Attribute
	protected String open;
	@Attribute
	protected String separator=",";
	@Attribute
	protected String close;
	
	@Override
	public void doTag() throws Exception {
		if(StringUtils.notEmpty(open)) {
			this.tagContext.getWriter().write(open);
		}
		super.doTag();
		if(StringUtils.notEmpty(close)) {
			this.tagContext.getWriter().write(close);
		}
	}
	@Override
	public void invokeBody() throws Exception {
		super.invokeBody();
		if(StringUtils.notEmpty(separator)) {
			if(iteratorStatus!=null&&!this.iteratorStatus.isLast()) {
				this.tagContext.getWriter().write(separator);
			}
		}
	}
	
}
