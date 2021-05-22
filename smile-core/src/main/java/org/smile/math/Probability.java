package org.smile.math;

import java.util.Random;
/**
 * 根据几率 计算 是否命中
 * @author 胡真山
 *
 */
public class Probability{
	
	public static final int gailv=100000;//总概率  
	/**随机种子*/
	public  static final Random random=new Random();	
    /**
     * 
     * probability/denominator 比率形式
     * @param probability 分子
     * @param denominator 分母
     */
    public  static boolean isGenerate(int probability,int denominator)
    {
    	if (probability >= gailv) {
    		return true;
    	}
    	if(denominator==0){denominator=gailv;}
    	int random_seed=random.nextInt(denominator);
    	return probability  > random_seed;
    }
    /**
     * 根据几率  计算是否生成 
     * @param probability 
     * @return
     */
    public  static boolean defaultIsGenerate(int probability)
    {
    	 int random_seed=random.nextInt(gailv);
    	 return probability>random_seed;
    }
    /**
     * 随机一个值 在min到100000之间
     * @param min
     * @return 包含min 100000
     */
    public static int  randomValue(int min)
    {
    	int temp = gailv - min;
		temp = random.nextInt(temp + 1);
		temp = temp + min;
		return temp;
    }
    /**
     * 从 min 和 max 中间随机一个值
     *  @param min
     *  @param max
     * @return 包含min max
     */
    public static int  randomValue(int min,int max)
    {
    	int temp = max - min;
		temp = random.nextInt(temp + 1);
		temp = temp + min;
		return temp;
    }
    
    
     /**
      * 返回在0-maxcout之间产生的随机数时候小于num
      * @param num
      * @return
      */
    public static boolean isGenerateToBoolean(int num,int maxcout){
    	double count=Math.random()*maxcout;
    	if(num>count){
    		return true;	
    	}
    	return false;
    }
    /**
     * 返回在0-maxcout之间产生的随机数时候小于num
     * @param num
     * @return
     */
   public static boolean isGenerateToBoolean(double num,int maxcout){
   	double count=Math.random()*maxcout;
   	if(num>count){
   		return true;	
   	}
   	return false;
   }
   /**
    * 随机产生min到max之间的整数值 包括min max
    * @param min
    * @param max
    * @return
    */
   public static int randomIntValue(int min,int max){
	   return (int)(Math.random() * (double)(max - min + 1)) + min;  
   }
   
   /**
    * 随机一个float类型的数字
    * @param min
    * @param max
    * @return
    */
   public static float randomFloatValue(float min,float max){
	   return (float)(Math.random() * (double)(max-min)) + min;  
   }
   
   /**
	 * 从min到max随机，包括min跟max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */

	public static int randomGetInt(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("maxValue must > minValue");
		} else if (min == max) {
			return min;
		} else {
			int interval = max - min + 1;
			int value = min + random.nextInt(interval);
			return value;
		}
	}
	/**
	 * 从min到max随机，包括min不包括max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	
	public static int randomValueNoBlockMax(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("maxValue must > minValue");
		} else if (min == max) {
			return min;
		} else {
			int interval = max - min;
			int value = min +random.nextInt(interval);
			return value;
		}
	}

	/**
	 * 随机一个boolean型的值
	 * @return
	 */
	public static boolean randomGetBoolean() {
		return random.nextBoolean();
	}
  
}
