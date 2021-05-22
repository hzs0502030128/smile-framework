package org.smile.annotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smile.collection.SoftHashMap;
import org.smile.commons.BridgeMethodResolver;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;
import org.smile.reflect.ClassTypeUtils;
import org.smile.reflect.MethodUtils;
import org.smile.util.Objects;
import org.smile.util.StringUtils;



/**
 * General utility methods for working with annotations, handling meta-annotations,
 * bridge methods (which the compiler generates for generic declarations) as well
 * as super methods (for optional <em>annotation inheritance</em>).
 *
 * <p>Note that most of the features of this class are not provided by the
 * JDK's introspection facilities themselves.
 *
 * <p>As a general rule for runtime-retained annotations (e.g. for transaction
 * control, authorization, or service exposure), always use the lookup methods
 * on this class (e.g., {@link #findAnnotation(Method, Class)},
 * {@link #getAnnotation(Method, Class)}, and {@link #getAnnotations(Method)})
 * instead of the plain annotation lookup methods in the JDK. You can still
 * explicitly choose between a <em>get</em> lookup on the given class level only
 * ({@link #getAnnotation(Method, Class)}) and a <em>find</em> lookup in the entire
 * inheritance hierarchy of the given method ({@link #findAnnotation(Method, Class)}).
 *
 * <h3>Terminology</h3>
 * The terms <em>directly present</em>, <em>indirectly present</em>, and
 * <em>present</em> have the same meanings as defined in the class-level
 * javadoc for {@link AnnotatedElement} (in Java 8).
 *
 * <p>An annotation is <em>meta-present</em> on an element if the annotation
 * is declared as a meta-annotation on some other annotation which is
 * <em>present</em> on the element. Annotation {@code A} is <em>meta-present</em>
 * on another annotation if {@code A} is either <em>directly present</em> or
 * <em>meta-present</em> on the other annotation.
 *
 * <h3>Meta-annotation Support</h3>
 * <p>Most {@code find*()} methods and some {@code get*()} methods in this class
 * provide support for finding annotations used as meta-annotations. Consult the
 * javadoc for each method in this class for details. For fine-grained support for
 * meta-annotations with <em>attribute overrides</em> in <em>composed annotations</em>,
 * consider using {@link AnnotatedElementUtils}'s more specific methods instead.
 *
 * <h3>Attribute Aliases</h3>
 * <p>All public methods in this class that return annotations, arrays of
 * annotations, or {@link AnnotationAttributes} transparently support attribute
 * aliases configured via {@link AliasFor @AliasFor}. Consult the various
 * {@code synthesizeAnnotation*(..)} methods for details.
 *
 * <h3>Search Scope</h3>
 * <p>The search algorithms used by methods in this class stop searching for
 * an annotation once the first annotation of the specified type has been
 * found. As a consequence, additional annotations of the specified type will
 * be silently ignored.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Mark Fisher
 * @author Chris Beams
 * @author Phillip Webb
 * @author Oleg Zhurakousky
 * @since 2.0
 * @see AliasFor
 * @see AnnotationAttributes
 * @see AnnotatedElementUtils
 * @see BridgeMethodResolver
 * @see java.lang.reflect.AnnotatedElement#getAnnotations()
 * @see java.lang.reflect.AnnotatedElement#getAnnotation(Class)
 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotations()
 */
public abstract class AnnotationUtils {
	//用来标识没有注解
	private static NullAnnotation NULL =ExtendsAttribute.class.getAnnotation(NullAnnotation.class);

	/**
	 * The attribute name for annotations with a single element.
	 */
	public static final String VALUE = "value";
	/***
	 * 原spring实现的方法，没有对此进行修改
	 */
	private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache = SoftHashMap.newConcurrentInstance();
	/**
	 * 对spring源方法进行过修改
	 * 子注解会对交注解的属性内容进行重写替换
	 * 对不子注解属性内容没有设置值的时候会继承父注解中设置了内容的属性
	 * 例如
	 * @Ann3(name = "name123",value = "value123")
		public @interface Ann2 {
			@AliasFor(annotation = Ann3.class)
			public String text() default "";
			@AliasFor(annotation = Ann3.class)
			public String name() default "";
		}
		
		@Ann2(text = "ann2Text123")
		public @interface Ann1 {
			public String attr() default "";
			@AliasFor(annotation = Ann2.class)
			public String text() default "";
			@AliasFor(annotation = Ann2.class)
			public String name() default "";
		}

		@Ann1(attr = "attr123",name="李白")

		此时获取Ann1的 text 则会是 ann2Text123  (继承)
		
		Ann3 的text 也是 ann2Text123 (重写)

	 */
	private static final Map<AnnotationCacheKey, Annotation> getAnnotationCache = SoftHashMap.newConcurrentInstance();
	
	private static final Map<AnnotationCacheKey, Boolean> metaPresentCache =SoftHashMap.newConcurrentInstance();

	private static final Map<Class<?>, Set<Method>> annotatedBaseTypeCache =SoftHashMap.newConcurrentInstance();

	private static final Map<Class<? extends Annotation>, Boolean> synthesizableCache =SoftHashMap.newConcurrentInstance();

	private static final Map<Class<? extends Annotation>, Map<String, List<String>>> attributeAliasesCache =SoftHashMap.newConcurrentInstance();

	private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache =SoftHashMap.newConcurrentInstance();

	/**别名描述缓存*/
	private static final Map<Method, AliasDescriptor> aliasDescriptorCache =SoftHashMap.newConcurrentInstance();

	private static transient Logger logger=LoggerFactory.getLogger(AnnotationUtils.class);


	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the supplied
	 * annotation: either the given annotation itself or a direct meta-annotation
	 * thereof.
	 * <p>Note that this method supports only a single level of meta-annotations.
	 * For support for arbitrary levels of meta-annotations, use one of the
	 * {@code find*()} methods instead.
	 * @param annotation the Annotation to check
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @return the first matching annotation, or {@code null} if not found
	 * @since 4.0
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
		if (annotationType.isInstance(annotation)) {
			return synthesizeAnnotation((A) annotation);
		}
		Class<? extends Annotation> annotatedElement = annotation.annotationType();
		try {
			A metaAnn = annotatedElement.getAnnotation(annotationType);
			return metaAnn;
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}

	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the supplied
	 * {@link AnnotatedElement}, where the annotation is either <em>present</em> or
	 * <em>meta-present</em> on the {@code AnnotatedElement}.
	 * <p>Note that this method supports only a single level of meta-annotations.
	 * For support for arbitrary levels of meta-annotations, use
	 * {@link #findAnnotation(AnnotatedElement, Class)} instead.
	 * @param annotatedElement the {@code AnnotatedElement} from which to get the annotation
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @return the first matching annotation, or {@code null} if not found
	 * @since 3.1
	 */
	public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotatedElement, annotationType);
		A annotation=(A) getAnnotationCache.get(cacheKey);//进行缓存
		if(annotation!=null) {
			if(annotation == NULL){
				return null;
			}
			return annotation;
		}
		try {
			annotation = annotatedElement.getAnnotation(annotationType);
			Annotation childExtendAnn=null;
			if (annotation == null) {
				ExtendsInfo<A> info=getAnnotation(null,annotatedElement, annotatedElement,annotationType);
				if(info!=null) {
					annotation=info.parent;
					childExtendAnn=info.child;
				}
			}
			if(annotation != null) {
				annotation=synthesizeAnnotation(annotation, annotatedElement,childExtendAnn);
				getAnnotationCache.put(cacheKey, annotation);
			}else{
				getAnnotationCache.put(cacheKey,NULL);
			}
			return annotation;
		}catch (Throwable ex) {
			handleIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}
	
	private static <A extends Annotation> ExtendsInfo<A> getAnnotation(Annotation childMetaAnn,AnnotatedElement annotatedElement,AnnotatedElement currentElement,Class<A> annotationType){
		//查询注解上的注解来看成是继承的交注解
		A parent;
		Annotation child;
		for (Annotation metaAnn : currentElement.getAnnotations()) {
			if(isInJavaLangAnnotationPackage(metaAnn)) {
				continue;
			}
			parent= getAnnotation(metaAnn,annotationType);
			if (parent!=null) {
				//对子注解进行同步操作
				metaAnn=getAnnotation(annotatedElement, metaAnn.annotationType());
				child=synthesizeAnnotation(metaAnn, annotatedElement,childMetaAnn);
				return new ExtendsInfo<A>(parent, child);
			}
		}
		for (Annotation metaAnn : currentElement.getAnnotations()) {
			if(isInJavaLangAnnotationPackage(metaAnn)) {
				continue;
			}
			ExtendsInfo<A> childInfo=getAnnotation(metaAnn,annotatedElement, metaAnn.annotationType(), annotationType);
			if(childInfo!=null) {
				return childInfo;
			}
		}
		return null;
	}
	
	
	static class ExtendsInfo<A>{
		A parent;
		Annotation child;
		ExtendsInfo(A parent,Annotation child){
			this.parent=parent;
			this.child=child;
		}
		
	}

	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the
	 * supplied {@link Method}, where the annotation is either <em>present</em>
	 * or <em>meta-present</em> on the method.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * <p>Note that this method supports only a single level of meta-annotations.
	 * For support for arbitrary levels of meta-annotations, use
	 * {@link #findAnnotation(Method, Class)} instead.
	 * @param method the method to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the first matching annotation, or {@code null} if not found
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 * @see #getAnnotation(AnnotatedElement, Class)
	 */
	
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		return getAnnotation((AnnotatedElement) resolvedMethod, annotationType);
	}

	/**
	 * Get all {@link Annotation Annotations} that are <em>present</em> on the
	 * supplied {@link AnnotatedElement}.
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * @param annotatedElement the Method, Constructor or Field to retrieve annotations from
	 * @return the annotations found, an empty array, or {@code null} if not
	 * resolvable (e.g. because nested Class values in annotation attributes
	 * failed to resolve at runtime)
	 * @since 4.0.8
	 * @see AnnotatedElement#getAnnotations()
	 */
	
	public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
		try {
			return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}

	/**
	 * Get all {@link Annotation Annotations} that are <em>present</em> on the
	 * supplied {@link Method}.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * @param method the Method to retrieve annotations from
	 * @return the annotations found, an empty array, or {@code null} if not
	 * resolvable (e.g. because nested Class values in annotation attributes
	 * failed to resolve at runtime)
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 * @see AnnotatedElement#getAnnotations()
	 */
	
	public static Annotation[] getAnnotations(Method method) {
		try {
			return synthesizeAnnotationArray(method.getAnnotations(), method);
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(method, ex);
			return null;
		}
	}

