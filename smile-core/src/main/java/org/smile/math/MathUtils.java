package org.smile.math;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.smile.beans.converter.ConvertException;
import org.smile.beans.converter.type.DoubleConverter;
import org.smile.beans.converter.type.IntegerConverter;
import org.smile.collection.CollectionUtils;
import org.smile.commons.SmileRunException;
import org.smile.log.LoggerHandler;

/**
 * @author huzhenshan
 *
 */
public class MathUtils implements LoggerHandler {
	/**
	 * 汉语中数字大写
	 */
	private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	/**
	 * 汉语中货币单位大写，这样的设计类似于占位符
	 */
	private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
	/**
	 * 特殊字符：整
	 */
	private static final String CN_FULL = "整";
	/**
	 * 特殊字符：负
	 */
	private static final String CN_NEGATIVE = "负";
	/**
	 * 金额的精度，默认值为2
	 */
	private static final int MONEY_PRECISION = 2;
	/**
	 * 特殊字符：零元整
	 */
	private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
	/***/
	private static final List NUMBER_CLASS=CollectionUtils.arrayList(Integer.class,Long.class,Double.class,BigDecimal.class);
	/**
	 * 向下取整
	 * @param number
	 * @return
	 */
	public static int floor(double number) {
		return (int) Math.floor(number);
	}
	
	public double pow(Number a, Number b){
		return StrictMath.pow(a.doubleValue(), b.doubleValue());
	}
	/**
	 * 除法 
	 * @param number
	 * @param divisor
	 * @param scale  保留小数位
	 * @return
	 */
	public static Number divide(Number number,Number divisor,int scale){
		BigDecimal res=new BigDecimal(number.toString()).divide(new BigDecimal(divisor.toString()),scale,BigDecimal.ROUND_HALF_UP);
		Number value= convert(res);
		int n1=NUMBER_CLASS.indexOf(number.getClass());
		int n2=NUMBER_CLASS.indexOf(divisor.getClass());
		int n3=NUMBER_CLASS.indexOf(value.getClass());
		int n=max(n1, n2,n3);
		switch(n){
			case 0:return value.intValue();
			case 1:return value.longValue();
			case 2:return value.doubleValue();
			case 3:return res;
			default:return value.intValue();
		}
	}
	/**
	 * 最多保存8位小数
	 * @param number
	 * @param divisor
	 * @return
	 */
	public static Number divide(Number number,Number divisor){
		return divide(number, divisor, 8);
	}
	/**
	 * 取最大值
	 * @param first
	 * @param i
	 * @return
	 */
	public static int max(int first,int ... i){
		Arrays.sort(i);
		return Math.max(first,i[i.length-1]);
	}
	
	/**
	 * 取最小值
	 * @param first
	 * @param i
	 * @return
	 */
	public static int min(int first,int ...i){
		Arrays.sort(i);
		return Math.min(first,i[0]);
	}
	
	/**
	 * 取最大值
	 * @param first
	 * @param i
	 * @return
	 */
	public static Number max(Number one,Number two){
		return one.doubleValue()<two.doubleValue()?two:one;
	}
	
	/**
	 * 取最小值
	 * @param first
	 * @param i
	 * @return
	 */
	public static Number min(Number one,Number two){
		return one.doubleValue()<two.doubleValue()?one:two;
	}
	/**
	   *    类型转换 可以转换时都往int型转换
	 * @param value
	 * @return
	 */
	public static Number convert(Number value){
		Number number=(Number)value;
		if(number.longValue()==number.intValue()){
			if(number.intValue()==number.doubleValue()){
				return number.intValue();
			}
			return number.doubleValue();
		}else{
			return number.longValue();
		}
	}

	/**
	 * 向上取整
	 * @param number
	 * @return
	 */
	public static int ceil(double number) {
		return (int) Math.ceil(number);
	}

	/**两个数字相减*/
	public static double subtract(double one, double two) {
		return new BigDecimal(one + "").subtract(new BigDecimal(two + "")).doubleValue();
	}

	/**两个整形相加,溢出时会抛出异常**/
	public static int addIntCheckFlow(int oldnum, int addnum) {
		if (oldnum >= Integer.MAX_VALUE - addnum) {
			throw new NumberOverflowException(oldnum+" + "+addnum+" over flow  max int");
		} else {
			return oldnum + addnum;
		}
	}

	/**两个整形相加是否溢出**/
	public static boolean addIsFlowInt(int oldnum, int addnum) {
		if (oldnum > Integer.MAX_VALUE - addnum) {
			return true;
		}
		return false;
	}

	/**
	 * 向下获取指定number数值的最大attom倍数的数值
	 * @param number
	 * @param atom
	 * @return
	 */
	public static double floorMultiple(double number, double atom) {
		double num = 0;
		num = number / atom;
		int a = (int) num;
		double b = num - a;
		return number - b * atom;
	}

