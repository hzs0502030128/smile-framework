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

    String getName();

    Type getType();

    int getAttributeCount();

    String getAttributeValue(String name);
    
    String getAttributeName(int index);

    boolean hasAttribute(String name);

    Set<String> getAttributeNames();

    String toString();
}

