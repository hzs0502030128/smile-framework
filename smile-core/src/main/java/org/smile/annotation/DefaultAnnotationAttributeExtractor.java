package org.smile.annotation;

/*
 * Copyright 2002-2015 the original author or authors.
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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.smile.reflect.MethodUtils;


/**
 * Default implementation of the {@link AnnotationAttributeExtractor} strategy
 * that is backed by an {@link Annotation}.
 *
 * @author Sam Brannen
 * @since 4.2
 * @see Annotation
 * @see AliasFor
 * @see AbstractAliasAwareAnnotationAttributeExtractor
 * @see MapAnnotationAttributeExtractor
 * @see AnnotationUtils#synthesizeAnnotation(Annotation, AnnotatedElement)
 */
public class DefaultAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor<Annotation> {

	/**
	 * Construct a new {@code DefaultAnnotationAttributeExtractor}.
	 * @param annotation the annotation to synthesize; never {@code null}
	 * @param annotatedElement the element that is annotated with the supplied
	 * annotation; may be {@code null} if unknown
	 */
	DefaultAnnotationAttributeExtractor(Annotation annotation, Object annotatedElement,OverWriteAnnotation overwriter) {
		super(annotation.annotationType(), annotatedElement, annotation,overwriter);
	}


	@Override
	protected Object getRawAttributeValue(Method attributeMethod) {
		MethodUtils.makeAccessible(attributeMethod);
		return MethodUtils.invoke(getSource(),attributeMethod);
	}

	@Override
	protected Object getRawAttributeValue(String attributeName) {
		Method attributeMethod = MethodUtils.findMethod(getAnnotationType(), attributeName);
		return (attributeMethod != null ? getRawAttributeValue(attributeMethod) : null);
	}

}