	/** 向下获取指定number数值的最大attom倍数的数值*/
	public static int floorMultiple(int number, int atom) {
		double num = floorMultiple((double) number, (double) atom);
		return (int) num;
	}

	/**
	 * 向上获取指定number数值的最小attom倍数的数值
	 * @param number
	 * @param atom
	 * @return
	 */
	public static double ceilMultiple(double number, double atom) {
		double num = 0;
		num = number / atom;
		int a = (int) num;
		a++;
		double b = a - num;
		if (b == 1) {
			return number;
		}
		return number + b * atom;
	}

	/** 向上获取指定number数值的最小attom倍数的数值*/
	public static int ceilMultiple(int number, int atom) {
		int num = (int) ceilMultiple((double) number, (double) atom);
		return num;
	}

	/**
	 * 把一个map中的值减去另一个map中的值
	 * <k,100> - <k,90>  = <k,10>
	 * @param srcMap 源
	 * @param subMap
	 */
	public static void subInMapUseMap(Map<Object, Integer> srcMap, Map<Object, Integer> subMap) {
		if (subMap == null) {
			return;
		}
		for (Map.Entry<Object, Integer> entry : subMap.entrySet()) {
			Integer value = srcMap.get(entry.getKey());
			if (value == null) {
				continue;
			}
			value -= entry.getValue();
			srcMap.put(entry.getKey(), value);
		}
	}

	/**把一个map中的所有的值加到别一个map中*/
	public static void addIntMap2Map(Map<Object, Integer> srcMap, Map<Object, Integer> addMap) {
		if (addMap == null) {
			return;
		}
		for (Map.Entry<Object, Integer> entry : addMap.entrySet()) {
			Integer value = srcMap.get(entry.getKey());
			if (value == null) {
				value = 0;
			}
			value +=entry.getValue();
			srcMap.put(entry.getKey(), value);
		}
	}
	/**
	 * 往一个map中的数字增加值
	 * @param srcMap
	 * @param key
	 * @param val
	 */
	public static void addInt2IntMap(Map<Object, Integer> srcMap,Object key, int val) {
		if (srcMap == null) {
			return;
		}
		Integer value = srcMap.get(key);
		if (value == null) {
			value = 0;
		}
		value += val;
		srcMap.put(key, value);
	}

	/**取小数点后几位*/
	public static float round(float number, int point) {
		int num = (int) Math.pow(10, point);
		float newNumber = (int)(number * num+0.5)/(float)num;
		return newNumber;
	}
	
	/**取小数点后几位*/
	public static double round(Number number, int point) {
		return round(number.doubleValue(), point);
	}
	/**
	 * 四舍五入成int类型
	 * @param number
	 * @return
	 */
	public static int round(Number number){
		return (int)Math.round(number.doubleValue());
	}
	
	
	/**取小数点后几位*/
	public static double round(double number, int point) {
		int num = (int) Math.pow(10, point);
		double newNumber = (int)(number * num+0.5)/(double)num;
		return newNumber;
	}

	/**
	 * 是空或者0
	 * @param number
	 * @return
	 */
	public static boolean isNullOrZero(Number number) {
		if (number == null) {
			return true;
		} else {
			return number.doubleValue() == 0;
		}
	}

	/**
	 * 多个数字相加
	 * @param ints
	 * @return
	 */
	public static int add(int... ints) {
		int result = 0;
		for (int i : ints) {
			result += i;
		}
		return result;
	}

