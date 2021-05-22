package org.smile.tag.parser;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.Set;

import org.smile.collection.LinkedHashMap;
import org.smile.commons.SmileRunException;
import org.smile.util.HashCode;
import org.smile.util.StringUtils;

public class CustomTagInfo implements TagInfo {
	/**标签解析出的属性*/
	protected Map<String,String> attributes=new LinkedHashMap<String,String>();
    /***标签类型*/
	private TagInfo.Type type;
    /**标签名称*/
    private String name;
    //保存当前对象hash值
    private int hashCode=-1;
    
    protected CustomTagInfo(){}

    /**
     * Create new tag.
     */
    public CustomTagInfo(String name, TagInfo.Type type) {
       this.name=name;
       this.type=type;
    }


	/**
     * Create a CustomTag based on an existing Tag - this takes a copy of the Tag.
     */
    public CustomTagInfo(TagInfo tag) {
    	this.name=tag.getName();
        this.type=tag.getType();
        if (tag instanceof CustomTagInfo) {
        	CustomTagInfo orig = (CustomTagInfo) tag;
            this.attributes.putAll(orig.attributes);
        } else {
        	Set<String> attributeNames=tag.getAttributeNames();
        	for(String attr:attributeNames){
        		attributes.put(attr, tag.getAttributeValue(attr));
        	}
        }
    }

    public void writeTo(Appendable out){
        int length = 0;
        length += type == TagInfo.Type.CLOSE ? 2 : 1;
        length += getName().length();
        for (Map.Entry<String, String> entry:this.attributes.entrySet()) {
            length += 1 + entry.getKey().length();
            if (entry.getKey() != null) {
                length += 3 + entry.getValue().length();
            }
        }
        length += type == TagInfo.Type.EMPTY ? 2 : 1;

        // Allocate buffer for tag.
        CharBuffer tag = CharBuffer.allocate(length);

        tag.put('<');
        if (type == TagInfo.Type.CLOSE) {
            tag.put('/');
        }
        tag.put(getName());

        for (Map.Entry<String, String> entry:this.attributes.entrySet()) {
        	tag.put(' ').put(entry.getKey());
            if (entry.getValue()!= null) {
                tag.put('=').put('"').put(entry.getValue()).put('"');
            }
        }

        if (type == TagInfo.Type.EMPTY) {
            tag.put('/');
        }
        tag.put('>');

        assert tag.remaining() == 0 : "Incorrect buffer length for tag. Please report this to the SiteMesh team.";

        // Output the buffer.
        tag.flip();
        try {
			out.append(tag);
		} catch (IOException e) {
			throw new SmileRunException(e);
		}
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomTagInfo)) return false;
       
        CustomTagInfo customTag = (CustomTagInfo) o;
        return type == customTag.type
        		&& StringUtils.equals(name, customTag.getName(), true)
                && attributes.equals(customTag.attributes);
    }
    @Override
    public int hashCode() {
    	if(this.hashCode==-1){
    		generateHashCode();
    	}
    	return hashCode;
    }
    /**
     * 创建hashcode
     */
    protected void generateHashCode(){
    	int result = (attributes != null ? attributes.hashCode() : 0);
        result = HashCode.hash(result, (getName() != null ? getName().hashCode() : 0));
        result = HashCode.hash(result ,type.hashCode());
        this.hashCode=result;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(64);
        writeTo(out);
        return out.toString();
    }

    

    @Override
    public TagInfo.Type getType() {
        return type;
    }

    @Override
    public int getAttributeCount() {
        return attributes.size();
    }

    @Override
    public String getAttributeName(int index) {
    	 if(this.attributes.size()>0){
         	int i=0;
             for(Map.Entry<String, String> entry:this.attributes.entrySet()){
             	if(i==index){
             		return entry.getKey();
             	}
             	i++;
             }
         }
         return null;
    }

    @Override
    public String getAttributeValue(String name) {
        return this.attributes.get(name);
    }
    @Override
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }


	@Override
	public Set<String> getAttributeNames() {
		return this.attributes.keySet();
	}
	
    public void removeAttribute(String name) {
       this.attributes.remove(name);
    }

	@Override
	public String getName() {
		return name;
	}

}

