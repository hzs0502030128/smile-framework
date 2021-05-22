package org.smile.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 使用byte带实现的 类似于java.io.ByteArrayInputStream 
 * 但此类不是线程安全的 不适用于并发场景
 * @author 胡真山
 *
 */
public class ByteArrayInputStream extends InputStream implements Input{
	/**缓存的数组*/
    byte[] buffer;
    int limit;
    int pos;
    int mark;

    public ByteArrayInputStream(byte data[]) {
        this(data, 0, data.length);
    }

    public ByteArrayInputStream(byte data[], int offset, int size) {
        this.buffer = data;
        this.mark = offset;
        this.pos = offset;
        this.limit = offset + size;
    }
    
    @Override
    public int read() throws IOException {
        if (pos < limit) {
            return buffer[pos++] & 0xff;
        } else {
            return -1;
        }
    }
    
    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
    
    @Override
    public int read(byte b[], int off, int len) {
        if (pos < limit) {
            len = Math.min(len, limit - pos);
            if (len > 0) {
                System.arraycopy(buffer, pos, b, off, len);
                pos += len;
            }
            return len;
        } else {
            return -1;
        }
    }
    @Override
    public long skip(long len) throws IOException {
        if (pos < limit) {
            len = Math.min(len, limit - pos);
            if (len > 0) {
                pos += len;
            }
            return len;
        } else {
            return -1;
        }
    }
    
    @Override
    public int available() {
        return limit - pos;
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public void mark(int markpos) {
        mark = pos;
    }
    
    @Override
    public void reset() {
        pos = mark;
    }
}

