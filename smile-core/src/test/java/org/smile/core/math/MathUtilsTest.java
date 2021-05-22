package org.smile.core.math;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.smile.collection.CollectionUtils;
import org.smile.commons.LongIdGenerator;
import org.smile.commons.SmileException;
import org.smile.commons.SmileRunException;
import org.smile.math.MathUtils;
import org.smile.math.NumberOverflowException;
import org.smile.util.UUIDGenerator;

import junit.framework.TestCase;
/**
 * 对数学工具类测试
 * @author 胡真山
 *
 */
public class MathUtilsTest extends TestCase{
	
	public void testCompare(){
		assertEquals(MathUtils.compare(0.0,-0.0),1);
	}
	/**
	 * 测试数字相加
	 */
	public void testIntAdd(){
		boolean error=false;
		try{
			MathUtils.addIntCheckFlow(Integer.MAX_VALUE,100);
		}catch(NumberOverflowException e){
			error=true;
		}
		assertEquals(error,true);
		assertEquals(MathUtils.addIntCheckFlow(100, 130), 230);
	}
	
	public void testFloorAndCeil(){
		assertEquals(MathUtils.floor(10.56), 10);
		assertEquals(MathUtils.ceil(10.56), 11);
		assertEquals(MathUtils.ceil(new BigDecimal(10.67)), 11);
		assertEquals(MathUtils.floor(new BigDecimal(10.67)), 10);
	}
	
	public void testMapAdd(){
		Map map=CollectionUtils.hashMap("age", 100);
		MathUtils.addInt2IntMap(map, "age", 2);
		assertEquals(map.get("age"), 102);
		MathUtils.addInt2IntMap(map, "m", 2);
		assertEquals(map.get("m"), 2);
	}
	
	public void testPow(){
		int result=MathUtils.ceilPowerOf2(32);
		assertEquals(result, 32);
		result=MathUtils.floorPowerOf2(65);
		assertEquals(result, 64);
	}
	
	public void testMod(){
		int m=8;
		int i=1;
		for(i=1;i<10000;i++){
			assertEquals(i%m, i&(m-1));
		}
		Object s=new Object[]{};
		System.out.println(s.getClass());
		System.out.println(MathUtils.mod(3,2));
	}
	
	public void testFormat(){
		float f=1.2446f;
		assertEquals(1.245f, MathUtils.round(f, 3));
		assertEquals(1, Math.round(f));
	}
	
	
	public void testInt() {
		
		for(int i=0;i<10;i++) {
			System.out.println(LongIdGenerator.generateId());
			System.out.println(UUIDGenerator.uuid());
		}
		Set<Long> set=new HashSet<>();
		long l=System.currentTimeMillis();
		for(int i=0;i<1000000;i++) {
			long s=LongIdGenerator.generateId();
			if(s<0) {
				throw new SmileRunException(""+s);
			}
			set.add(s);
			
		}
		System.out.println(System.currentTimeMillis()-l);
		System.out.println(set.size());
	}
	
	
}
