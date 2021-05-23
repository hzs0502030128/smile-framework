package org.smile.tag.parser;

import java.io.IOException;
import java.util.Set;

public interface TagInfo {

    public static enum Type {

        /**
         * Opening tag: <code>&lt;blah&gt;</code>
         */
        OPEN,

        /**
         * Closing tag: <code>&lt;/blah&gt;</code>
         */
        CLOSE,

        /**
         * Empty tag: <code>&lt;blah/&gt;</code>
         */
        EMPTY,

        /**
         * Opening conditional comment: <code>&lt;!--[</code>
         */
        OPEN_CONDITIONAL_COMMENT,

        /**
         * Closing conditional comment: <code>&lt;![</code>
         */
        CLOSE_CONDITIONAL_COMMENT
    }

    void writeTo(Appendable out) throws IOException;

    /**
     * 标签名称
     * @return
     */
    String getName();

    /**
     * 标称类型
     * @return
     */
    Type getType();

    /**
     * 属性个数
     * @return
     */
    int getAttributeCount();

    /**
     * 获取属性的值
     * @param name
     * @return
     */
    String getAttributeValue(String name);

    /**
     * 以索引获取属性名称
     * @param index
     * @return
     */
    String getAttributeName(int index);

    boolean hasAttribute(String name);

    Set<String> getAttributeNames();

    public void removeAttribute(String name);

    String toString();
}

