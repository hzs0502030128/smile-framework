package org.smile.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.commons.Strings;
import org.smile.reflect.MethodUtils;


public class Objects {
	
	private static final String NULL_STRING = "null";
	private static final String ARRAY_START = "{";
	private static final String ARRAY_END = "}";
	private static final String EMPTY_ARRAY_STR = ARRAY_START + ARRAY_END;
	private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
	/**空数组*/
	public static  final Object[] EMPTY_ARRAY=new Object[0];

	 /**
     * <p>Singleton used as a <code>null</code> placeholder where
     * <code>null</code> has another meaning.</p>
     *
     * <p>For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.</p>
     *
     * <p>Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.</p>
     *
     * <p>This instance is Serializable.</p>
     */
    public static final Null NULL = new Null();
    

    // Defaulting
    //-----------------------------------------------------------------------
    /**
     * <p>Returns a default value if the object passed is
     * <code>null</code>.</p>
     * 
     * <pre>
     * ObjectUtils.defaultIfNull(null, null)      = null
     * ObjectUtils.defaultIfNull(null, "")        = ""
     * ObjectUtils.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtils.defaultIfNull("abc", *)        = "abc"
     * ObjectUtils.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param object  the <code>Object</code> to test, may be <code>null</code>
     * @param defaultValue  the default value to return, may be <code>null</code>
     * @return <code>object</code> if it is not <code>null</code>, defaultValue otherwise
     */
    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * <p>Compares two objects for equality, where either one or both
     * objects may be <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.equals(null, null)                  = true
     * ObjectUtils.equals(null, "")                    = false
     * ObjectUtils.equals("", null)                    = false
     * ObjectUtils.equals("", "")                      = true
     * ObjectUtils.equals(Boolean.TRUE, null)          = false
     * ObjectUtils.equals(Boolean.TRUE, "true")        = false
     * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
     * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
     * </pre>
     *
     * @param object1  the first object, may be <code>null</code>
     * @param object2  the second object, may be <code>null</code>
     * @return <code>true</code> if the values of both objects are the same
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    /**
     * <p>Compares two objects for inequality, where either one or both
     * objects may be <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.notEqual(null, null)                  = false
     * ObjectUtils.notEqual(null, "")                    = true
     * ObjectUtils.notEqual("", null)                    = true
     * ObjectUtils.notEqual("", "")                      = false
     * ObjectUtils.notEqual(Boolean.TRUE, null)          = true
     * ObjectUtils.notEqual(Boolean.TRUE, "true")        = true
     * ObjectUtils.notEqual(Boolean.TRUE, Boolean.TRUE)  = false
     * ObjectUtils.notEqual(Boolean.TRUE, Boolean.FALSE) = true
     * </pre>
     *
     * @param object1  the first object, may be <code>null</code>
     * @param object2  the second object, may be <code>null</code>
     * @return <code>false</code> if the values of both objects are the same
     * @since 2.6
     */
    public static boolean notEquals(Object object1, Object object2) {
        return equals(object1, object2) == false;
    }

    /**
     * <p>Gets the hash code of an object returning zero when the
     * object is <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.hashCode(null)   = 0
     * ObjectUtils.hashCode(obj)    = obj.hashCode()
     * </pre>
     *
     * @param obj  the object to obtain the hash code of, may be <code>null</code>
     * @return the hash code of the object, or zero if null
     * @since 2.1
     */
    public static int hashCode(Object obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }

