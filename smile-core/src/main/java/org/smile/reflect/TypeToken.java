package org.smile.reflect;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

public class TypeToken<T> {
	
	static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};
	//类型
	final Type type;
	/**泛型*/
	final Class<? super T> rawType;
	final int hashCode;

	protected TypeToken() {
		this.type = getSuperclassTypeParameter(getClass());
		this.rawType = (Class<? super T>) getRawType(type);
		this.hashCode = type.hashCode();
	}
	
	protected TypeToken(Class rawType) {
		this.rawType=rawType;
		this.type = getSuperclassTypeParameter(getClass());
		this.hashCode = type.hashCode();
	}
	
	public Type getType(){
		return this.type;
	}
	
	public static ParameterizedType newParameterizedType(Type rawType,Class ownerType){
		return new ParameterizedTypeImpl(ownerType, rawType, new Type[]{ownerType});
	}

	Type getSuperclassTypeParameter(Class<?> subclass) {
		return getSuperclassTypeParameter(subclass,this.rawType);
	}
	
	public static Type getSuperclassTypeParameter(Class<?> subclass,Class rawTypeClass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return canonicalize(parameterized.getActualTypeArguments()[0],rawTypeClass);
	}

	private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
		
		private final Type componentType;

		public GenericArrayTypeImpl(Type componentType) {
			this.componentType = canonicalize(componentType);
		}

		public Type getGenericComponentType() {
			return componentType;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof GenericArrayType && eq(this, (GenericArrayType) o);
		}

		@Override
		public int hashCode() {
			return componentType.hashCode();
		}

		@Override
		public String toString() {
			return typeToString(componentType) + "[]";
		}

		private static final long serialVersionUID = 0;
	}

	private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
		private final Type ownerType;
		private final Type rawType;
		private final Type[] typeArguments;

		public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
			// require an owner type if the raw type needs it
			if (rawType instanceof Class<?>) {
				Class<?> rawTypeAsClass = (Class<?>) rawType;
				boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null;
				checkArgument(ownerType != null || isStaticOrTopLevelClass);
			}

			this.ownerType = ownerType == null ? null : canonicalize(ownerType);
			this.rawType = canonicalize(rawType);
			this.typeArguments = typeArguments.clone();
			for (int t = 0, length = this.typeArguments.length; t < length; t++) {
				checkNotNull(this.typeArguments[t]);
				checkNotPrimitive(this.typeArguments[t]);
				this.typeArguments[t] = canonicalize(this.typeArguments[t]);
			}
		}
		@Override
		public Type[] getActualTypeArguments() {
			return typeArguments.clone();
		}
		@Override
		public Type getRawType() {
			return rawType;
		}
		@Override
		public Type getOwnerType() {
			return ownerType;
		}
		
		

		@Override
		public boolean equals(Object other) {
			return other instanceof ParameterizedType && eq(this, (ParameterizedType) other);
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(typeArguments) ^ rawType.hashCode() ^ hashCodeOrZero(ownerType);
		}

		@Override
		public String toString() {
			int length = typeArguments.length;
			if (length == 0) {
				return typeToString(rawType);
			}

			StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
			stringBuilder.append(typeToString(rawType)).append("<").append(typeToString(typeArguments[0]));
			for (int i = 1; i < length; i++) {
				stringBuilder.append(", ").append(typeToString(typeArguments[i]));
			}
			return stringBuilder.append(">").toString();
		}

		private static final long serialVersionUID = 0;
	}

	private static final class WildcardTypeImpl implements WildcardType, Serializable {
		private final Type upperBound;
		private final Type lowerBound;

		public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
			checkArgument(lowerBounds.length <= 1);
			checkArgument(upperBounds.length == 1);

			if (lowerBounds.length == 1) {
				checkNotNull(lowerBounds[0]);
				checkNotPrimitive(lowerBounds[0]);
				checkArgument(upperBounds[0] == Object.class);
				this.lowerBound = canonicalize(lowerBounds[0]);
				this.upperBound = Object.class;

			} else {
				checkNotNull(upperBounds[0]);
				checkNotPrimitive(upperBounds[0]);
				this.lowerBound = null;
				this.upperBound = canonicalize(upperBounds[0]);
			}
		}
		@Override
		public Type[] getUpperBounds() {
			return new Type[] { upperBound };
		}
		@Override
		public Type[] getLowerBounds() {
			return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof WildcardType && eq(this, (Type) other);
		}

		@Override
		public int hashCode() {
			// this equals Arrays.hashCode(getLowerBounds()) ^
			// Arrays.hashCode(getUpperBounds());
			return (lowerBound != null ? 31 + lowerBound.hashCode() : 1) ^ (31 + upperBound.hashCode());
		}

		@Override
		public String toString() {
			if (lowerBound != null) {
				return "? super " + typeToString(lowerBound);
			} else if (upperBound == Object.class) {
				return "?";
			} else {
				return "? extends " + typeToString(upperBound);
			}
		}

		private static final long serialVersionUID = 0;
	}
	
	public static Type canonicalize(Type type) {
		return canonicalize(type, null);
	}

	public static Type canonicalize(Type type,Class rawType) {
		if (type instanceof Class) {
			Class<?> c = (Class<?>) type;
			return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			Type ownerType=rawType==null? p.getOwnerType():rawType;
			Type[] typeArgs= rawType==null? p.getActualTypeArguments():new Type[]{rawType};
			return new ParameterizedTypeImpl(ownerType,p.getRawType(),typeArgs);
		} else if (type instanceof GenericArrayType) {
			GenericArrayType g = (GenericArrayType) type;
			return new GenericArrayTypeImpl(g.getGenericComponentType());

		} else if (type instanceof WildcardType) {
			WildcardType w = (WildcardType) type;
			return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
		} else {
			// type is either serializable as-is or unsupported
			return type;
		}
	}

	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			// type is a normal class.
			return (Class<?>) type;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			// I'm not exactly sure why getRawType() returns Type instead of
			// Class.
			// Neal isn't either but suspects some pathological case related
			// to nested classes exists.
			Type rawType = parameterizedType.getRawType();
			checkArgument(rawType instanceof Class);
			return (Class<?>) rawType;

		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			return Array.newInstance(getRawType(componentType), 0).getClass();

		} else if (type instanceof TypeVariable) {
			// we could use the variable's bounds, but that won't work if there
			// are multiple.
			// having a raw type that's more general than necessary is okay
			return Object.class;

		} else if (type instanceof WildcardType) {
			return getRawType(((WildcardType) type).getUpperBounds()[0]);

		} else {
			String className = type == null ? "null" : type.getClass().getName();
			throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type + "> is of type " + className);
		}
	}

	public static <T> T checkNotNull(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	public static void checkArgument(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException();
		}
	}

	static void checkNotPrimitive(Type type) {
		checkArgument(!(type instanceof Class<?>) || !((Class<?>) type).isPrimitive());
	}

	static boolean equal(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	static boolean eq(Type a, Type b) {
		return equals(a, b);
	}

	/**
	 * Returns true if {@code a} and {@code b} are equal.
	 */
	public static boolean equals(Type a, Type b) {
		if (a == b) {
			// also handles (a == null && b == null)
			return true;

		} else if (a instanceof Class) {
			// Class already specifies equals().
			return a.equals(b);

		} else if (a instanceof ParameterizedType) {
			if (!(b instanceof ParameterizedType)) {
				return false;
			}

			// TODO: save a .clone() call
			ParameterizedType pa = (ParameterizedType) a;
			ParameterizedType pb = (ParameterizedType) b;
			return equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

		} else if (a instanceof GenericArrayType) {
			if (!(b instanceof GenericArrayType)) {
				return false;
			}

			GenericArrayType ga = (GenericArrayType) a;
			GenericArrayType gb = (GenericArrayType) b;
			return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

		} else if (a instanceof WildcardType) {
			if (!(b instanceof WildcardType)) {
				return false;
			}

			WildcardType wa = (WildcardType) a;
			WildcardType wb = (WildcardType) b;
			return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

		} else if (a instanceof TypeVariable) {
			if (!(b instanceof TypeVariable)) {
				return false;
			}
			TypeVariable<?> va = (TypeVariable<?>) a;
			TypeVariable<?> vb = (TypeVariable<?>) b;
			return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());

		} else {
			// This isn't a type we support. Could be a generic array type,
			// wildcard type, etc.
			return false;
		}
	}

	static int hashCodeOrZero(Object o) {
		return o != null ? o.hashCode() : 0;
	}

	static String typeToString(Type type) {
		return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
	}

}
