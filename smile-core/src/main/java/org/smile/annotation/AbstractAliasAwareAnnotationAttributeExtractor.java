package org.smile.annotation;

/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.smile.annotation.AnnotationUtils.ExtendsAttribute;
import org.smile.commons.Assert;
import org.smile.util.Objects;

/**
 * Abstract base class for {@link AnnotationAttributeExtractor} implementations
 * that transparently enforce attribute alias semantics for annotation
 * attributes that are annotated with {@link AliasFor @AliasFor}.
 *
 * @author Sam Brannen
 * @since 4.2
 * @param <S> the type of source supported by this extractor
 * @see Annotation
 * @see AliasFor
 * @see AnnotationUtils#synthesizeAnnotation(Annotation, Object)
 */
public abstract class AbstractAliasAwareAnnotationAttributeExtractor<S> implements AnnotationAttributeExtractor<S> {

	private final Class<? extends Annotation> annotationType;

	private final Object annotatedElement;

	private final S source;

	private final Map<String, List<String>> attributeAliasMap;
	
	private final OverWriteAnnotation overWriter;
	/**
	 * 从父注解中继承的属性
	 */
	private final Map<String,ExtendsAttribute> superAttributes;
	


	/**
	 * Construct a new {@code AbstractAliasAwareAnnotationAttributeExtractor}.
	 * @param annotationType the annotation type to synthesize; never {@code null}
	 * @param annotatedElement the element that is annotated with the annotation
	 * of the supplied type; may be {@code null} if unknown
	 * @param source the underlying source of annotation attributes; never {@code null}
	 */
	AbstractAliasAwareAnnotationAttributeExtractor(
			Class<? extends Annotation> annotationType, Object annotatedElement, S source,OverWriteAnnotation overWriter) {

		Assert.notNull(annotationType, "annotationType must not be null");
		Assert.notNull(source, "source must not be null");
		this.annotationType = annotationType;
		this.annotatedElement = annotatedElement;
		this.source = source;
		this.attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
		this.overWriter=overWriter;
		if(source instanceof Annotation) {
			this.superAttributes=AnnotationUtils.isOverrideSuper((Annotation)source);
		}else {
			superAttributes=null;
		}
	}
	
	


	@Override
	public final Class<? extends Annotation> getAnnotationType() {
		return this.annotationType;
	}

	@Override
	public final Object getAnnotatedElement() {
		return this.annotatedElement;
	}

	@Override
	public final S getSource() {
		return this.source;
	}

	@Override
	public final Object getAttributeValue(Method attributeMethod) {
		String attributeName = attributeMethod.getName();
		Object defaultValue =AnnotationUtils.getDefaultValue(this.annotationType, attributeName);
		Object attributeValue =null;
		if(this.overWriter!=null&&this.overWriter.isOverWrite(attributeName)) {
			attributeValue=this.overWriter.invoke(attributeName);
			if(Objects.notEquals(attributeValue,defaultValue)) {
				return attributeValue;
			}
		}
		attributeValue=	getRawAttributeValue(attributeMethod);
		List<String> aliasNames = this.attributeAliasMap.get(attributeName);
		if (aliasNames != null) {
			for (String aliasName : aliasNames) {
				Object aliasValue = getRawAttributeValue(aliasName);
				if(Objects.equals(aliasName, defaultValue)) {
					Object extendsAliasValue=extendsAliasValue(attributeName);
					if(extendsAliasValue!=null) {
						return extendsAliasValue;
					}
				}
				if (!Objects.equals(attributeValue, aliasValue) &&
						!Objects.equals(attributeValue, defaultValue) &&
						!Objects.equals(aliasValue, defaultValue)) {
					String elementName = (this.annotatedElement != null ? this.annotatedElement.toString() : "unknown element");
					throw new AnnotationException(String.format(
							"In annotation [%s] declared on %s and synthesized from [%s], attribute '%s' and its " +
							"alias '%s' are present with values of [%s] and [%s], but only one is permitted.",
							this.annotationType.getName(), elementName, this.source, attributeName, aliasName,
							Objects.nullSafeToString(attributeValue), Objects.nullSafeToString(aliasValue)));
				}

				// If the user didn't declare the annotation with an explicit value,
				// use the value of the alias instead.
				if (Objects.nullSafeEquals(attributeValue, defaultValue)) {
					attributeValue = aliasValue;
				}
			}
		}
		if(Objects.equals(attributeValue,defaultValue)){//继承属性
			Object extendsAliasValue=extendsAliasValue(attributeName);
			if(extendsAliasValue!=null) {
				attributeValue=extendsAliasValue;
			}
		}
		return attributeValue;
	}

	private Object extendsAliasValue(String attributeName) {
		//看有没有继承
		if(this.superAttributes!=null) {
			ExtendsAttribute extend=superAttributes.get(attributeName);
			if(extend!=null) {
				Object aliasValue=extend.invoke();
				Object defaultV=AnnotationUtils.getDefaultValue(this.annotationType, attributeName);
				if(Objects.notEquals(aliasValue, defaultV)) {
					return aliasValue;
				}
			}
		}
		return null;
	}

	/**
	 * Get the raw, unmodified attribute value from the underlying
	 * {@linkplain #getSource source} that corresponds to the supplied
	 * attribute method.
	 */
	protected abstract Object getRawAttributeValue(Method attributeMethod);

	/**
	 * Get the raw, unmodified attribute value from the underlying
	 * {@linkplain #getSource source} that corresponds to the supplied
	 * attribute name.
	 */
	protected abstract Object getRawAttributeValue(String attributeName);

}

