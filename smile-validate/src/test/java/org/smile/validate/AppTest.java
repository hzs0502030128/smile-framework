package org.smile.validate;

import org.junit.Before;
import org.junit.Test;
import org.smile.collection.CollectionUtils;
import org.smile.validate.rule.DefaultRule;

/**
 * Unit test for simple App.
 */
public class AppTest {
	Rule r;

	@Before
	public void before() {
		DefaultRule rule = new DefaultRule();
		rule.setType(ValidateType.required);
		rule.setFieldName("name");
		r = rule;
	}

	@Test
	public void shouldAnswerWithTrue() {
		ValidateElement ele = new ValidateElement(new Rule[] { r });
		CommonValidateSupport vs = new CommonValidateSupport().setTarget(CollectionUtils.hashMap("name", null));
		if (!ele.validate(vs)) {
			System.out.println(vs.getErrors());
		}
	}
}