    // Identity ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.identityToString(null)         = null
     * ObjectUtils.identityToString("")           = "java.lang.String@1e23"
     * ObjectUtils.identityToString(Boolean.TRUE) = "java.lang.Boolean@7fa"
     * </pre>
     *
     * @param object  the object to create a toString for, may be
     *  <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *  <code>null</code> passed in
     */
    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        identityToString(buffer, object);
        return buffer.toString();
    }

    /**
     * <p>Appends the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will throw a NullPointerException for either of the two parameters. </p>
     *
     * <pre>
     * ObjectUtils.identityToString(buf, "")            = buf.append("java.lang.String@1e23"
     * ObjectUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa"
     * ObjectUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
     * </pre>
     *
     * @param buffer  the buffer to append to
     * @param object  the object to create a toString for
     * @since 2.4
     */
    public static void identityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        buffer.append(object.getClass().getName())
              .append('@')
              .append(Integer.toHexString(System.identityHashCode(object)));
    }

    /**
     * <p>Appends the toString that would be produced by <code>Object</code>
     * if a class did not override toString itself. <code>null</code>
     * will return <code>null</code>.</p>
     *
     * <pre>
     * ObjectUtils.appendIdentityToString(*, null)            = null
     * ObjectUtils.appendIdentityToString(null, "")           = "java.lang.String@1e23"
     * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = "java.lang.Boolean@7fa"
     * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
     * </pre>
     *
     * @param buffer  the buffer to append to, may be <code>null</code>
     * @param object  the object to create a toString for, may be <code>null</code>
     * @return the default toString text, or <code>null</code> if
     *  <code>null</code> passed in
     * @since 2.0
     * @deprecated The design of this method is bad - see LANG-360. Instead, use identityToString(StringBuffer, Object).
     */
    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer
            .append(object.getClass().getName())
            .append('@')
            .append(Integer.toHexString(System.identityHashCode(object)));
    }

    // ToString
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * an empty string ("") if <code>null</code> input.</p>
     * 
     * <pre>
     * ObjectUtils.toString(null)         = ""
     * ObjectUtils.toString("")           = ""
     * ObjectUtils.toString("bat")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE) = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * <p>Gets the <code>toString</code> of an <code>Object</code> returning
     * a specified text if <code>null</code> input.</p>
     * 
     * <pre>
     * ObjectUtils.toString(null, null)           = null
     * ObjectUtils.toString(null, "null")         = "null"
     * ObjectUtils.toString("", "null")           = ""
     * ObjectUtils.toString("bat", "null")        = "bat"
     * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
     * </pre>
     * 
     * @see StringUtils#defaultString(String,String)
     * @see String#valueOf(Object)
     * @param obj  the Object to <code>toString</code>, may be null
     * @param nullStr  the String to return if <code>null</code> input, may be null
     * @return the passed in Object's toString, or nullStr if <code>null</code> input
     * @since 2.0
     */
    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    // Min/Max
    //-----------------------------------------------------------------------
    /**
     * Null safe comparison of Comparables.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @return
     *  <ul>
     *   <li>If both objects are non-null and unequal, the lesser object.
     *   <li>If both objects are non-null and equal, c1.
     *   <li>If one of the comparables is null, the non-null object.
     *   <li>If both the comparables are null, null is returned.
     *  </ul>
     */
    public static Object min(Comparable c1, Comparable c2) {
        return (compare(c1, c2, true) <= 0 ? c1 : c2);
    }

    /**
     * Null safe comparison of Comparables.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @return
     *  <ul>
     *   <li>If both objects are non-null and unequal, the greater object.
     *   <li>If both objects are non-null and equal, c1.
     *   <li>If one of the comparables is null, the non-null object.
     *   <li>If both the comparables are null, null is returned.
     *  </ul>
     */
    public static Object max(Comparable c1, Comparable c2) {
        return (compare(c1, c2, false) >= 0 ? c1 : c2);
    }

    /**
     * Null safe comparison of Comparables.
     * {@code null} is assumed to be less than a non-{@code null} value.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @return a negative value if c1 < c2, zero if c1 = c2
     * and a positive value if c1 > c2
     * @since 2.6
     */
    public static int compare(Comparable c1, Comparable c2) {
        return compare(c1, c2, false);
    }

    /**
     * Null safe comparison of Comparables.
     * 
     * @param c1  the first comparable, may be null
     * @param c2  the second comparable, may be null
     * @param nullGreater if true <code>null</code> is considered greater
     * than a Non-<code>null</code> value or if false <code>null</code> is
     * considered less than a Non-<code>null</code> value
     * @return a negative value if c1 < c2, zero if c1 = c2
     * and a positive value if c1 > c2
     * @see java.util.Comparator#compare(Object, Object)
     * @since 2.6
     */
    public static int compare(Comparable c1, Comparable c2, boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        } else if (c1 == null) {
            return (nullGreater ? 1 : -1);
        } else if (c2 == null) {
            return (nullGreater ? -1 : 1);
        }
        return c1.compareTo(c2);
    }
    
    /**
     * Clone an object.
     * 
     * @param o the object to clone
     * @return the clone if the object implements {@link Cloneable} otherwise <code>null</code>
     * @throws CloneFailedException if the object is cloneable and the clone operation fails
     * @since 2.6
     */
    public static Object clone(final Object o) {
        if (o instanceof Cloneable) {
            final Object result;
            if (o.getClass().isArray()) {
                final Class componentType = o.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    result = ((Object[])o).clone();
                } else {
                    int length = Array.getLength(o);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0) {
                        Array.set(result, length, Array.get(o, length));
                    }
                }
            } else {
                try {
                	Method method=MethodUtils.getMethod(o.getClass(), "clone");
                	if(method==null) {
                        throw new SmileRunException("Cloneable type "
                            + o.getClass().getName()
                            + " has no clone method");
                	}
                    result =method.invoke(o);
                } catch (final IllegalAccessException e) {
                    throw new SmileRunException("Cannot clone Cloneable type "
                        + o.getClass().getName(), e);
                } catch (final InvocationTargetException e) {
                    throw new SmileRunException("Exception cloning Cloneable type "
                        + o.getClass().getName(), e.getTargetException());
                }
            }
            return result;
        }

        return null;
    }

    /**
     * Clone an object if possible. This method is similar to {@link #clone(Object)}, but will
     * return the provided instance as the return value instead of <code>null</code> if the instance
     * is not cloneable. This is more convenient if the caller uses different
     * implementations (e.g. of a service) and some of the implementations do not allow concurrent
     * processing or have state. In such cases the implementation can simply provide a proper
     * clone implementation and the caller's code does not have to change.
     * 
     * @param o the object to clone
     * @return the clone if the object implements {@link Cloneable} otherwise the object itself
     * @throws CloneFailedException if the object is cloneable and the clone operation fails
     * @since 2.6
     */
    public static Object cloneIfPossible(final Object o) {
        final Object clone = clone(o);
        return clone == null ? o : clone;
    }
    
    /**
	 * Determine whether the given object is an array:
	 * either an Object array or a primitive array.
	 * @param obj the object to check
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
	}

	/**
	 * Determine whether the given array is empty:
	 * i.e. {@code null} or of zero length.
	 * @param array the array to check
	 * @see #isEmpty(Object)
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * Determine whether the given object is empty.
	 * <p>This method supports the following object types.
	 * <ul>
	 * <li>{@code Array}: considered empty if its length is zero</li>
	 * <li>{@link CharSequence}: considered empty if its length is zero</li>
	 * <li>{@link Collection}: delegates to {@link Collection#isEmpty()}</li>
	 * <li>{@link Map}: delegates to {@link Map#isEmpty()}</li>
	 * </ul>
	 * <p>If the given object is non-null and not one of the aforementioned
	 * supported types, this method returns {@code false}.
	 * @param obj the object to check
	 * @return {@code true} if the object is {@code null} or <em>empty</em>
	 * @since 4.2
	 * @see ObjectUtils#isEmpty(Object[])
	 * @see StringUtils#hasLength(CharSequence)
	 * @see StringUtils#isEmpty(Object)
	 * @see CollectionUtils#isEmpty(java.util.Collection)
	 * @see CollectionUtils#isEmpty(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}

		// else
		return false;
	}

	/**
	 * Check whether the given array contains the given element.
	 * @param array the array to check (may be {@code null},
	 * in which case the return value will always be {@code false})
	 * @param element the element to check for
	 * @return whether the element has been found in the given array
	 */
	public static boolean containsElement(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (Object arrayEle : array) {
			if (nullSafeEquals(arrayEle, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given array of enum constants contains a constant with the given name,
	 * ignoring case when determining a match.
	 * @param enumValues the enum values to check, typically the product of a call to MyEnum.values()
	 * @param constant the constant name to find (must not be null or empty string)
	 * @return whether the constant has been found in the given array
	 */
	public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
		return containsConstant(enumValues, constant, false);
	}

	/**
	 * Check whether the given array of enum constants contains a constant with the given name.
	 * @param enumValues the enum values to check, typically the product of a call to MyEnum.values()
	 * @param constant the constant name to find (must not be null or empty string)
	 * @param caseSensitive whether case is significant in determining a match
	 * @return whether the constant has been found in the given array
	 */
	public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
		for (Enum<?> candidate : enumValues) {
			if (caseSensitive ?
					candidate.toString().equals(constant) :
					candidate.toString().equalsIgnoreCase(constant)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Case insensitive alternative to {@link Enum#valueOf(Class, String)}.
	 * @param <E> the concrete Enum type
	 * @param enumValues the array of all Enum constants in question, usually per Enum.values()
	 * @param constant the constant to get the enum value of
	 * @throws IllegalArgumentException if the given constant is not found in the given array
	 * of enum values. Use {@link #containsConstant(Enum[], String)} as a guard to avoid this exception.
	 */
	public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
		for (E candidate : enumValues) {
			if (candidate.toString().equalsIgnoreCase(constant)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException(
				String.format("constant [%s] does not exist in enum type %s",
						constant, enumValues.getClass().getComponentType().getName()));
	}

	/**
	 * Append the given object to the given array, returning a new array
	 * consisting of the input array contents plus the given object.
	 * @param array the array to append to (can be {@code null})
	 * @param obj the object to append
	 * @return the new array (of the same component type; never {@code null})
	 */
	public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
		Class<?> compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		}
		else if (obj != null) {
			compType = obj.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		@SuppressWarnings("unchecked")
		A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	/**
	 * Convert the given array (which may be a primitive array) to an
	 * object array (if necessary of primitive wrapper objects).
	 * <p>A {@code null} source value will be converted to an
	 * empty Object array.
	 * @param source the (potentially primitive) array
	 * @return the corresponding object array (never {@code null})
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class<?> wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}


	//---------------------------------------------------------------------
	// Convenience methods for content-based equality/hash-code handling
	//---------------------------------------------------------------------

	/**
	 * Determine if the given objects are equal, returning {@code true}
	 * if both are {@code null} or {@code false} if only one is
	 * {@code null}.
	 * <p>Compares arrays with {@code Arrays.equals}, performing an equality
	 * check based on the array elements rather than the array reference.
	 * @param o1 first Object to compare
	 * @param o2 second Object to compare
	 * @return whether the given objects are equal
	 * @see java.util.Arrays#equals
	 */
	public static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			if (o1 instanceof Object[] && o2 instanceof Object[]) {
				return Arrays.equals((Object[]) o1, (Object[]) o2);
			}
			if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
			}
			if (o1 instanceof byte[] && o2 instanceof byte[]) {
				return Arrays.equals((byte[]) o1, (byte[]) o2);
			}
			if (o1 instanceof char[] && o2 instanceof char[]) {
				return Arrays.equals((char[]) o1, (char[]) o2);
			}
			if (o1 instanceof double[] && o2 instanceof double[]) {
				return Arrays.equals((double[]) o1, (double[]) o2);
			}
			if (o1 instanceof float[] && o2 instanceof float[]) {
				return Arrays.equals((float[]) o1, (float[]) o2);
			}
			if (o1 instanceof int[] && o2 instanceof int[]) {
				return Arrays.equals((int[]) o1, (int[]) o2);
			}
			if (o1 instanceof long[] && o2 instanceof long[]) {
				return Arrays.equals((long[]) o1, (long[]) o2);
			}
			if (o1 instanceof short[] && o2 instanceof short[]) {
				return Arrays.equals((short[]) o1, (short[]) o2);
			}
		}
		return false;
	}

	/**
	 * Return as hash code for the given object; typically the value of
	 * {@code Object#hashCode()}}. If the object is an array,
	 * this method will delegate to any of the {@code nullSafeHashCode}
	 * methods for arrays in this class. If the object is {@code null},
	 * this method returns 0.
	 * @see #nullSafeHashCode(Object[])
	 * @see #nullSafeHashCode(boolean[])
	 * @see #nullSafeHashCode(byte[])
	 * @see #nullSafeHashCode(char[])
	 * @see #nullSafeHashCode(double[])
	 * @see #nullSafeHashCode(float[])
	 * @see #nullSafeHashCode(int[])
	 * @see #nullSafeHashCode(long[])
	 * @see #nullSafeHashCode(short[])
	 */
	public static int nullSafeHashCode(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj.getClass().isArray()) {
			if (obj instanceof Object[]) {
				return nullSafeHashCode((Object[]) obj);
			}
			if (obj instanceof boolean[]) {
				return nullSafeHashCode((boolean[]) obj);
			}
			if (obj instanceof byte[]) {
				return nullSafeHashCode((byte[]) obj);
			}
			if (obj instanceof char[]) {
				return nullSafeHashCode((char[]) obj);
			}
			if (obj instanceof double[]) {
				return nullSafeHashCode((double[]) obj);
			}
			if (obj instanceof float[]) {
				return nullSafeHashCode((float[]) obj);
			}
			if (obj instanceof int[]) {
				return nullSafeHashCode((int[]) obj);
			}
			if (obj instanceof long[]) {
				return nullSafeHashCode((long[]) obj);
			}
			if (obj instanceof short[]) {
				return nullSafeHashCode((short[]) obj);
			}
		}
		return obj.hashCode();
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(Object[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(boolean[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(byte[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(char[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(double[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(float[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(int[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(long[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return a hash code based on the contents of the specified array.
	 * If {@code array} is {@code null}, this method returns 0.
	 */
	public static int nullSafeHashCode(short[] array) {
		return HashCode.hash(HashCode.SEED, array);
	}

	/**
	 * Return the same value as {@link Boolean#hashCode()}}.
	 * @see Boolean#hashCode()
	 */
	public static int hashCode(boolean bool) {
		return (bool ? 1231 : 1237);
	}

	/**
	 * Return the same value as {@link Double#hashCode()}}.
	 * @see Double#hashCode()
	 */
	public static int hashCode(double dbl) {
		return hashCode(Double.doubleToLongBits(dbl));
	}

	/**
	 * Return the same value as {@link Float#hashCode()}}.
	 * @see Float#hashCode()
	 */
	public static int hashCode(float flt) {
		return Float.floatToIntBits(flt);
	}

	/**
	 * Return the same value as {@link Long#hashCode()}}.
	 * @see Long#hashCode()
	 */
	public static int hashCode(long lng) {
		return (int) (lng ^ (lng >>> 32));
	}


	//---------------------------------------------------------------------
	// Convenience methods for toString output
	//---------------------------------------------------------------------


	/**
	 * Return a hex String form of an object's identity hash code.
	 * @param obj the object
	 * @return the object's identity code in hex notation
	 */
	public static String getIdentityHexString(Object obj) {
		return Integer.toHexString(System.identityHashCode(obj));
	}

	/**
	 * Return a content-based String representation if {@code obj} is
	 * not {@code null}; otherwise returns an empty String.
	 * <p>Differs from {@link #nullSafeToString(Object)} in that it returns
	 * an empty String rather than "null" for a {@code null} value.
	 * @param obj the object to build a display String for
	 * @return a display String representation of {@code obj}
	 * @see #nullSafeToString(Object)
	 */
	public static String getDisplayString(Object obj) {
		if (obj == null) {
			return Strings.BLANK;
		}
		return nullSafeToString(obj);
	}

	/**
	 * Determine the class name for the given object.
	 * <p>Returns {@code "null"} if {@code obj} is {@code null}.
	 * @param obj the object to introspect (may be {@code null})
	 * @return the corresponding class name
	 */
	public static String nullSafeClassName(Object obj) {
		return (obj != null ? obj.getClass().getName() : Strings.BLANK);
	}

	/**
	 * Return a String representation of the specified Object.
	 * <p>Builds a String representation of the contents in case of an array.
	 * Returns {@code "null"} if {@code obj} is {@code null}.
	 * @param obj the object to build a String representation for
	 * @return a String representation of {@code obj}
	 */
	public static String nullSafeToString(Object obj) {
		if (obj == null) {
			return Strings.BLANK;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Object[]) {
			return nullSafeToString((Object[]) obj);
		}
		if (obj instanceof boolean[]) {
			return nullSafeToString((boolean[]) obj);
		}
		if (obj instanceof byte[]) {
			return nullSafeToString((byte[]) obj);
		}
		if (obj instanceof char[]) {
			return nullSafeToString((char[]) obj);
		}
		if (obj instanceof double[]) {
			return nullSafeToString((double[]) obj);
		}
		if (obj instanceof float[]) {
			return nullSafeToString((float[]) obj);
		}
		if (obj instanceof int[]) {
			return nullSafeToString((int[]) obj);
		}
		if (obj instanceof long[]) {
			return nullSafeToString((long[]) obj);
		}
		if (obj instanceof short[]) {
			return nullSafeToString((short[]) obj);
		}
		String str = obj.toString();
		return (str != null ? str : Strings.BLANK);
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(Object[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(String.valueOf(array[i]));
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(boolean[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(byte[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(char[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append("'").append(array[i]).append("'");
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(double[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(float[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}

			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(int[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(long[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}

	/**
	 * Return a String representation of the contents of the specified array.
	 * <p>The String representation consists of a list of the array's elements,
	 * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
	 * by the characters {@code ", "} (a comma followed by a space). Returns
	 * {@code "null"} if {@code array} is {@code null}.
	 * @param array the array to build a String representation for
	 * @return a String representation of {@code array}
	 */
	public static String nullSafeToString(short[] array) {
		if (array == null) {
			return NULL_STRING;
		}
		int length = array.length;
		if (length == 0) {
			return EMPTY_ARRAY_STR;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(ARRAY_START);
			}
			else {
				sb.append(ARRAY_ELEMENT_SEPARATOR);
			}
			sb.append(array[i]);
		}
		sb.append(ARRAY_END);
		return sb.toString();
	}


    // Null
    //-----------------------------------------------------------------------
    /**
     * <p>Class used as a null placeholder where <code>null</code>
     * has another meaning.</p>
     *
     * <p>For example, in a <code>HashMap</code> the
     * {@link java.util.HashMap#get(java.lang.Object)} method returns
     * <code>null</code> if the <code>Map</code> contains
     * <code>null</code> or if there is no matching key. The
     * <code>Null</code> placeholder can be used to distinguish between
     * these two cases.</p>
     *
     * <p>Another example is <code>Hashtable</code>, where <code>null</code>
     * cannot be stored.</p>
     */
    public static class Null implements Serializable {
        /**
         * Required for serialization support. Declare serialization compatibility with Commons Lang 1.0
         * 
         * @see java.io.Serializable
         */
        private static final long serialVersionUID = 7092611880189329093L;
        
        /**
         * Restricted constructor - singleton.
         */
        Null() {
            super();
        }
        
        /**
         * <p>Ensure singleton.</p>
         * 
         * @return the singleton value
         */
        private Object readResolve() {
            return NULL;
        }
    }
}
