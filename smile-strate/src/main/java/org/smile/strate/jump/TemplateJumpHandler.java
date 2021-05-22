package org.smile.strate.jump;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.smile.beans.PropertyHandler;
import org.smile.beans.PropertyHandlers;
import org.smile.beans.converter.BeanException;
import org.smile.collection.BaseMapEntry;
import org.smile.commons.NotImplementedException;
import org.smile.commons.SmileRunException;
import org.smile.io.IOUtils;
import org.smile.strate.Strate;
import org.smile.strate.action.Action;
import org.smile.strate.action.ActionContext;
import org.smile.strate.config.ResultConfig;
import org.smile.strate.dispatch.DispatchContext;
import org.smile.template.Configuration;
import org.smile.template.FileTemplateLoader;
import org.smile.template.HashTemplateModel;
import org.smile.template.Template;

public class TemplateJumpHandler implements JumpHandler {

	protected Configuration configuration;

	public TemplateJumpHandler() {
		String baseDir = ActionContext.getServletContext().getRealPath("/");
		FileTemplateLoader loader = new FileTemplateLoader(new File(baseDir));
		configuration = new Configuration(loader);
		configuration.setEncode(Strate.encoding);
		configuration.setTemplateType(Template.SMILE);
	}

	@Override
	public void jump(DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		jump(new ActionModel(context.getAction()), context, forward);
	}

	@Override
	public void jump(Object methodResult,DispatchContext context, ResultConfig forward) throws StrateResultJumpException {
		Writer writer = null;
		try {
			Template template = configuration.getTemplate(forward.getValue());
			writer = new OutputStreamWriter(context.getResponse().getOutputStream(), Strate.encoding);
			template.process(methodResult, writer);
		} catch (Exception e) {
			throw new StrateResultJumpException("process template error," + forward.getName() + "-" + forward.getValue(), e);
		} finally {
			IOUtils.close(writer);
		}
	}

	class ActionModel extends HashTemplateModel {

		/** 当前的action对象 */
		Action action;

		PropertyHandler propertyHandler;
		/*** 当前的请求对象 */
		HttpServletRequest request;
		static final String ACTION = "action";
		static final String REQUEST = "request";
		static final String RESPONSE = "response";

		ActionModel(Action action) {
			this.action = action;
			this.propertyHandler = PropertyHandlers.getHanlder(action.getClass(), false);
			this.request = action.request();
		}

		@Override
		public Object put(String key, Object value) {
			request.setAttribute(key, value);
			return null;
		}

		@Override
		public Set<Entry<String, Object>> entrySet() {
			return new AbstractSet<Map.Entry<String, Object>>() {
				@Override
				public Iterator<Map.Entry<String, Object>> iterator() {
					return new Iterator<Map.Entry<String, Object>>() {
						Enumeration<String> enumeration = action.request().getAttributeNames();

						@Override
						public boolean hasNext() {
							return enumeration.hasMoreElements();
						}

						@Override
						public Map.Entry<String, Object> next() {
							String key = enumeration.nextElement();
							Object value = action.request().getAttribute(key);
							BaseMapEntry entry = new BaseMapEntry();
							entry.setKey(key);
							entry.setValue(value);
							return entry;
						}

						@Override
						public void remove() {
							throw new NotImplementedException();
						}
					};
				}

				@Override
				public int size() {
					throw new NotImplementedException();
				}
			};
		}

		@Override
		protected Object getModelValue(String key) {
			try {
				switch (key) {
				case ACTION:
					return action;
				case REQUEST:
					return request;
				case RESPONSE:
					return action.response();
				default:
					Object value = propertyHandler.getExpFieldValue(action, key);
					if (value == null) {
						value = action.request().getAttribute(key);
					}
					return value;
				}
			} catch (BeanException e) {
				throw new SmileRunException(e);
			}
		}
	}

}