	/**
	 * 把输入的金额转换为汉语中人民币的大写
	 * 
	 * @param numberOfMoney
	 *            输入的金额
	 * @return 对应的汉语大写
	 */
	public static String number2CNMontrayUnit(Number num) {
		BigDecimal numberOfMoney =new BigDecimal(num.toString());
		StringBuffer sb = new StringBuffer();
		// -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
		// positive.
		int signum = numberOfMoney.signum();
		// 零元整的情况
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}
		// 这里会进行金额的四舍五入
		long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
		// 得到小数点后两位值
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
		if (!(scale > 0)) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if ((scale > 0) && (!(scale % 10 > 0))) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			if (number <= 0) {
				break;
			}
			// 每次获取到最后一个数
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				if ((numIndex == 9) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
				}
				if ((numIndex == 13) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
				}
				sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				++zeroSize;
				if (!(getZero)) {
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				}
				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
				} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				}
				getZero = true;
			}
			// 让number每次都去掉最后一个数
			number = number / 10;
			++numIndex;
		}
		// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (!(scale > 0)) {
			sb.append(CN_FULL);
		}
		return sb.toString();
	}

	/**从数字区间随机出一个长度的数组,数组中的元素是不重复的，但不是大小有序的*/
	public static int[] randomArray(int min, int max, int size) {
		int len = max - min + 1;
		if (max < min || size > len) {
			throw new RuntimeException("不合的数字区间 min:" + min + " max:" + max + " size:" + size);
		}
		// 初始化给定范围的待选数组
		int[] source = new int[len];
		for (int i = min; i < min + len; i++) {
			source[i - min] = i;
		}
		int[] result = new int[size];
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			// 待选数组0到(len-2)随机一个下标
			index = Probability.randomGetInt(0, --len);
			// 将随机到的数放入结果集
			result[i] = source[index];
			// 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
			source[index] = source[len];
		}
		return result;
	}

	/**
	 * 向上取整
	 * @param number
	 * @return
	 */
	public static int ceil(Number number) {
		if (number == null) {
			return 0;
		}
		return ceil(number.doubleValue());
	}

	/**
	 * 向下取整
	 * @param number
	 * @return
	 */
	public static int floor(Number number) {
		if (number == null) {
			return 0;
		}
		return floor(number.doubleValue());
	}
	/**
	 * 大于等于此数字的2幂
	 * @return
	 */
	public static int ceilPowerOf2(int number){
		return Integer.highestOneBit((number - 1) << 1);
	}
	
	
	public static long ceilPowerOf2(long number){
		return Long.highestOneBit((number - 1) << 1);
	}
	
	/**
	 * 小于等于此数字的2幂
	 * @param num
	 * @return
	 */
	public static int floorPowerOf2(int num){
		int result=1;
		while(num>1){
			num=num>>1;
			result=result<<1;
		}
		return result;
	}
	/**
	 * 比较两个double型的大小
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static int compare(double lhs, double rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return +1;
        }
        // Need to compare bits to handle 0.0 == -0.0 being true
        // compare should put -0.0 < +0.0
        // Two NaNs are also == for compare purposes
        // where NaN == NaN is false
        long lhsBits = Double.doubleToLongBits(lhs);
        long rhsBits = Double.doubleToLongBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        // Something exotic! A comparison to NaN or 0.0 vs -0.0
        // Fortunately NaN's long is > than everything else
        // Also negzeros bits < poszero
        // NAN: 9221120237041090560
        // MAX: 9218868437227405311
        // NEGZERO: -9223372036854775808
        if (lhsBits < rhsBits) {
            return -1;
        } else {
            return +1;
        }
    }

	/**
	 * 转换成int类型
	 * @param number
	 * @return
	 */
	public static int integer(Object number){
		if(number instanceof Number){
			return ((Number) number).intValue();
		}
		try {
			return IntegerConverter.instance.convert(number);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * 转换成double类型
	 * @param number
	 * @return
	 */
	public static double toDouble(Object number){
		if(number instanceof Number){
			return ((Number) number).doubleValue();
		}
		try {
			return DoubleConverter.instance.convert(number);
		} catch (ConvertException e) {
			throw new SmileRunException(e);
		}
	}
	/**
	 * 绝对值
	 * @param number
	 * @return
	 */
	public static double abs(Number number){
		return StrictMath.abs(number.doubleValue());
	}
	
	/**
	 * 开平方根
	 * @param number
	 * @return
	 */
	public static double sqrt(Number number){
		return StrictMath.sqrt(number.doubleValue());
	}
	/**
	 * 立方根
	 * @param number
	 * @return
	 */
	public static double cbrt(Number number){
		return StrictMath.cbrt(number.doubleValue());
	}
	
	public static double cos(Number number){
		return StrictMath.cos(number.doubleValue());
	}
	
	public static double tan(Number number){
		return StrictMath.tan(number.doubleValue());
	}
	
	public static double sin(Number number){
		return StrictMath.sin(number.doubleValue());
	}
	
	public static double acos(Number number){
		return StrictMath.acos(number.doubleValue());
	}
	
	public static double asin(Number number){
		return StrictMath.asin(number.doubleValue());
	}
	
	public static double log(Number number){
		return StrictMath.log(number.doubleValue());
	}
	
	
	public static double log10(Number number){
		return StrictMath.log10(number.doubleValue());
	}
	
	public static double atan(Number number){
		return StrictMath.atan(number.doubleValue());
	}
	/**
	 * 随机数
	 * @return
	 */
	public static double random(){
		return StrictMath.random();
	}
	
	/**
     * 从 min 和 max 中间随机一个值
     *  @param min
     *  @param max
     * @return 包含min max
     */
    public static int  randomInt(int min,int max)
    {
    	int temp = max - min;
		temp = Probability.random.nextInt(temp + 1);
		temp = temp + min;
		return temp;
    }
    /**
     * 模运
     * @param n
     * @param m
     * @return
     */
    public static Number mod(Number n,Number m){
    	if(n instanceof Short&&m instanceof Short){
    		return n.intValue()%m.intValue();
    	}
    	if(n instanceof Long&&m instanceof Long){
    		return n.longValue()%m.longValue();
    	}
    	if(n instanceof Double||m instanceof Double){
    		return n.doubleValue()%m.doubleValue();
    	}
    	if(n instanceof Float||m instanceof Float){
    		return n.floatValue()%m.floatValue();
    	}
    	return n.intValue()%m.intValue();
    }
	
}
