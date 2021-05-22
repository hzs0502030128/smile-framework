package org.smile.db;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.smile.commons.Strings;
import org.smile.log.Logger;
import org.smile.log.LoggerFactory;

public class JdbcSupports {

	private static final Logger logger = LoggerFactory.getLogger(JdbcSupports.class);
	/**
	 * Convert java clases to java.sql.Type constant.
	 */
	private static final HashMap<Class, Integer> typeMap = new HashMap<Class, Integer>();

	static {
		typeMap.put(Byte.class, new Integer(java.sql.Types.TINYINT));
		typeMap.put(Short.class, new Integer(java.sql.Types.SMALLINT));
		typeMap.put(Integer.class, new Integer(java.sql.Types.INTEGER));
		typeMap.put(Long.class, new Integer(java.sql.Types.BIGINT));
		typeMap.put(Float.class, new Integer(java.sql.Types.REAL));
		typeMap.put(Double.class, new Integer(java.sql.Types.DOUBLE));
		typeMap.put(BigDecimal.class, new Integer(java.sql.Types.DECIMAL));
		typeMap.put(Boolean.class, java.sql.Types.BOOLEAN);
		typeMap.put(byte[].class, new Integer(java.sql.Types.VARBINARY));
		typeMap.put(java.sql.Date.class, new Integer(java.sql.Types.DATE));
		typeMap.put(java.sql.Time.class, new Integer(java.sql.Types.TIME));
		typeMap.put(java.sql.Timestamp.class, new Integer(java.sql.Types.TIMESTAMP));
		typeMap.put(String.class, new Integer(java.sql.Types.VARCHAR));
		typeMap.put(Blob.class, new Integer(java.sql.Types.LONGVARBINARY));
		typeMap.put(Clob.class, new Integer(java.sql.Types.LONGVARCHAR));
	}

	/**
	 * Hex constants to use in conversion routines.
	 */
	private static final char hex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Static utility Calendar object.
	 */
	private static final GregorianCalendar cal = new GregorianCalendar();

	/**
	 * Convert a byte[] object to a hex string.
	 *
	 * @param bytes
	 *            The byte array to convert.
	 * @return The hex equivalent as a <code>String</code>.
	 */
	public static String toHex(byte[] bytes) {
		int len = bytes.length;

		if (len > 0) {
			StringBuffer buf = new StringBuffer(len * 2);

			for (int i = 0; i < len; i++) {
				int b1 = bytes[i] & 0xFF;

				buf.append(hex[b1 >> 4]);
				buf.append(hex[b1 & 0x0F]);
			}

			return buf.toString();
		}

		return Strings.BLANK;
	}

	

	/**
	 * Get the JDBC type constant which matches the supplied Object type.
	 *
	 * @param value
	 *            The object to analyse.
	 * @return The JDBC type constant as an <code>int</code>.
	 */
	public static int getJdbcType(Object value) {
		if (value == null) {
			return java.sql.Types.NULL;
		}

		return getJdbcType(value.getClass());
	}

	/**
	 * Get the JDBC type constant which matches the supplied <code>Class</code>.
	 *
	 * @param typeClass
	 *            the <code>Class</code> to analyse
	 * @return the JDBC type constant as an <code>int</code>
	 */
	public static int getJdbcType(Class typeClass) {
		if (typeClass == null) {
			return java.sql.Types.OTHER;
		}

		Object type = typeMap.get(typeClass);

		if (type == null) {
			// not in typeMap - try recursion through superclass hierarchy
			return getJdbcType(typeClass.getSuperclass());
		}

		return ((Integer) type).intValue();
	}

