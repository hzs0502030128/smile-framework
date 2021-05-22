package org.smile.dataset.index;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.dataset.Key;
import org.smile.dataset.RandomRowSet;
import org.smile.dataset.Row;
import org.smile.dataset.RowSet;
import org.smile.dataset.sort.RowComparator;



public class IndexBuilder {
	/**排序后的行*/
    Row[] rows;
    /**行的索引*/
    int[] rowIndexs;
    /**多级索引时的层级 例：[[0],[0,1]*/
    Object[] groupLevels;
    
    Map<Key,RowSet> index;
    
    RowSet srcRowSet;

    /**
     * @param src 源数据集合
     * @param cols 用于索引的列
     */
    public IndexBuilder(RowSet src, int[] cols) {
        this.rows = sort(src.toArray(), cols);
        this.rowIndexs = initRowIndexs();
        this.groupLevels = getLevels(cols);
        this.srcRowSet = src;
    }

    private Row[] sort(Row[] rows, int[] cols) {
        RowComparator c = new RowComparator(cols);
        Arrays.sort(rows, c);
        return rows;
    }

    private int[] initRowIndexs() {
        int[] result = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].index();
        }
        return result;
    }

    private Object[] getLevels(int[] fields) {
        Object[] result = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            int[] f = new int[i + 1];
            System.arraycopy(fields, 0, f, 0, i + 1);
            result[i] = f;
        }
        return result;
    }

    /**
     * 创建索引数据
     * @return
     */
    public Map<Key,RowSet> build() {
        this.index = new LinkedHashMap<Key,RowSet>();
        build(0, 0, this.rows.length - 1);
        this.index.put(Key.STAR, getTotalIndex());
        return this.index;
    }

    protected RowSet getTotalIndex() {
        return this.srcRowSet;
    }

    
    private void build(int level, int from, int to) {
        int[] fields = (int[]) this.groupLevels[level];

        while (from <= to) {
            Row thisRow = rows[from];

            int row = from;

            while (row <= to) {//直到下一个与当前不一样时,完成当前分级
                Row next = rows[row];
                if (!thisRow.equals(next, fields)) {
                    break;
                }

                row++;
            }

            int _from = from;
            int _to = row - 1;

            
            Object[] vals = thisRow.values(fields);
            Key key = new Key(vals);

            RowSet item = getIndex(key, _from, _to);

            if (level < (this.groupLevels.length - 1)) {
                build(level + 1, _from, _to);
            }
            
            this.index.put(key, item);

            from = row;
        }
    }
    /**
     * 索引键对应的子数据
     * @param key 索引的键
     * @param _from 行起始
     * @param _to 行结束
     * @return
     */
    protected RowSet getIndex(Key key, int _from, int _to) {
        int[] rows = new int[_to - _from + 1];
        System.arraycopy(rowIndexs, _from, rows, 0, rows.length);
        RandomRowSet rowset= new RandomRowSet(srcRowSet.getDataSet(),rows);
        rowset.setKey(key);
        return rowset;
    }
}
