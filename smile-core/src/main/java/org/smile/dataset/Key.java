package org.smile.dataset;




public class Key extends BaseKey{
	/**用来表达整个集合的一个键*/
	public static final Key STAR = new Key(new Object[0]);

    
    public Key(Object[] keys) {
       super(keys);
    }

    
    public int getLength() {
        if (elements == null) {
            return 0;
        } else {
            return this.elements.length;
        }
    }

    
    public Key getParent() {
        if (elements.length > 0) {
            Object[] newkey = new Object[elements.length - 1];
            System.arraycopy(elements, 0, newkey, 0, newkey.length);
            return new Key(newkey);
        } else {
            return null;
        }
    }
    
    
    public boolean isStar(){
    	return this.elements.length==0;
    }

    
    public Object lastValue() {
        if ((this.elements != null) && (this.elements.length > 0)) {
            return this.elements[elements.length - 1];
        } else {
            return null;
        }
    }

    
    public boolean isChildOf(Key key) {
        return ((key.elements.length + 1) == (this.elements.length)) && this.startWith(key);
    }

    /**
     * 当前键是否是以指定的key开头
     * @param start 起始
     * @return
     */
    private boolean startWith(Key start) {
        if (start.elements.length > this.elements.length) {
            return false;
        }

        for (int i = 0; i < start.elements.length; i++) {
            if (!equals(start.elements[i], this.elements[i])) {
                return false;
            }
        }

        return true;
    }
    

    /**
     * 比较两个内容是否一样
     * @param one
     * @param another
     * @return
     */
    @Override
    protected boolean equals(Object one, Object another) {
        return (one != null) ? one.equals(another) : (another == null);
    }
}