	/**
	 * Get a String describing the supplied JDBC type constant.
	 *
	 * @param jdbcType
	 *            The constant to be decoded.
	 * @return The text decode of the type constant as a <code>String</code>.
	 */
	public static String getJdbcTypeName(int jdbcType) {
		switch (jdbcType) {
			case java.sql.Types.ARRAY:
				return "ARRAY";
			case java.sql.Types.BIGINT:
				return "BIGINT";
			case java.sql.Types.BINARY:
				return "BINARY";
			case java.sql.Types.BIT:
				return "BIT";
			case java.sql.Types.BLOB:
				return "BLOB";
			case java.sql.Types.BOOLEAN:
				return "BOOLEAN";
			case java.sql.Types.CHAR:
				return "CHAR";
			case java.sql.Types.CLOB:
				return "CLOB";
			case java.sql.Types.DATALINK:
				return "DATALINK";
			case java.sql.Types.DATE:
				return "DATE";
			case java.sql.Types.DECIMAL:
				return "DECIMAL";
			case java.sql.Types.DISTINCT:
				return "DISTINCT";
			case java.sql.Types.DOUBLE:
				return "DOUBLE";
			case java.sql.Types.FLOAT:
				return "FLOAT";
			case java.sql.Types.INTEGER:
				return "INTEGER";
			case java.sql.Types.JAVA_OBJECT:
				return "JAVA_OBJECT";
			case java.sql.Types.LONGVARBINARY:
				return "LONGVARBINARY";
			case java.sql.Types.LONGVARCHAR:
				return "LONGVARCHAR";
			case java.sql.Types.NULL:
				return "NULL";
			case java.sql.Types.NUMERIC:
				return "NUMERIC";
			case java.sql.Types.OTHER:
				return "OTHER";
			case java.sql.Types.REAL:
				return "REAL";
			case java.sql.Types.REF:
				return "REF";
			case java.sql.Types.SMALLINT:
				return "SMALLINT";
			case java.sql.Types.STRUCT:
				return "STRUCT";
			case java.sql.Types.TIME:
				return "TIME";
			case java.sql.Types.TIMESTAMP:
				return "TIMESTAMP";
			case java.sql.Types.TINYINT:
				return "TINYINT";
			case java.sql.Types.VARBINARY:
				return "VARBINARY";
			case java.sql.Types.VARCHAR:
				return "VARCHAR";
			default:
				return "ERROR";
		}
	}

	/**
	 * Retrieve the fully qualified java class name for the supplied JDBC Types
	 * constant.
	 *
	 * @param jdbcType
	 *            The JDBC Types constant.
	 * @return The fully qualified java class name as a <code>String</code>.
	 */
	public static Class getJdbcTypeJavaClass(int jdbcType) {
		switch (jdbcType) {
			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				return Boolean.class;
			case java.sql.Types.TINYINT:
			case java.sql.Types.SMALLINT:
			case java.sql.Types.INTEGER:
				return Integer.class;

			case java.sql.Types.BIGINT:
				return Long.class;

			case java.sql.Types.NUMERIC:
			case java.sql.Types.DECIMAL:
				return BigDecimal.class;

			case java.sql.Types.REAL:
				return Float.class;

			case java.sql.Types.FLOAT:
			case java.sql.Types.DOUBLE:
				return Double.class;

			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
				return String.class;

			case java.sql.Types.BINARY:
			case java.sql.Types.VARBINARY:
				return byte[].class;

			case java.sql.Types.LONGVARBINARY:
			case java.sql.Types.BLOB:
				return Blob.class;

			case java.sql.Types.LONGVARCHAR:
			case java.sql.Types.CLOB:
				return Clob.class;

			case java.sql.Types.DATE:
				return Date.class;

			case java.sql.Types.TIME:
				return Time.class;

			case java.sql.Types.TIMESTAMP:
				return Timestamp.class;
		}

		return Object.class;
	}
	/**
	 * Link the original cause to an <code>SQLWarning</code>.
	 * <p>
	 * This convenience method calls
	 * {@link #linkException(Exception, Throwable)} and casts the result for
	 * cleaner code elsewhere.
	 *
	 * @param sqle
	 *            The <code>SQLWarning</code> to enhance.
	 * @param cause
	 *            The <code>Throwable</code> to link.
	 * @return The original <code>SQLWarning</code> object.
	 */
	public static SQLWarning linkException(SQLWarning sqle, Throwable cause) {
		return (SQLWarning) linkException((Exception) sqle, cause);
	}

	/**
	 * Link the original cause to an <code>SQLException</code>.
	 * <p>
	 * This convenience method calls
	 * {@link #linkException(Exception, Throwable)} and casts the result for
	 * cleaner code elsewhere.
	 *
	 * @param sqle
	 *            The <code>SQLException</code> to enhance.
	 * @param cause
	 *            The <code>Throwable</code> to link.
	 * @return The original <code>SQLException</code> object.
	 */
	public static SQLException linkException(SQLException sqle, Throwable cause) {
		return (SQLException) linkException((Exception) sqle, cause);
	}