	/**
	 * Get the <em>repeatable</em> {@linkplain Annotation annotations} of
	 * {@code annotationType} from the supplied {@link AnnotatedElement}, where
	 * such annotations are either <em>present</em>, <em>indirectly present</em>,
	 * or <em>meta-present</em> on the element.
	 * <p>This method mimics the functionality of Java 8's
	 * {@link java.lang.reflect.AnnotatedElement#getAnnotationsByType(Class)}
	 * with support for automatic detection of a <em>container annotation</em>
	 * declared via @{@link java.lang.annotation.Repeatable} (when running on
	 * Java 8 or higher) and with additional support for meta-annotations.
	 * <p>Handles both single annotations and annotations nested within a
	 * <em>container annotation</em>.
	 * <p>Correctly handles <em>bridge methods</em> generated by the
	 * compiler if the supplied element is a {@link Method}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>present</em> on the supplied element.
	 * @param annotatedElement the element to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the annotations found or an empty set (never {@code null})
	 * @since 4.2
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see #getDeclaredRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see AnnotatedElementUtils#getMergedRepeatableAnnotations(AnnotatedElement, Class)
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod
	 * @see java.lang.annotation.Repeatable
	 * @see java.lang.reflect.AnnotatedElement#getAnnotationsByType
	 */
	public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement,
			Class<A> annotationType) {

		return getRepeatableAnnotations(annotatedElement, annotationType, null);
	}

	/**
	 * Get the <em>repeatable</em> {@linkplain Annotation annotations} of
	 * {@code annotationType} from the supplied {@link AnnotatedElement}, where
	 * such annotations are either <em>present</em>, <em>indirectly present</em>,
	 * or <em>meta-present</em> on the element.
	 * <p>This method mimics the functionality of Java 8's
	 * {@link java.lang.reflect.AnnotatedElement#getAnnotationsByType(Class)}
	 * with additional support for meta-annotations.
	 * <p>Handles both single annotations and annotations nested within a
	 * <em>container annotation</em>.
	 * <p>Correctly handles <em>bridge methods</em> generated by the
	 * compiler if the supplied element is a {@link Method}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>present</em> on the supplied element.
	 * @param annotatedElement the element to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @param containerAnnotationType the type of the container that holds
	 * the annotations; may be {@code null} if a container is not supported
	 * or if it should be looked up via @{@link java.lang.annotation.Repeatable}
	 * when running on Java 8 or higher
	 * @return the annotations found or an empty set (never {@code null})
	 * @since 4.2
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class)
	 * @see #getDeclaredRepeatableAnnotations(AnnotatedElement, Class)
	 * @see #getDeclaredRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see AnnotatedElementUtils#getMergedRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod
	 * @see java.lang.annotation.Repeatable
	 * @see java.lang.reflect.AnnotatedElement#getAnnotationsByType
	 */
	public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement,
			Class<A> annotationType,  Class<? extends Annotation> containerAnnotationType) {

		Set<A> annotations = getDeclaredRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
		if (!annotations.isEmpty()) {
			return annotations;
		}

		if (annotatedElement instanceof Class) {
			Class<?> superclass = ((Class<?>) annotatedElement).getSuperclass();
			if (superclass != null && Object.class != superclass) {
				return getRepeatableAnnotations(superclass, annotationType, containerAnnotationType);
			}
		}

		return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, false);
	}

	/**
	 * Get the declared <em>repeatable</em> {@linkplain Annotation annotations}
	 * of {@code annotationType} from the supplied {@link AnnotatedElement},
	 * where such annotations are either <em>directly present</em>,
	 * <em>indirectly present</em>, or <em>meta-present</em> on the element.
	 * <p>This method mimics the functionality of Java 8's
	 * {@link java.lang.reflect.AnnotatedElement#getDeclaredAnnotationsByType(Class)}
	 * with support for automatic detection of a <em>container annotation</em>
	 * declared via @{@link java.lang.annotation.Repeatable} (when running on
	 * Java 8 or higher) and with additional support for meta-annotations.
	 * <p>Handles both single annotations and annotations nested within a
	 * <em>container annotation</em>.
	 * <p>Correctly handles <em>bridge methods</em> generated by the
	 * compiler if the supplied element is a {@link Method}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>present</em> on the supplied element.
	 * @param annotatedElement the element to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the annotations found or an empty set (never {@code null})
	 * @since 4.2
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class)
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see #getDeclaredRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see AnnotatedElementUtils#getMergedRepeatableAnnotations(AnnotatedElement, Class)
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod
	 * @see java.lang.annotation.Repeatable
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotationsByType
	 */
	public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement,
			Class<A> annotationType) {

		return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
	}

	/**
	 * Get the declared <em>repeatable</em> {@linkplain Annotation annotations}
	 * of {@code annotationType} from the supplied {@link AnnotatedElement},
	 * where such annotations are either <em>directly present</em>,
	 * <em>indirectly present</em>, or <em>meta-present</em> on the element.
	 * <p>This method mimics the functionality of Java 8's
	 * {@link java.lang.reflect.AnnotatedElement#getDeclaredAnnotationsByType(Class)}
	 * with additional support for meta-annotations.
	 * <p>Handles both single annotations and annotations nested within a
	 * <em>container annotation</em>.
	 * <p>Correctly handles <em>bridge methods</em> generated by the
	 * compiler if the supplied element is a {@link Method}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>present</em> on the supplied element.
	 * @param annotatedElement the element to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @param containerAnnotationType the type of the container that holds
	 * the annotations; may be {@code null} if a container is not supported
	 * or if it should be looked up via @{@link java.lang.annotation.Repeatable}
	 * when running on Java 8 or higher
	 * @return the annotations found or an empty set (never {@code null})
	 * @since 4.2
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class)
	 * @see #getRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see #getDeclaredRepeatableAnnotations(AnnotatedElement, Class)
	 * @see AnnotatedElementUtils#getMergedRepeatableAnnotations(AnnotatedElement, Class, Class)
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod
	 * @see java.lang.annotation.Repeatable
	 * @see java.lang.reflect.AnnotatedElement#getDeclaredAnnotationsByType
	 */
	public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement,
			Class<A> annotationType,  Class<? extends Annotation> containerAnnotationType) {

		return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, true);
	}

	/**
	 * Perform the actual work for {@link #getRepeatableAnnotations(AnnotatedElement, Class, Class)}
	 * and {@link #getDeclaredRepeatableAnnotations(AnnotatedElement, Class, Class)}.
	 * <p>Correctly handles <em>bridge methods</em> generated by the
	 * compiler if the supplied element is a {@link Method}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>present</em> on the supplied element.
	 * @param annotatedElement the element to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @param containerAnnotationType the type of the container that holds
	 * the annotations; may be {@code null} if a container is not supported
	 * or if it should be looked up via @{@link java.lang.annotation.Repeatable}
	 * when running on Java 8 or higher
	 * @param declaredMode {@code true} if only declared annotations (i.e.,
	 * directly or indirectly present) should be considered
	 * @return the annotations found or an empty set (never {@code null})
	 * @since 4.2
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod
	 * @see java.lang.annotation.Repeatable
	 */
	private static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement,
			Class<A> annotationType,  Class<? extends Annotation> containerAnnotationType, boolean declaredMode) {

		try {
			if (annotatedElement instanceof Method) {
				annotatedElement = BridgeMethodResolver.findBridgedMethod((Method) annotatedElement);
			}
			return new AnnotationCollector<>(annotationType, containerAnnotationType, declaredMode).getResult(annotatedElement);
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotatedElement, ex);
			return Collections.emptySet();
		}
	}

	/**
	 * Find a single {@link Annotation} of {@code annotationType} on the
	 * supplied {@link AnnotatedElement}.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>directly present</em> on the supplied element.
	 * <p><strong>Warning</strong>: this method operates generically on
	 * annotated elements. In other words, this method does not execute
	 * specialized search algorithms for classes or methods. If you require
	 * the more specific semantics of {@link #findAnnotation(Class, Class)}
	 * or {@link #findAnnotation(Method, Class)}, invoke one of those methods
	 * instead.
	 * @param annotatedElement the {@code AnnotatedElement} on which to find the annotation
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @return the first matching annotation, or {@code null} if not found
	 * @since 4.2
	 */
	
	public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		// Do NOT store result in the findAnnotationCache since doing so could break
		// findAnnotation(Class, Class) and findAnnotation(Method, Class).
		A ann = findAnnotation(annotatedElement, annotationType, new HashSet<Annotation>());
		return (ann != null ? synthesizeAnnotation(ann, annotatedElement) : null);
	}

	/**
	 * Perform the search algorithm for {@link #findAnnotation(AnnotatedElement, Class)}
	 * avoiding endless recursion by tracking which annotations have already
	 * been <em>visited</em>.
	 * @param annotatedElement the {@code AnnotatedElement} on which to find the annotation
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @param visited the set of annotations that have already been visited
	 * @return the first matching annotation, or {@code null} if not found
	 * @since 4.2
	 */
	
	private static <A extends Annotation> A findAnnotation(
			AnnotatedElement annotatedElement, Class<A> annotationType, Set<Annotation> visited) {
		try {
			A annotation = annotatedElement.getAnnotation(annotationType);
			if (annotation != null) {
				return annotation;
			}
			for (Annotation declaredAnn : annotatedElement.getDeclaredAnnotations()) {
				Class<? extends Annotation> declaredType = declaredAnn.annotationType();
				if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
					annotation = findAnnotation((AnnotatedElement) declaredType, annotationType, visited);
					if (annotation != null) {
						return annotation;
					}
				}
			}
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotatedElement, ex);
		}
		return null;
	}

	/**
	 * Find a single {@link Annotation} of {@code annotationType} on the supplied
	 * {@link Method}, traversing its super methods (i.e., from superclasses and
	 * interfaces) if the annotation is not <em>directly present</em> on the given
	 * method itself.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * <p>Meta-annotations will be searched if the annotation is not
	 * <em>directly present</em> on the method.
	 * <p>Annotations on methods are not inherited by default, so we need to handle
	 * this explicitly.
	 * @param method the method to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the first matching annotation, or {@code null} if not found
	 * @see #getAnnotation(Method, Class)
	 */
	@SuppressWarnings("unchecked")
	
	public static <A extends Annotation> A findAnnotation(Method method,  Class<A> annotationType) {
		if (annotationType == null) {
			return null;
		}

		AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
		A result = (A) findAnnotationCache.get(cacheKey);

		if (result == null) {
			Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
			result = findAnnotation((AnnotatedElement) resolvedMethod, annotationType);
			if (result == null) {
				result = searchOnInterfaces(method, annotationType, method.getDeclaringClass().getInterfaces());
			}

			Class<?> clazz = method.getDeclaringClass();
			while (result == null) {
				clazz = clazz.getSuperclass();
				if (clazz == null || Object.class == clazz) {
					break;
				}
				Set<Method> annotatedMethods = getAnnotatedMethodsInBaseType(clazz);
				if (!annotatedMethods.isEmpty()) {
					for (Method annotatedMethod : annotatedMethods) {
						if (annotatedMethod.getName().equals(method.getName()) &&
								Arrays.equals(annotatedMethod.getParameterTypes(), method.getParameterTypes())) {
							Method resolvedSuperMethod = BridgeMethodResolver.findBridgedMethod(annotatedMethod);
							result = findAnnotation((AnnotatedElement) resolvedSuperMethod, annotationType);
							if (result != null) {
								break;
							}
						}
					}
				}
				if (result == null) {
					result = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
				}
			}

			if (result != null) {
				result = synthesizeAnnotation(result, method);
				findAnnotationCache.put(cacheKey, result);
			}
		}

		return result;
	}

	
	private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>... ifcs) {
		for (Class<?> ifc : ifcs) {
			Set<Method> annotatedMethods = getAnnotatedMethodsInBaseType(ifc);
			if (!annotatedMethods.isEmpty()) {
				for (Method annotatedMethod : annotatedMethods) {
					if (isOverride(method, annotatedMethod)) {
						A annotation = getAnnotation(annotatedMethod, annotationType);
						if (annotation != null) {
							return annotation;
						}
					}
				}
			}
		}
		return null;
	}

	static Set<Method> getAnnotatedMethodsInBaseType(Class<?> baseType) {
		boolean ifcCheck = baseType.isInterface();
		if (ifcCheck && ClassTypeUtils.isJavaLanguageInterface(baseType)) {
			return Collections.emptySet();
		}

		Set<Method> annotatedMethods = annotatedBaseTypeCache.get(baseType);
		if (annotatedMethods != null) {
			return annotatedMethods;
		}
		Method[] methods = (ifcCheck ? baseType.getMethods() : baseType.getDeclaredMethods());
		for (Method baseMethod : methods) {
			try {
				// Public methods on interfaces (including interface hierarchy),
				// non-private (and therefore overridable) methods on base classes
				if ((ifcCheck || !Modifier.isPrivate(baseMethod.getModifiers())) &&
						hasSearchableAnnotations(baseMethod)) {
					if (annotatedMethods == null) {
						annotatedMethods = new HashSet<>();
					}
					annotatedMethods.add(baseMethod);
				}
			}
			catch (Throwable ex) {
				handleIntrospectionFailure(baseMethod, ex);
			}
		}
		if (annotatedMethods == null) {
			annotatedMethods = Collections.emptySet();
		}
		annotatedBaseTypeCache.put(baseType, annotatedMethods);
		return annotatedMethods;
	}

	private static boolean hasSearchableAnnotations(Method ifcMethod) {
		Annotation[] anns = ifcMethod.getAnnotations();
		if (anns.length == 0) {
			return false;
		}
		if (anns.length == 1) {
			Class<?> annType = anns[0].annotationType();
			return (annType != Deprecated.class);
		}
		return true;
	}

	private static boolean isOverride(Method method, Method candidate) {
		if (!candidate.getName().equals(method.getName()) ||
				candidate.getParameterTypes().length != method.getParameterTypes().length) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		if (Arrays.equals(candidate.getParameterTypes(), paramTypes)) {
			return true;
		}
		return false;
	}

	/**
	 * Find a single {@link Annotation} of {@code annotationType} on the
	 * supplied {@link Class}, traversing its interfaces, annotations, and
	 * superclasses if the annotation is not <em>directly present</em> on
	 * the given class itself.
	 * <p>This method explicitly handles class-level annotations which are not
	 * declared as {@link java.lang.annotation.Inherited inherited} <em>as well
	 * as meta-annotations and annotations on interfaces</em>.
	 * <p>The algorithm operates as follows:
	 * <ol>
	 * <li>Search for the annotation on the given class and return it if found.
	 * <li>Recursively search through all annotations that the given class declares.
	 * <li>Recursively search through all interfaces that the given class declares.
	 * <li>Recursively search through the superclass hierarchy of the given class.
	 * </ol>
	 * <p>Note: in this context, the term <em>recursively</em> means that the search
	 * process continues by returning to step #1 with the current interface,
	 * annotation, or superclass as the class to look for annotations on.
	 * @param clazz the class to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @return the first matching annotation, or {@code null} if not found
	 */
	
	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
		return findAnnotation(clazz, annotationType, true);
	}

	/**
	 * Perform the actual work for {@link #findAnnotation(AnnotatedElement, Class)},
	 * honoring the {@code synthesize} flag.
	 * @param clazz the class to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @param synthesize {@code true} if the result should be
	 * {@linkplain #synthesizeAnnotation(Annotation) synthesized}
	 * @return the first matching annotation, or {@code null} if not found
	 * @since 4.2.1
	 */
	@SuppressWarnings("unchecked")
	
	private static <A extends Annotation> A findAnnotation(
			Class<?> clazz,  Class<A> annotationType, boolean synthesize) {

		if (annotationType == null) {
			return null;
		}

		AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
		A result = (A) findAnnotationCache.get(cacheKey);
		if (result == null) {
			result = findAnnotation(clazz, annotationType, new HashSet<Annotation>());
			if (result != null && synthesize) {
				result = synthesizeAnnotation(result, clazz);
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return result;
	}

	/**
	 * Perform the search algorithm for {@link #findAnnotation(Class, Class)},
	 * avoiding endless recursion by tracking which annotations have already
	 * been <em>visited</em>.
	 * @param clazz the class to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @param visited the set of annotations that have already been visited
	 * @return the first matching annotation, or {@code null} if not found
	 */
	
	private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
		try {
			A annotation = clazz.getAnnotation(annotationType);
			if (annotation != null) {
				return annotation;
			}
			for (Annotation declaredAnn : clazz.getDeclaredAnnotations()) {
				Class<? extends Annotation> declaredType = declaredAnn.annotationType();
				if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
					annotation = findAnnotation(declaredType, annotationType, visited);
					if (annotation != null) {
						return annotation;
					}
				}
			}
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(clazz, ex);
			return null;
		}

		for (Class<?> ifc : clazz.getInterfaces()) {
			A annotation = findAnnotation(ifc, annotationType, visited);
			if (annotation != null) {
				return annotation;
			}
		}

		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null || Object.class == superclass) {
			return null;
		}
		return findAnnotation(superclass, annotationType, visited);
	}

	/**
	 * Find the first {@link Class} in the inheritance hierarchy of the
	 * specified {@code clazz} (including the specified {@code clazz} itself)
	 * on which an annotation of the specified {@code annotationType} is
	 * <em>directly present</em>.
	 * <p>If the supplied {@code clazz} is an interface, only the interface
	 * itself will be checked; the inheritance hierarchy for interfaces will
	 * not be traversed.
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * <p>The standard {@link Class} API does not provide a mechanism for
	 * determining which class in an inheritance hierarchy actually declares
	 * an {@link Annotation}, so we need to handle this explicitly.
	 * @param annotationType the annotation type to look for
	 * @param clazz the class to check for the annotation on (may be {@code null})
	 * @return the first {@link Class} in the inheritance hierarchy that
	 * declares an annotation of the specified {@code annotationType}, or
	 * {@code null} if not found
	 * @see Class#isAnnotationPresent(Class)
	 * @see Class#getDeclaredAnnotations()
	 * @see #findAnnotationDeclaringClassForTypes(List, Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	
	public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType,  Class<?> clazz) {
		if (clazz == null || Object.class == clazz) {
			return null;
		}
		if (isAnnotationDeclaredLocally(annotationType, clazz)) {
			return clazz;
		}
		return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
	}

	/**
	 * Find the first {@link Class} in the inheritance hierarchy of the
	 * specified {@code clazz} (including the specified {@code clazz} itself)
	 * on which at least one of the specified {@code annotationTypes} is
	 * <em>directly present</em>.
	 * <p>If the supplied {@code clazz} is an interface, only the interface
	 * itself will be checked; the inheritance hierarchy for interfaces will
	 * not be traversed.
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * <p>The standard {@link Class} API does not provide a mechanism for
	 * determining which class in an inheritance hierarchy actually declares
	 * one of several candidate {@linkplain Annotation annotations}, so we
	 * need to handle this explicitly.
	 * @param annotationTypes the annotation types to look for
	 * @param clazz the class to check for the annotations on, or {@code null}
	 * @return the first {@link Class} in the inheritance hierarchy that
	 * declares an annotation of at least one of the specified
	 * {@code annotationTypes}, or {@code null} if not found
	 * @since 3.2.2
	 * @see Class#isAnnotationPresent(Class)
	 * @see Class#getDeclaredAnnotations()
	 * @see #findAnnotationDeclaringClass(Class, Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	
	public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes,  Class<?> clazz) {
		if (clazz == null || Object.class == clazz) {
			return null;
		}
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			if (isAnnotationDeclaredLocally(annotationType, clazz)) {
				return clazz;
			}
		}
		return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
	}

	/**
	 * Determine whether an annotation of the specified {@code annotationType}
	 * is declared locally (i.e., <em>directly present</em>) on the supplied
	 * {@code clazz}.
	 * <p>The supplied {@link Class} may represent any type.
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * <p>Note: This method does <strong>not</strong> determine if the annotation
	 * is {@linkplain java.lang.annotation.Inherited inherited}. For greater
	 * clarity regarding inherited annotations, consider using
	 * {@link #isAnnotationInherited(Class, Class)} instead.
	 * @param annotationType the annotation type to look for
	 * @param clazz the class to check for the annotation on
	 * @return {@code true} if an annotation of the specified {@code annotationType}
	 * is <em>directly present</em>
	 * @see java.lang.Class#getDeclaredAnnotations()
	 * @see java.lang.Class#getDeclaredAnnotation(Class)
	 * @see #isAnnotationInherited(Class, Class)
	 */
	public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
		try {
			return (clazz.getAnnotation(annotationType) != null);
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(clazz, ex);
			return false;
		}
	}

	/**
	 * Determine whether an annotation of the specified {@code annotationType}
	 * is <em>present</em> on the supplied {@code clazz} and is
	 * {@linkplain java.lang.annotation.Inherited inherited} (i.e., not
	 * <em>directly present</em>).
	 * <p>Meta-annotations will <em>not</em> be searched.
	 * <p>If the supplied {@code clazz} is an interface, only the interface
	 * itself will be checked. In accordance with standard meta-annotation
	 * semantics in Java, the inheritance hierarchy for interfaces will not
	 * be traversed. See the {@linkplain java.lang.annotation.Inherited javadoc}
	 * for the {@code @Inherited} meta-annotation for further details regarding
	 * annotation inheritance.
	 * @param annotationType the annotation type to look for
	 * @param clazz the class to check for the annotation on
	 * @return {@code true} if an annotation of the specified {@code annotationType}
	 * is <em>present</em> and <em>inherited</em>
	 * @see Class#isAnnotationPresent(Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz) {
		return (clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz));
	}

	/**
	 * Determine if an annotation of type {@code metaAnnotationType} is
	 * <em>meta-present</em> on the supplied {@code annotationType}.
	 * @param annotationType the annotation type to search on
	 * @param metaAnnotationType the type of meta-annotation to search for
	 * @return {@code true} if such an annotation is meta-present
	 * @since 4.2.1
	 */
	public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType,
			 Class<? extends Annotation> metaAnnotationType) {

		if (metaAnnotationType == null) {
			return false;
		}

		AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
		Boolean metaPresent = metaPresentCache.get(cacheKey);
		if (metaPresent != null) {
			return metaPresent;
		}
		metaPresent = Boolean.FALSE;
		if (findAnnotation(annotationType, metaAnnotationType, false) != null) {
			metaPresent = Boolean.TRUE;
		}
		metaPresentCache.put(cacheKey, metaPresent);
		return metaPresent;
	}

	/**
	 * Determine if the supplied {@link Annotation} is defined in the core JDK
	 * {@code java.lang.annotation} package.
	 * @param annotation the annotation to check
	 * @return {@code true} if the annotation is in the {@code java.lang.annotation} package
	 */
	public static boolean isInJavaLangAnnotationPackage( Annotation annotation) {
		return (annotation != null && isInJavaLangAnnotationPackage(annotation.annotationType()));
	}

	/**
	 * Determine if the {@link Annotation} with the supplied name is defined
	 * in the core JDK {@code java.lang.annotation} package.
	 * @param annotationType the annotation type to check
	 * @return {@code true} if the annotation is in the {@code java.lang.annotation} package
	 * @since 4.3.8
	 */
	static boolean isInJavaLangAnnotationPackage( Class<? extends Annotation> annotationType) {
		return (annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName()));
	}

	/**
	 * Determine if the {@link Annotation} with the supplied name is defined
	 * in the core JDK {@code java.lang.annotation} package.
	 * @param annotationType the name of the annotation type to check
	 * @return {@code true} if the annotation is in the {@code java.lang.annotation} package
	 * @since 4.2
	 */
	public static boolean isInJavaLangAnnotationPackage( String annotationType) {
		return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
	}

	/**
	 * Check the declared attributes of the given annotation, in particular covering
	 * Google App Engine's late arrival of {@code TypeNotPresentExceptionProxy} for
	 * {@code Class} values (instead of early {@code Class.getAnnotations() failure}.
	 * <p>This method not failing indicates that {@link #getAnnotationAttributes(Annotation)}
	 * won't failure either (when attempted later on).
	 * @param annotation the annotation to validate
	 * @throws IllegalStateException if a declared {@code Class} attribute could not be read
	 * @since 4.3.15
	 * @see Class#getAnnotations()
	 * @see #getAnnotationAttributes(Annotation)
	 */
	public static void validateAnnotation(Annotation annotation) {
		for (Method method : getAttributeMethods(annotation.annotationType())) {
			Class<?> returnType = method.getReturnType();
			if (returnType == Class.class || returnType == Class[].class) {
				try {
					method.invoke(annotation);
				}
				catch (Throwable ex) {
					throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
				}
			}
		}
	}

	/**
	 * Retrieve the given annotation's attributes as a {@link Map}, preserving all
	 * attribute types.
	 * <p>Equivalent to calling {@link #getAnnotationAttributes(Annotation, boolean, boolean)}
	 * with the {@code classValuesAsString} and {@code nestedAnnotationsAsMap} parameters
	 * set to {@code false}.
	 * <p>Note: This method actually returns an {@link AnnotationAttributes} instance.
	 * However, the {@code Map} signature has been preserved for binary compatibility.
	 * @param annotation the annotation to retrieve the attributes for
	 * @return the Map of annotation attributes, with attribute names as keys and
	 * corresponding attribute values as values (never {@code null})
	 * @see #getAnnotationAttributes(AnnotatedElement, Annotation)
	 * @see #getAnnotationAttributes(Annotation, boolean, boolean)
	 * @see #getAnnotationAttributes(AnnotatedElement, Annotation, boolean, boolean)
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
		return getAnnotationAttributes(null, annotation);
	}

	/**
	 * Retrieve the given annotation's attributes as a {@link Map}.
	 * <p>Equivalent to calling {@link #getAnnotationAttributes(Annotation, boolean, boolean)}
	 * with the {@code nestedAnnotationsAsMap} parameter set to {@code false}.
	 * <p>Note: This method actually returns an {@link AnnotationAttributes} instance.
	 * However, the {@code Map} signature has been preserved for binary compatibility.
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @return the Map of annotation attributes, with attribute names as keys and
	 * corresponding attribute values as values (never {@code null})
	 * @see #getAnnotationAttributes(Annotation, boolean, boolean)
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString) {
		return getAnnotationAttributes(annotation, classValuesAsString, false);
	}

	/**
	 * Retrieve the given annotation's attributes as an {@link AnnotationAttributes} map.
	 * <p>This method provides fully recursive annotation reading capabilities on par with
	 * the reflection-based {@link org.springframework.core.type.StandardAnnotationMetadata}.
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to convert nested annotations into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata}) or to preserve them as
	 * {@code Annotation} instances
	 * @return the annotation attributes (a specialized Map) with attribute names as keys
	 * and corresponding attribute values as values (never {@code null})
	 * @since 3.1.1
	 */
	public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString,
			boolean nestedAnnotationsAsMap) {

		return getAnnotationAttributes(null, annotation, classValuesAsString, nestedAnnotationsAsMap);
	}

	/**
	 * Retrieve the given annotation's attributes as an {@link AnnotationAttributes} map.
	 * <p>Equivalent to calling {@link #getAnnotationAttributes(AnnotatedElement, Annotation, boolean, boolean)}
	 * with the {@code classValuesAsString} and {@code nestedAnnotationsAsMap} parameters
	 * set to {@code false}.
	 * @param annotatedElement the element that is annotated with the supplied annotation;
	 * may be {@code null} if unknown
	 * @param annotation the annotation to retrieve the attributes for
	 * @return the annotation attributes (a specialized Map) with attribute names as keys
	 * and corresponding attribute values as values (never {@code null})
	 * @since 4.2
	 * @see #getAnnotationAttributes(AnnotatedElement, Annotation, boolean, boolean)
	 */
	public static AnnotationAttributes getAnnotationAttributes( AnnotatedElement annotatedElement, Annotation annotation) {
		return getAnnotationAttributes(annotatedElement, annotation, false, false);
	}

	/**
	 * Retrieve the given annotation's attributes as an {@link AnnotationAttributes} map.
	 * <p>This method provides fully recursive annotation reading capabilities on par with
	 * the reflection-based {@link org.springframework.core.type.StandardAnnotationMetadata}.
	 * @param annotatedElement the element that is annotated with the supplied annotation;
	 * may be {@code null} if unknown
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to convert nested annotations into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata}) or to preserve them as
	 * {@code Annotation} instances
	 * @return the annotation attributes (a specialized Map) with attribute names as keys
	 * and corresponding attribute values as values (never {@code null})
	 * @since 4.2
	 */
	public static AnnotationAttributes getAnnotationAttributes( AnnotatedElement annotatedElement,
			Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

		return getAnnotationAttributes(
				(Object) annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
	}

	private static AnnotationAttributes getAnnotationAttributes( Object annotatedElement,
			Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

		AnnotationAttributes attributes =
				retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
		postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
		return attributes;
	}

	/**
	 * Retrieve the given annotation's attributes as an {@link AnnotationAttributes} map.
	 * <p>This method provides fully recursive annotation reading capabilities on par with
	 * the reflection-based {@link org.springframework.core.type.StandardAnnotationMetadata}.
	 * <p><strong>NOTE</strong>: This variant of {@code getAnnotationAttributes()} is
	 * only intended for use within the framework. The following special rules apply:
	 * <ol>
	 * <li>Default values will be replaced with default value placeholders.</li>
	 * <li>The resulting, merged annotation attributes should eventually be
	 * {@linkplain #postProcessAnnotationAttributes post-processed} in order to
	 * ensure that placeholders have been replaced by actual default values and
	 * in order to enforce {@code @AliasFor} semantics.</li>
	 * </ol>
	 * @param annotatedElement the element that is annotated with the supplied annotation;
	 * may be {@code null} if unknown
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to convert nested annotations into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata}) or to preserve them as
	 * {@code Annotation} instances
	 * @return the annotation attributes (a specialized Map) with attribute names as keys
	 * and corresponding attribute values as values (never {@code null})
	 * @since 4.2
	 * @see #postProcessAnnotationAttributes
	 */
	static AnnotationAttributes retrieveAnnotationAttributes( Object annotatedElement, Annotation annotation,
			boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

		Class<? extends Annotation> annotationType = annotation.annotationType();
		AnnotationAttributes attributes = new AnnotationAttributes(annotationType);

		for (Method method : getAttributeMethods(annotationType)) {
			try {
				Object attributeValue = method.invoke(annotation);
				Object defaultValue = method.getDefaultValue();
				if (defaultValue != null && Objects.equals(attributeValue, defaultValue)) {
					attributeValue = new DefaultValueHolder(defaultValue);
				}
				attributes.put(method.getName(),
						adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
			}
			catch (Throwable ex) {
				if (ex instanceof InvocationTargetException) {
					Throwable targetException = ((InvocationTargetException) ex).getTargetException();
					throw new AnnotationException("",targetException);
				}
				throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
			}
		}

		return attributes;
	}

	/**
	 * Adapt the given value according to the given class and nested annotation settings.
	 * <p>Nested annotations will be
	 * {@linkplain #synthesizeAnnotation(Annotation, AnnotatedElement) synthesized}.
	 * @param annotatedElement the element that is annotated, used for contextual
	 * logging; may be {@code null} if unknown
	 * @param value the annotation attribute value
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to convert nested annotations into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata}) or to preserve them as
	 * {@code Annotation} instances
	 * @return the adapted value, or the original value if no adaptation is needed
	 */
	
	static Object adaptValue( Object annotatedElement,  Object value,
			boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

		if (classValuesAsString) {
			if (value instanceof Class) {
				return ((Class<?>) value).getName();
			}
			else if (value instanceof Class[]) {
				Class<?>[] clazzArray = (Class<?>[]) value;
				String[] classNames = new String[clazzArray.length];
				for (int i = 0; i < clazzArray.length; i++) {
					classNames[i] = clazzArray[i].getName();
				}
				return classNames;
			}
		}

		if (value instanceof Annotation) {
			Annotation annotation = (Annotation) value;
			if (nestedAnnotationsAsMap) {
				return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
			}
			else {
				return synthesizeAnnotation(annotation, annotatedElement);
			}
		}

		if (value instanceof Annotation[]) {
			Annotation[] annotations = (Annotation[]) value;
			if (nestedAnnotationsAsMap) {
				AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
				for (int i = 0; i < annotations.length; i++) {
					mappedAnnotations[i] =
							getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);
				}
				return mappedAnnotations;
			}
			else {
				return synthesizeAnnotationArray(annotations, annotatedElement);
			}
		}

		// Fallback
		return value;
	}

	/**
	 * Register the annotation-declared default values for the given attributes,
	 * if available.
	 * @param attributes the annotation attributes to process
	 * @since 4.3.2
	 */
	public static void registerDefaultValues(AnnotationAttributes attributes) {
		// Only do defaults scanning for public annotations; we'd run into
		// IllegalAccessExceptions otherwise, and we don't want to mess with
		// accessibility in a SecurityManager environment.
		Class<? extends Annotation> annotationType = attributes.annotationType();
		if (annotationType != null && Modifier.isPublic(annotationType.getModifiers())) {
			// Check declared default values of attributes in the annotation type.
			for (Method annotationAttribute : getAttributeMethods(annotationType)) {
				String attributeName = annotationAttribute.getName();
				Object defaultValue = annotationAttribute.getDefaultValue();
				if (defaultValue != null && !attributes.containsKey(attributeName)) {
					if (defaultValue instanceof Annotation) {
						defaultValue = getAnnotationAttributes((Annotation) defaultValue, false, true);
					}
					else if (defaultValue instanceof Annotation[]) {
						Annotation[] realAnnotations = (Annotation[]) defaultValue;
						AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
						for (int i = 0; i < realAnnotations.length; i++) {
							mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], false, true);
						}
						defaultValue = mappedAnnotations;
					}
					attributes.put(attributeName, new DefaultValueHolder(defaultValue));
				}
			}
		}
	}

	/**
	 * Post-process the supplied {@link AnnotationAttributes}, preserving nested
	 * annotations as {@code Annotation} instances.
	 * <p>Specifically, this method enforces <em>attribute alias</em> semantics
	 * for annotation attributes that are annotated with {@link AliasFor @AliasFor}
	 * and replaces default value placeholders with their original default values.
	 * @param annotatedElement the element that is annotated with an annotation or
	 * annotation hierarchy from which the supplied attributes were created;
	 * may be {@code null} if unknown
	 * @param attributes the annotation attributes to post-process
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @since 4.3.2
	 * @see #postProcessAnnotationAttributes(Object, AnnotationAttributes, boolean, boolean)
	 * @see #getDefaultValue(Class, String)
	 */
	public static void postProcessAnnotationAttributes( Object annotatedElement,
			AnnotationAttributes attributes, boolean classValuesAsString) {

		postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, false);
	}

	/**
	 * Post-process the supplied {@link AnnotationAttributes}.
	 * <p>Specifically, this method enforces <em>attribute alias</em> semantics
	 * for annotation attributes that are annotated with {@link AliasFor @AliasFor}
	 * and replaces default value placeholders with their original default values.
	 * @param annotatedElement the element that is annotated with an annotation or
	 * annotation hierarchy from which the supplied attributes were created;
	 * may be {@code null} if unknown
	 * @param attributes the annotation attributes to post-process
	 * @param classValuesAsString whether to convert Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata})
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to convert nested annotations into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata}) or to preserve them as
	 * {@code Annotation} instances
	 * @since 4.2
	 * @see #retrieveAnnotationAttributes(Object, Annotation, boolean, boolean)
	 * @see #getDefaultValue(Class, String)
	 */
	static void postProcessAnnotationAttributes( Object annotatedElement,
			 AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {

		if (attributes == null) {
			return;
		}

		Class<? extends Annotation> annotationType = attributes.annotationType();

		// Track which attribute values have already been replaced so that we can short
		// circuit the search algorithms.
		Set<String> valuesAlreadyReplaced = new HashSet<>();

		if (!attributes.validated) {
			// Validate @AliasFor configuration
			Map<String, List<String>> aliasMap = getAttributeAliasMap(annotationType);
			for(Map.Entry<String, List<String>> entry:aliasMap.entrySet()) {
				String attributeName=entry.getKey();
				List<String>aliasedAttributeNames=entry.getValue();
				if (valuesAlreadyReplaced.contains(attributeName)) {
					return;
				}
				Object value = attributes.get(attributeName);
				boolean valuePresent = (value != null && !(value instanceof DefaultValueHolder));
				for (String aliasedAttributeName : aliasedAttributeNames) {
					if (valuesAlreadyReplaced.contains(aliasedAttributeName)) {
						continue;
					}
					Object aliasedValue = attributes.get(aliasedAttributeName);
					boolean aliasPresent = (aliasedValue != null && !(aliasedValue instanceof DefaultValueHolder));
					// Something to validate or replace with an alias?
					if (valuePresent || aliasPresent) {
						if (valuePresent && aliasPresent) {
							// Since annotation attributes can be arrays, we must use ObjectUtils.nullSafeEquals().
							if (!Objects.equals(value, aliasedValue)) {
								String elementAsString =
										(annotatedElement != null ? annotatedElement.toString() : "unknown element");
								throw new AnnotationException(String.format(
										"In AnnotationAttributes for annotation [%s] declared on %s, " +
										"attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], " +
										"but only one is permitted.", attributes.displayName, elementAsString,
										attributeName, aliasedAttributeName, Objects.nullSafeToString(value),
										Objects.nullSafeToString(aliasedValue)));
							}
						}
						else if (aliasPresent) {
							// Replace value with aliasedValue
							attributes.put(attributeName,
									adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
							valuesAlreadyReplaced.add(attributeName);
						}
						else {
							// Replace aliasedValue with value
							attributes.put(aliasedAttributeName,
									adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
							valuesAlreadyReplaced.add(aliasedAttributeName);
						}
					}
				}
			}
			attributes.validated = true;
		}

		// Replace any remaining placeholders with actual default values
		for (String attributeName : attributes.keySet()) {
			if (valuesAlreadyReplaced.contains(attributeName)) {
				continue;
			}
			Object value = attributes.get(attributeName);
			if (value instanceof DefaultValueHolder) {
				value = ((DefaultValueHolder) value).defaultValue;
				attributes.put(attributeName,
						adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
			}
		}
	}

	/**
	 * Retrieve the <em>value</em> of the {@code value} attribute of a
	 * single-element Annotation, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the value
	 * @return the attribute value, or {@code null} if not found unless the attribute
	 * value cannot be retrieved due to an {@link AnnotationConfigurationException},
	 * in which case such an exception will be rethrown
	 * @see #getValue(Annotation, String)
	 */
	
	public static Object getValue(Annotation annotation) {
		return getValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>value</em> of a named attribute, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the value
	 * @param attributeName the name of the attribute value to retrieve
	 * @return the attribute value, or {@code null} if not found unless the attribute
	 * value cannot be retrieved due to an {@link AnnotationConfigurationException},
	 * in which case such an exception will be rethrown
	 * @see #getValue(Annotation)
	 * @see #rethrowAnnotationConfigurationException(Throwable)
	 */
	
	public static Object getValue( Annotation annotation,  String attributeName) {
		if (annotation == null || !StringUtils.hasText(attributeName)) {
			return null;
		}
		try {
			Method method = annotation.annotationType().getDeclaredMethod(attributeName);
			MethodUtils.makeAccessible(method);
			return method.invoke(annotation);
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException(
					"Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, ex);
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotation.getClass(), ex);
			return null;
		}
	}

	/**
	 * Retrieve the <em>default value</em> of the {@code value} attribute
	 * of a single-element Annotation, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the default value
	 * @return the default value, or {@code null} if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	
	public static Object getDefaultValue(Annotation annotation) {
		return getDefaultValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named attribute, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the default value
	 * @param attributeName the name of the attribute value to retrieve
	 * @return the default value of the named attribute, or {@code null} if not found
	 * @see #getDefaultValue(Class, String)
	 */
	
	public static Object getDefaultValue( Annotation annotation,  String attributeName) {
		if (annotation == null) {
			return null;
		}
		return getDefaultValue(annotation.annotationType(), attributeName);
	}

	/**
	 * Retrieve the <em>default value</em> of the {@code value} attribute
	 * of a single-element Annotation, given the {@link Class annotation type}.
	 * @param annotationType the <em>annotation type</em> for which the default value should be retrieved
	 * @return the default value, or {@code null} if not found
	 * @see #getDefaultValue(Class, String)
	 */
	
	public static Object getDefaultValue(Class<? extends Annotation> annotationType) {
		return getDefaultValue(annotationType, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named attribute, given the
	 * {@link Class annotation type}.
	 * @param annotationType the <em>annotation type</em> for which the default value should be retrieved
	 * @param attributeName the name of the attribute value to retrieve.
	 * @return the default value of the named attribute, or {@code null} if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	
	public static Object getDefaultValue(
			 Class<? extends Annotation> annotationType,  String attributeName) {

		if (annotationType == null || !StringUtils.hasText(attributeName)) {
			return null;
		}
		try {
			return annotationType.getDeclaredMethod(attributeName).getDefaultValue();
		}
		catch (Throwable ex) {
			handleIntrospectionFailure(annotationType, ex);
			return null;
		}
	}

	/**
	 * <em>Synthesize</em> an annotation from the supplied {@code annotation}
	 * by wrapping it in a dynamic proxy that transparently enforces
	 * <em>attribute alias</em> semantics for annotation attributes that are
	 * annotated with {@link AliasFor @AliasFor}.
	 * @param annotation the annotation to synthesize
	 * @return the synthesized annotation, if the supplied annotation is
	 * <em>synthesizable</em>; {@code null} if the supplied annotation is
	 * {@code null}; otherwise, the supplied annotation unmodified
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #synthesizeAnnotation(Annotation, AnnotatedElement)
	 */
	static <A extends Annotation> A synthesizeAnnotation(A annotation) {
		return synthesizeAnnotation(annotation, null);
	}

	/**
	 * <em>Synthesize</em> an annotation from the supplied {@code annotation}
	 * by wrapping it in a dynamic proxy that transparently enforces
	 * <em>attribute alias</em> semantics for annotation attributes that are
	 * annotated with {@link AliasFor @AliasFor}.
	 * @param annotation the annotation to synthesize
	 * @param annotatedElement the element that is annotated with the supplied
	 * annotation; may be {@code null} if unknown
	 * @return the synthesized annotation if the supplied annotation is
	 * <em>synthesizable</em>; {@code null} if the supplied annotation is
	 * {@code null}; otherwise the supplied annotation unmodified
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #synthesizeAnnotation(Map, Class, AnnotatedElement)
	 * @see #synthesizeAnnotation(Class)
	 */
	public static <A extends Annotation> A synthesizeAnnotation(
			A annotation,  AnnotatedElement annotatedElement) {
		return synthesizeAnnotation(annotation, (Object) annotatedElement);
	}

	@SuppressWarnings("unchecked")
	static <A extends Annotation> A synthesizeAnnotation(A annotation, Object annotatedElement) {
		if (annotation instanceof SynthesizedAnnotation) {
			return annotation;
		}
		Class<? extends Annotation> annotationType = annotation.annotationType();
		if (!isSynthesizable(annotationType)) {
			return annotation;
		}

		DefaultAnnotationAttributeExtractor attributeExtractor =
				new DefaultAnnotationAttributeExtractor(annotation, annotatedElement,null);
		InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);

		// Can always expose Spring's SynthesizedAnnotation marker since we explicitly check for a
		// synthesizable annotation before (which needs to declare @AliasFor from the same package)
		Class<?>[] exposedInterfaces = new Class<?>[] {annotationType,SynthesizedAnnotation.class};
		return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
	}
	/**
	 * 处理子注解的同步操作
	 * @param <A>
	 * @param annotation
	 * @param annotatedElement
	 * @param extendAnn
	 * @return
	 */
	static <A extends Annotation> A synthesizeAnnotation(A annotation, AnnotatedElement annotatedElement,Annotation extendAnn) {
		if (annotation instanceof SynthesizedAnnotation) {
			return annotation;
		}
		Class<? extends Annotation> annotationType = annotation.annotationType();
		OverWriteAnnotation overWriter=isOverrideExtends(annotatedElement,extendAnn, annotation);
		if (!isSynthesizable(annotationType)&&overWriter==null) {
			return annotation;
		}
		
		DefaultAnnotationAttributeExtractor attributeExtractor =
				new DefaultAnnotationAttributeExtractor(annotation, annotatedElement,overWriter);
		InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);

		// Can always expose Spring's SynthesizedAnnotation marker since we explicitly check for a
		// synthesizable annotation before (which needs to declare @AliasFor from the same package)
		Class<?>[] exposedInterfaces = new Class<?>[] {annotationType,SynthesizedAnnotation.class};
		return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
	} 
	
	/**
	 * 是否注解属性进行重写
	 * @param <A>
	 * @param extendAnn
	 * @param annotation
	 * @return
	 */
	static  <A extends Annotation>  OverWriteAnnotation isOverrideExtends(AnnotatedElement annotatedElement,Annotation extendAnn,A annotation) {
		if(extendAnn==null) {
			return null;
		}
		extendAnn=getAnnotation(annotatedElement, extendAnn.annotationType());
		OverWriteAnnotation over=null;
		for (Method attribute : getAttributeMethods(extendAnn.annotationType())) {
			AliasDescriptor descriptor = AliasDescriptor.from(attribute);
			if(descriptor!=null&&descriptor.isOverrideFor(annotation.annotationType())){
				if(over==null) {
					over= new OverWriteAnnotation(extendAnn);
				}
				over.mappingWriteAttribute(descriptor.aliasedAttributeName, descriptor.sourceAttributeName);
			}
		}
		return over;
	}
	
	/**
	 * 是否注解属性进行重写
	 * @param <A>
	 * @param extendAnn
	 * @param annotation
	 * @return
	 */
	static  <A extends Annotation>  Map<String,ExtendsAttribute> isOverrideSuper(Annotation annotation) {
		Map<String,ExtendsAttribute> superAttributes=null;
		for (Method attribute : getAttributeMethods(annotation.annotationType())) {
			AliasDescriptor descriptor = AliasDescriptor.from(attribute);
			if(descriptor!=null){
				if(!descriptor.isAliasPair) {//不是一对的
					Class<Annotation> superClazz=(Class<Annotation>) descriptor.aliasedAnnotationType;
					Annotation superAnn=getAnnotation(annotation, superClazz);
					if(superAnn!=null) {
						Method superAttribute;
						try {
							superAttribute = descriptor.aliasedAnnotationType.getDeclaredMethod(descriptor.aliasedAttributeName);
						} catch (NoSuchMethodException e) {
							throw new AnnotationException("alias name "+descriptor.aliasedAttributeName+" error ");
						} catch (SecurityException e) {
							throw new AnnotationException(e);
						}
						ExtendsAttribute ea=new ExtendsAttribute(superAttribute,superAnn);
						if(superAttributes==null) {
							superAttributes=new HashMap<String,ExtendsAttribute>();
						}
						superAttributes.put(descriptor.sourceAttributeName, ea);
					}
				}
			}
		}
		return superAttributes;
	}

	/**
	 * <em>Synthesize</em> an annotation from the supplied map of annotation
	 * attributes by wrapping the map in a dynamic proxy that implements an
	 * annotation of the specified {@code annotationType} and transparently
	 * enforces <em>attribute alias</em> semantics for annotation attributes
	 * that are annotated with {@link AliasFor @AliasFor}.
	 * <p>The supplied map must contain a key-value pair for every attribute
	 * defined in the supplied {@code annotationType} that is not aliased or
	 * does not have a default value. Nested maps and nested arrays of maps
	 * will be recursively synthesized into nested annotations or nested
	 * arrays of annotations, respectively.
	 * <p>Note that {@link AnnotationAttributes} is a specialized type of
	 * {@link Map} that is an ideal candidate for this method's
	 * {@code attributes} argument.
	 * @param attributes the map of annotation attributes to synthesize
	 * @param annotationType the type of annotation to synthesize
	 * @param annotatedElement the element that is annotated with the annotation
	 * corresponding to the supplied attributes; may be {@code null} if unknown
	 * @return the synthesized annotation
	 * @throws IllegalArgumentException if a required attribute is missing or if an
	 * attribute is not of the correct type
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #synthesizeAnnotation(Annotation, AnnotatedElement)
	 * @see #synthesizeAnnotation(Class)
	 * @see #getAnnotationAttributes(AnnotatedElement, Annotation)
	 * @see #getAnnotationAttributes(AnnotatedElement, Annotation, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A synthesizeAnnotation(Map<String, Object> attributes,
			Class<A> annotationType,  AnnotatedElement annotatedElement) {

		MapAnnotationAttributeExtractor attributeExtractor =
				new MapAnnotationAttributeExtractor(attributes, annotationType, annotatedElement,null);
		InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
		Class<?>[] exposedInterfaces = (canExposeSynthesizedMarker(annotationType) ?
				new Class<?>[] {annotationType, SynthesizedAnnotation.class} : new Class<?>[] {annotationType});
		return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
	}
	
	/**
	 * Check whether we can expose our {@link SynthesizedAnnotation} marker for the given annotation type.
	 * @param annotationType the annotation type that we are about to create a synthesized proxy for
	 */
	private static boolean canExposeSynthesizedMarker(Class<? extends Annotation> annotationType) {
		try {
			return (Class.forName(SynthesizedAnnotation.class.getName(), false, annotationType.getClassLoader()) ==
					SynthesizedAnnotation.class);
		}
		catch (ClassNotFoundException ex) {
			return false;
		}
	}

	/**
	 * <em>Synthesize</em> an annotation from its default attributes values.
	 * <p>This method simply delegates to
	 * {@link #synthesizeAnnotation(Map, Class, AnnotatedElement)},
	 * supplying an empty map for the source attribute values and {@code null}
	 * for the {@link AnnotatedElement}.
	 * @param annotationType the type of annotation to synthesize
	 * @return the synthesized annotation
	 * @throws IllegalArgumentException if a required attribute is missing
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #synthesizeAnnotation(Map, Class, AnnotatedElement)
	 * @see #synthesizeAnnotation(Annotation, AnnotatedElement)
	 */
	public static <A extends Annotation> A synthesizeAnnotation(Class<A> annotationType) {
		return synthesizeAnnotation(Collections.<String, Object> emptyMap(), annotationType, null);
	}

	/**
	 * <em>Synthesize</em> an array of annotations from the supplied array
	 * of {@code annotations} by creating a new array of the same size and
	 * type and populating it with {@linkplain #synthesizeAnnotation(Annotation)
	 * synthesized} versions of the annotations from the input array.
	 * @param annotations the array of annotations to synthesize
	 * @param annotatedElement the element that is annotated with the supplied
	 * array of annotations; may be {@code null} if unknown
	 * @return a new array of synthesized annotations, or {@code null} if
	 * the supplied array is {@code null}
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #synthesizeAnnotation(Annotation, AnnotatedElement)
	 * @see #synthesizeAnnotation(Map, Class, AnnotatedElement)
	 */
	static Annotation[] synthesizeAnnotationArray(Annotation[] annotations,Object annotatedElement) {

		Annotation[] synthesized = (Annotation[]) Array.newInstance(
				annotations.getClass().getComponentType(), annotations.length);
		for (int i = 0; i < annotations.length; i++) {
			synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
		}
		return synthesized;
	}

	/**
	 * <em>Synthesize</em> an array of annotations from the supplied array
	 * of {@code maps} of annotation attributes by creating a new array of
	 * {@code annotationType} with the same size and populating it with
	 * {@linkplain #synthesizeAnnotation(Map, Class, AnnotatedElement)
	 * synthesized} versions of the maps from the input array.
	 * @param maps the array of maps of annotation attributes to synthesize
	 * @param annotationType the type of annotations to synthesize
	 * (never {@code null})
	 * @return a new array of synthesized annotations, or {@code null} if
	 * the supplied array is {@code null}
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2.1
	 * @see #synthesizeAnnotation(Map, Class, AnnotatedElement)
	 * @see #synthesizeAnnotationArray(Annotation[], Object)
	 */
	@SuppressWarnings("unchecked")
	static <A extends Annotation> A[] synthesizeAnnotationArray( Map<String, Object>[] maps, Class<A> annotationType) {
		if (maps == null) {
			return null;
		}

		A[] synthesized = (A[]) Array.newInstance(annotationType, maps.length);
		for (int i = 0; i < maps.length; i++) {
			synthesized[i] = synthesizeAnnotation(maps[i], annotationType, null);
		}
		return synthesized;
	}

	/**
	 * Get a map of all attribute aliases declared via {@code @AliasFor}
	 * in the supplied annotation type.
	 * <p>The map is keyed by attribute name with each value representing
	 * a list of names of aliased attributes.
	 * <p>For <em>explicit</em> alias pairs such as x and y (i.e., where x
	 * is an {@code @AliasFor("y")} and y is an {@code @AliasFor("x")}, there
	 * will be two entries in the map: {@code x -> (y)} and {@code y -> (x)}.
	 * <p>For <em>implicit</em> aliases (i.e., attributes that are declared
	 * as attribute overrides for the same attribute in the same meta-annotation),
	 * there will be n entries in the map. For example, if x, y, and z are
	 * implicit aliases, the map will contain the following entries:
	 * {@code x -> (y, z)}, {@code y -> (x, z)}, {@code z -> (x, y)}.
	 * <p>An empty return value implies that the annotation does not declare
	 * any attribute aliases.
	 * @param annotationType the annotation type to find attribute aliases in
	 * @return a map containing attribute aliases (never {@code null})
	 * @since 4.2
	 */
	static Map<String, List<String>> getAttributeAliasMap( Class<? extends Annotation> annotationType) {
		if (annotationType == null) {
			return Collections.emptyMap();
		}

		Map<String, List<String>> map = attributeAliasesCache.get(annotationType);
		if (map != null) {
			return map;
		}

		map = new LinkedHashMap<>();
		for (Method attribute : getAttributeMethods(annotationType)) {
			List<String> aliasNames = getAttributeAliasNames(attribute);
			if (!aliasNames.isEmpty()) {
				map.put(attribute.getName(), aliasNames);
			}
		}

		attributeAliasesCache.put(annotationType, map);
		return map;
	}


	/**
	 * Determine if annotations of the supplied {@code annotationType} are
	 * <em>synthesizable</em> (i.e., in need of being wrapped in a dynamic
	 * proxy that provides functionality above that of a standard JDK
	 * annotation).
	 * <p>Specifically, an annotation is <em>synthesizable</em> if it declares
	 * any attributes that are configured as <em>aliased pairs</em> via
	 * {@link AliasFor @AliasFor} or if any nested annotations used by the
	 * annotation declare such <em>aliased pairs</em>.
	 * @since 4.2
	 * @see SynthesizedAnnotation
	 * @see SynthesizedAnnotationInvocationHandler
	 */
	@SuppressWarnings("unchecked")
	private static boolean isSynthesizable(Class<? extends Annotation> annotationType) {
		Boolean synthesizable = synthesizableCache.get(annotationType);
		if (synthesizable != null) {
			return synthesizable;
		}

		synthesizable = Boolean.FALSE;
		for (Method attribute : getAttributeMethods(annotationType)) {
			if (!getAttributeAliasNames(attribute).isEmpty()) {
				synthesizable = Boolean.TRUE;
				break;
			}
			Class<?> returnType = attribute.getReturnType();
			if (Annotation[].class.isAssignableFrom(returnType)) {
				Class<? extends Annotation> nestedAnnotationType =
						(Class<? extends Annotation>) returnType.getComponentType();
				if (isSynthesizable(nestedAnnotationType)) {
					synthesizable = Boolean.TRUE;
					break;
				}
			}
			else if (Annotation.class.isAssignableFrom(returnType)) {
				Class<? extends Annotation> nestedAnnotationType = (Class<? extends Annotation>) returnType;
				if (isSynthesizable(nestedAnnotationType)) {
					synthesizable = Boolean.TRUE;
					break;
				}
			}
			AliasDescriptor alias=AliasDescriptor.from(attribute);
			if(alias!=null) {
				synthesizable= Boolean.TRUE;
				break;
			}
		}

		synthesizableCache.put(annotationType, synthesizable);
		return synthesizable;
	}

	/**
	 * Get the names of the aliased attributes configured via
	 * {@link AliasFor @AliasFor} for the supplied annotation {@code attribute}.
	 * @param attribute the attribute to find aliases for
	 * @return the names of the aliased attributes (never {@code null}, though
	 * potentially <em>empty</em>)
	 * @throws IllegalArgumentException if the supplied attribute method is
	 * {@code null} or not from an annotation
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 * @see #getAttributeOverrideName(Method, Class)
	 */
	static List<String> getAttributeAliasNames(Method attribute) {
		AliasDescriptor descriptor = AliasDescriptor.from(attribute);
		return (descriptor != null ? descriptor.getAttributeAliasNames() : Collections.<String> emptyList());
	}

	/**
	 * Get the name of the overridden attribute configured via
	 * {@link AliasFor @AliasFor} for the supplied annotation {@code attribute}.
	 * @param attribute the attribute from which to retrieve the override
	 * (never {@code null})
	 * @param metaAnnotationType the type of meta-annotation in which the
	 * overridden attribute is allowed to be declared
	 * @return the name of the overridden attribute, or {@code null} if not
	 * found or not applicable for the specified meta-annotation type
	 * @throws IllegalArgumentException if the supplied attribute method is
	 * {@code null} or not from an annotation, or if the supplied meta-annotation
	 * type is {@code null} or {@link Annotation}
	 * @throws AnnotationConfigurationException if invalid configuration of
	 * {@code @AliasFor} is detected
	 * @since 4.2
	 */
	
	static String getAttributeOverrideName(Method attribute,  Class<? extends Annotation> metaAnnotationType) {
		AliasDescriptor descriptor = AliasDescriptor.from(attribute);
		if(descriptor != null && metaAnnotationType != null ) {
			return descriptor.getAttributeOverrideName(metaAnnotationType);
		}
		return null;
	}

	/**
	 * Get all methods declared in the supplied {@code annotationType} that
	 * match Java's requirements for annotation <em>attributes</em>.
	 * <p>All methods in the returned list will be
	 * {@linkplain ReflectionUtils#makeAccessible(Method) made accessible}.
	 * @param annotationType the type in which to search for attribute methods
	 * (never {@code null})
	 * @return all annotation attribute methods in the specified annotation
	 * type (never {@code null}, though potentially <em>empty</em>)
	 * @since 4.2
	 */
	static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
		List<Method> methods = attributeMethodsCache.get(annotationType);
		if (methods != null) {
			return methods;
		}

		methods = new ArrayList<>();
		for (Method method : annotationType.getDeclaredMethods()) {
			if (isAttributeMethod(method)) {
				MethodUtils.makeAccessible(method);
				methods.add(method);
			}
		}

		attributeMethodsCache.put(annotationType, methods);
		return methods;
	}

	/**
	 * Get the annotation with the supplied {@code annotationName} on the
	 * supplied {@code element}.
	 * @param element the element to search on
	 * @param annotationName the fully qualified class name of the annotation
	 * type to find
	 * @return the annotation if found; {@code null} otherwise
	 * @since 4.2
	 */
	
	static Annotation getAnnotation(AnnotatedElement element, String annotationName) {
		for (Annotation annotation : element.getAnnotations()) {
			if (annotation.annotationType().getName().equals(annotationName)) {
				return annotation;
			}
		}
		return null;
	}

	/**
	 * Determine if the supplied {@code method} is an annotation attribute method.
	 * @param method the method to check
	 * @return {@code true} if the method is an attribute method
	 * @since 4.2
	 */
	static boolean isAttributeMethod( Method method) {
		return (method != null && method.getParameterTypes().length == 0 && method.getReturnType() != void.class);
	}

	/**
	 * Determine if the supplied method is an "annotationType" method.
	 * @return {@code true} if the method is an "annotationType" method
	 * @since 4.2
	 * @see Annotation#annotationType()
	 */
	static boolean isAnnotationTypeMethod( Method method) {
		return (method != null && method.getName().equals("annotationType") && method.getParameterTypes().length == 0);
	}

	/**
	 * Resolve the container type for the supplied repeatable {@code annotationType}.
	 * <p>Automatically detects a <em>container annotation</em> declared via
	 * {@link java.lang.annotation.Repeatable}. If the supplied annotation type
	 * is not annotated with {@code @Repeatable}, this method simply returns
	 * {@code null}.
	 * @since 4.2
	 */
	
	static Class<? extends Annotation> resolveContainerAnnotationType(Class<? extends Annotation> annotationType) {
//		Repeatable repeatable = getAnnotation(annotationType, Repeatable.class);
//		return (repeatable != null ? repeatable.value() : null);
		return null;
	}


	/**
	 * Handle the supplied annotation introspection exception.
	 * <p>If the supplied exception is an {@link AnnotationConfigurationException},
	 * it will simply be thrown, allowing it to propagate to the caller, and
	 * nothing will be logged.
	 * <p>Otherwise, this method logs an introspection failure (in particular
	 * {@code TypeNotPresentExceptions}) before moving on, assuming nested
	 * Class values were not resolvable within annotation attributes and
	 * thereby effectively pretending there were no annotations on the specified
	 * element.
	 * @param element the element that we tried to introspect annotations on
	 * @param ex the exception that we encountered
	 * @see #rethrowAnnotationConfigurationException
	 */
	static void handleIntrospectionFailure(AnnotatedElement element, Throwable ex) {
		if(ex instanceof AnnotationException) {
			throw (AnnotationException)ex;//是注解异常时抛出
		}

		if (element instanceof Class && Annotation.class.isAssignableFrom((Class<?>) element)) {
			// Meta-annotation or (default) value lookup on an annotation type
			if (logger.isDebugEnabled()) {
				logger.debug("Failed to meta-introspect annotation " + element + ": " + ex);
			}
		}
		else {
			// Direct annotation lookup on regular Class, Method, Field
			if (logger.isInfoEnabled()) {
				logger.info("Failed to introspect annotations on " + element + ": " + ex);
			}
		}
	}

	/**
	 * Clear the internal annotation metadata cache.
	 * @since 4.3.15
	 */
	public static void clearCache() {
		findAnnotationCache.clear();
		getAnnotationCache.clear();
		metaPresentCache.clear();
		annotatedBaseTypeCache.clear();
		synthesizableCache.clear();
		attributeAliasesCache.clear();
		attributeMethodsCache.clear();
		aliasDescriptorCache.clear();
	}


	/**
	 * Cache key for the AnnotatedElement cache.
	 */
	private static final class AnnotationCacheKey implements Comparable<AnnotationCacheKey> {

		private final AnnotatedElement element;

		private final Class<? extends Annotation> annotationType;

		public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
			this.element = element;
			this.annotationType = annotationType;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof AnnotationCacheKey)) {
				return false;
			}
			AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
			return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
		}

		@Override
		public int hashCode() {
			return (this.element.hashCode() * 29 + this.annotationType.hashCode());
		}

		@Override
		public String toString() {
			return "@" + this.annotationType + " on " + this.element;
		}

		@Override
		public int compareTo(AnnotationCacheKey other) {
			int result = this.element.toString().compareTo(other.element.toString());
			if (result == 0) {
				result = this.annotationType.getName().compareTo(other.annotationType.getName());
			}
			return result;
		}
	}


	private static class AnnotationCollector<A extends Annotation> {

		private final Class<A> annotationType;

		
		private final Class<? extends Annotation> containerAnnotationType;

		private final boolean declaredMode;

		private final Set<AnnotatedElement> visited = new HashSet<>();

		private final Set<A> result = new LinkedHashSet<>();

		AnnotationCollector(Class<A> annotationType,
				 Class<? extends Annotation> containerAnnotationType, boolean declaredMode) {

			this.annotationType = annotationType;
			this.containerAnnotationType = (containerAnnotationType != null ? containerAnnotationType :
					resolveContainerAnnotationType(annotationType));
			this.declaredMode = declaredMode;
		}

		Set<A> getResult(AnnotatedElement element) {
			process(element);
			return Collections.unmodifiableSet(this.result);
		}

		@SuppressWarnings("unchecked")
		private void process(AnnotatedElement element) {
			if (this.visited.add(element)) {
				try {
					Annotation[] annotations = (this.declaredMode ? element.getDeclaredAnnotations() : element.getAnnotations());
					for (Annotation ann : annotations) {
						Class<? extends Annotation> currentAnnotationType = ann.annotationType();
						if (Objects.equals(this.annotationType, currentAnnotationType)) {
							this.result.add(synthesizeAnnotation((A) ann, element));
						}
						else if (Objects.equals(this.containerAnnotationType, currentAnnotationType)) {
							this.result.addAll(getValue(element, ann));
						}
						else if (!isInJavaLangAnnotationPackage(currentAnnotationType)) {
							process(currentAnnotationType);
						}
					}
				}
				catch (Throwable ex) {
					handleIntrospectionFailure(element, ex);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private List<A> getValue(AnnotatedElement element, Annotation annotation) {
			try {
				List<A> synthesizedAnnotations = new ArrayList<>();
				A[] value = (A[]) AnnotationUtils.getValue(annotation);
				if (value != null) {
					for (A anno : value) {
						synthesizedAnnotations.add(synthesizeAnnotation(anno, element));
					}
				}
				return synthesizedAnnotations;
			}
			catch (Throwable ex) {
				handleIntrospectionFailure(element, ex);
			}
			// Unable to read value from repeating annotation container -> ignore it.
			return Collections.emptyList();
		}
	}


	/**
	 * {@code AliasDescriptor} encapsulates the declaration of {@code @AliasFor}
	 * on a given annotation attribute and includes support for validating
	 * the configuration of aliases (both explicit and implicit).
	 * @since 4.2.1
	 * @see #from
	 * @see #getAttributeAliasNames
	 * @see #getAttributeOverrideName
	 */
	static final class AliasDescriptor {
		/**源属性*/
		private final Method sourceAttribute;
		/**源注解*/
		private final Class<? extends Annotation> sourceAnnotationType;
		
		private final String sourceAttributeName;
		/**别名对应的属性*/
		private final Method aliasedAttribute;
		/***/
		private final Class<? extends Annotation> aliasedAnnotationType;

		private final String aliasedAttributeName;
		/**是否是同一个注解类型中的一对别名*/
		private final boolean isAliasPair;

		/**
		 * 从注解的属性方法中初始化出别名描述对象
		 * 如不存在别名注解返回null
		 * @param attribute 注解的属性方法
		 * @return
		 */
		public static AliasDescriptor from(Method attribute) {
			AliasDescriptor descriptor = aliasDescriptorCache.get(attribute);
			if (descriptor != null) {
				return descriptor;
			}

			AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
			if (aliasFor == null) {
				return null;
			}

			descriptor = new AliasDescriptor(attribute, aliasFor);
			descriptor.validate();
			aliasDescriptorCache.put(attribute, descriptor);
			return descriptor;
		}

		/**
		 * 别名描述的构建方法
		 * @param sourceAttribute
		 * @param aliasFor
		 */
		private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
			Class<?> declaringClass = sourceAttribute.getDeclaringClass();

			this.sourceAttribute = sourceAttribute;
			this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
			this.sourceAttributeName = sourceAttribute.getName();

			this.aliasedAnnotationType = (Annotation.class == aliasFor.annotation() ?
					this.sourceAnnotationType : aliasFor.annotation());
			this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
			if (this.aliasedAnnotationType == this.sourceAnnotationType &&
					this.aliasedAttributeName.equals(this.sourceAttributeName)) {
				//别名不可以指定为自己
				String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to " +
						"itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.",
						sourceAttribute.getName(), declaringClass.getName());
				throw new AnnotationException(msg);
			}
			try {
				this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName);
			}catch (NoSuchMethodException ex) {//别名注解类没有指定名称的属性时
				String msg = String.format(
						"Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].",
						this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
						this.aliasedAnnotationType.getName());
				throw new AnnotationException(msg, ex);
			}

			this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
		}

		/**对别名属性进行数据校验*/
		private void validate() {
			// Target annotation is not meta-present?
			if (!this.isAliasPair && !isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType)) {
				String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares " +
						"an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.",
						this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
						this.aliasedAnnotationType.getName());
				throw new AnnotationException(msg);
			}

			if (this.isAliasPair) {
				AliasFor mirrorAliasFor = this.aliasedAttribute.getAnnotation(AliasFor.class);
				if (mirrorAliasFor == null) {
					String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].",
							this.aliasedAttributeName, this.sourceAnnotationType.getName(), this.sourceAttributeName);
					throw new AnnotationException(msg);
				}
			}
			//对别名对应的返回类型进行校验，返回类型不一样的不能互为别名
			Class<?> returnType = this.sourceAttribute.getReturnType();
			Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
			if (returnType != aliasedReturnType &&
					(!aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType())) {
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
						"and attribute '%s' in annotation [%s] must declare the same return type.",
						this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
						this.aliasedAnnotationType.getName());
				throw new AnnotationException(msg);
			}

			if (this.isAliasPair) {
				validateDefaultValueConfiguration(this.aliasedAttribute);
			}
		}
		/**
		 *	对属性的默认值进行校验
		 * @param aliasedAttribute
		 */
		private void validateDefaultValueConfiguration(Method aliasedAttribute) {
			Object defaultValue = this.sourceAttribute.getDefaultValue();
			Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();

			if (defaultValue == null || aliasedDefaultValue == null) {
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
						"and attribute '%s' in annotation [%s] must declare default values.",
						this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
						aliasedAttribute.getDeclaringClass().getName());
				throw new AnnotationException(msg);
			}
			//两个属性的默认值不一样时抛出异常
			if (!Objects.equals(defaultValue, aliasedDefaultValue)) {
				String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
						"and attribute '%s' in annotation [%s] must declare the same default value.",
						this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
						aliasedAttribute.getDeclaringClass().getName());
				throw new AnnotationException(msg);
			}
		}

		/**
		 * Validate this descriptor against the supplied descriptor.
		 * <p>This method only validates the configuration of default values
		 * for the two descriptors, since other aspects of the descriptors
		 * are validated when they are created.
		 */
		private void validateAgainst(AliasDescriptor otherDescriptor) {
			validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
		}

		/**
		 * Determine if this descriptor represents an explicit override for
		 * an attribute in the supplied {@code metaAnnotationType}.
		 * @see #isAliasFor
		 */
		private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType) {
			return (this.aliasedAnnotationType == metaAnnotationType);
		}

		/**
		 * Determine if this descriptor and the supplied descriptor both
		 * effectively represent aliases for the same attribute in the same
		 * target annotation, either explicitly or implicitly.
		 * <p>This method searches the attribute override hierarchy, beginning
		 * with this descriptor, in order to detect implicit and transitively
		 * implicit aliases.
		 * @return {@code true} if this descriptor and the supplied descriptor
		 * effectively alias the same annotation attribute
		 * @see #isOverrideFor
		 */
		private boolean isAliasFor(AliasDescriptor otherDescriptor) {
			for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
				for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
					if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
						return true;
					}
				}
			}
			return false;
		}

		public List<String> getAttributeAliasNames() {
			// Explicit alias pair?
			if (this.isAliasPair) {
				return Collections.singletonList(this.aliasedAttributeName);
			}

			// Else: search for implicit aliases
			List<String> aliases = new ArrayList<>();
			for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
				if (this.isAliasFor(otherDescriptor)) {
					this.validateAgainst(otherDescriptor);
					aliases.add(otherDescriptor.sourceAttributeName);
				}
			}
			return aliases;
		}
		/**
		 * 获取源注解的其它别人描述
		 * @return
		 */
		private List<AliasDescriptor> getOtherDescriptors() {
			List<AliasDescriptor> otherDescriptors = new ArrayList<>();
			for (Method currentAttribute : getAttributeMethods(this.sourceAnnotationType)) {
				if (!this.sourceAttribute.equals(currentAttribute)) {
					AliasDescriptor otherDescriptor = AliasDescriptor.from(currentAttribute);
					if (otherDescriptor != null) {
						otherDescriptors.add(otherDescriptor);
					}
				}
			}
			return otherDescriptors;
		}

		/**
		 * 从指定的注解获取对就的别名
		 * @param metaAnnotationType
		 * @return
		 */
		public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
			// Search the attribute override hierarchy, starting with the current attribute
			for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
				if (desc.isOverrideFor(metaAnnotationType)) {
					return desc.aliasedAttributeName;
				}
			}

			// Else: explicit attribute override for a different meta-annotation
			return null;
		}

		
		private AliasDescriptor getAttributeOverrideDescriptor() {
			if (this.isAliasPair) {
				return null;
			}
			return AliasDescriptor.from(this.aliasedAttribute);
		}

		/**
		 * Get the name of the aliased attribute configured via the supplied
		 * {@link AliasFor @AliasFor} annotation on the supplied {@code attribute},
		 * or the original attribute if no aliased one specified (indicating that
		 * the reference goes to a same-named attribute on a meta-annotation).
		 * <p>This method returns the value of either the {@code attribute}
		 * or {@code value} attribute of {@code @AliasFor}, ensuring that only
		 * one of the attributes has been declared while simultaneously ensuring
		 * that at least one of the attributes has been declared.
		 * @param aliasFor the {@code @AliasFor} annotation from which to retrieve
		 * the aliased attribute name
		 * @param attribute the attribute that is annotated with {@code @AliasFor}
		 * @return the name of the aliased attribute (never {@code null} or empty)
		 * @throws AnnotationConfigurationException if invalid configuration of
		 * {@code @AliasFor} is detected
		 */
		private String getAliasedAttributeName(AliasFor aliasFor, Method attribute) {
			String attributeName = aliasFor.value();
			boolean attributeDeclared = StringUtils.hasText(attributeName);
			if(!attributeDeclared) {
				if(aliasFor.annotation()!=Annotation.class) {//是对应其它注解的别名时可以取当前属性的名称
					attributeName=attribute.getName();
					attributeDeclared=true;
				}
			}
			// Ensure user did not declare both 'value' and 'attribute' in @AliasFor
			if (!attributeDeclared) {
				String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' " +
						"and its alias 'value' are present with values of [%s], but only one is permitted.",
						attribute.getName(), attribute.getDeclaringClass().getName(), attributeName);
				throw new AnnotationException(msg);
			}

			// Either explicit attribute name or pointing to same-named attribute by default
			return (StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName());
		}

		@Override
		public String toString() {
			return String.format("%s: @%s(%s) is an alias for @%s(%s)", getClass().getSimpleName(),
					this.sourceAnnotationType.getSimpleName(), this.sourceAttributeName,
					this.aliasedAnnotationType.getSimpleName(), this.aliasedAttributeName);
		}
	}
	/**
	 * 用于对继承的属性进行封装
	 * @author 胡真山
	 *
	 */
	@NullAnnotation
	static class ExtendsAttribute {
		/** 继承来的属性方法 */
		Method attribute;
		/** 继承的父注解 */
		Annotation superAnnotation;

		ExtendsAttribute(Method attribute, Annotation superAnnotation) {
			this.attribute = attribute;
			this.superAnnotation = superAnnotation;
		}
		/**调用发展的结果*/
		Object invoke() {
			return MethodUtils.invoke(superAnnotation, attribute);
		}
	}


	private static class DefaultValueHolder {

		final Object defaultValue;

		public DefaultValueHolder(Object defaultValue) {
			this.defaultValue = defaultValue;
		}
	}

}