	/**
	 * Link the original cause to an <code>Exception</code>.
	 * <p>
	 * If running under JVM 1.4+ the <code>Throwable.initCause(Throwable)</code>
	 * method will be invoked to chain the exception, else the exception is
	 * logged via the {@link Logger} class. Modeled after the code written by
	 * Brian Heineman.
	 *
	 * @param exception
	 *            The <code>Exception</code> to enhance.
	 * @param cause
	 *            The <code>Throwable</code> to link.
	 * @return The original <code>Exception</code> object.
	 */
	public static Throwable linkException(Exception exception, Throwable cause) {
		Class exceptionClass = exception.getClass();
		Class[] parameterTypes = new Class[] { Throwable.class };
		Object[] arguments = new Object[] { cause };

		try {
			Method initCauseMethod = exceptionClass.getMethod("initCause", parameterTypes);
			initCauseMethod.invoke(exception, arguments);
		} catch (NoSuchMethodException e) {
			// Best we can do; this method does not exist in older JVMs.
			logger.debug(e);
		} catch (Exception e) {
			// Ignore all other exceptions. Do not prevent the main exception
			// from being returned if reflection fails for any reason.
		}

		return exception;
	}

	/**
	 * Convert a timestamp to a different Timezone.
	 *
	 * @param value
	 *            the timestamp value
	 * @param target
	 *            the <code>Calendar</code> containing the TimeZone
	 * @return the new timestamp value as a <code>long</code>
	 */
	public static long timeToZone(java.util.Date value, Calendar target) {
		synchronized (cal) {
			java.util.Date tmp = target.getTime();
			try {
				cal.setTime(value);
				if (value instanceof Timestamp) {
					// Not Running under 1.4 so need to add milliseconds
					cal.set(Calendar.MILLISECOND, ((Timestamp) value).getNanos() / 1000000);
				}
				target.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
				target.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
				target.set(Calendar.SECOND, cal.get(Calendar.SECOND));
				target.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
				target.set(Calendar.YEAR, cal.get(Calendar.YEAR));
				target.set(Calendar.MONTH, cal.get(Calendar.MONTH));
				target.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
				return target.getTime().getTime();
			} finally {
				target.setTime(tmp);
			}
		}
	}

	/**
	 * Convert a timestamp from a different Timezone.
	 * 
	 * @param value
	 *            the timestamp value.
	 * @param target
	 *            the Calendar containing the TimeZone.
	 * @return The new timestamp value as a <code>long</code>.
	 */
	public static long timeFromZone(java.util.Date value, Calendar target) {
		synchronized (cal) {
			java.util.Date tmp = target.getTime();
			try {
				target.setTime(value);
				if (value instanceof Timestamp) {
					// Not Running under 1.4 so need to add milliseconds
					target.set(Calendar.MILLISECOND, ((Timestamp) value).getNanos() / 1000000);
				}
				cal.set(Calendar.HOUR_OF_DAY, target.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, target.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, target.get(Calendar.SECOND));
				cal.set(Calendar.MILLISECOND, target.get(Calendar.MILLISECOND));
				cal.set(Calendar.YEAR, target.get(Calendar.YEAR));
				cal.set(Calendar.MONTH, target.get(Calendar.MONTH));
				cal.set(Calendar.DAY_OF_MONTH, target.get(Calendar.DAY_OF_MONTH));
				return cal.getTime().getTime();
			} finally {
				target.setTime(tmp);
			}
		}
	}

	/**
	 * Converts a LOB to the equivalent Java type, i.e. <code>Clob</code> to
	 * <code>String</code> and <code>Blob</code> to <code>byte[]</code>. If the
	 * value passed is not a LOB object, it is left unchanged and no exception
	 * is thrown; the idea is to transparently convert only LOBs.
	 *
	 * @param value
	 *            an object that may be a LOB
	 * @return if the value was a LOB, the equivalent Java object, otherwise the
	 *         original value
	 * @throws SQLException
	 *             if an error occurs while reading the LOB contents
	 */
	public static Object convertLOB(Object value) throws SQLException {
		if (value instanceof Clob) {
			Clob c = (Clob) value;
			return c.getSubString(1, (int) c.length());
		}

		if (value instanceof Blob) {
			Blob b = (Blob) value;
			return b.getBytes(1, (int) b.length());
		}

		return value;
	}

	/**
	 * Converts a LOB type constant to the equivalent Java type constant, i.e.
	 * <code>Types.CLOB</code> to <code>Types.LONGVARCHAR</code> and
	 * <code>Types.BLOB</code> to <code>Types.LONGVARBINARY</code>. If the type
	 * passed is not that of a LOB, it is left unchanged and no exception is
	 * thrown; the idea is to transparently convert only LOB types.
	 *
	 * @param type
	 *            a {@link Types} constant defining a JDBC type, possibly a LOB
	 * @return if the type was that of a LOB, the equivalent Java object type,
	 *         otherwise the original type
	 */
	public static int convertLOBType(int type) {
		switch (type) {
			case Types.BLOB:
				return Types.LONGVARBINARY;
			case Types.CLOB:
				return Types.LONGVARCHAR;
			default:
				return type;
		}
	}

}
