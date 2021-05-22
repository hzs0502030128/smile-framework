package org.smile.dataset.index;

import org.smile.dataset.CrossRowSet;
import org.smile.dataset.Key;
import org.smile.dataset.RowSet;



public class CrossIndexBuilder extends IndexBuilder {
	/**列索引的列*/
    int[] cols;

    public CrossIndexBuilder(RowSet src, int[] fields, int[] cols) {
        super(src, fields);
        this.cols = cols;
    }

    @Override
    protected RowSet getTotalIndex() {
        return new CrossRowSet(srcRowSet.getDataSet(), Key.STAR, rowIndexs, cols);
    }
    @Override
    protected RowSet getIndex(Key key, int _from, int _to) {
        int[] rows = new int[_to - _from + 1];
        System.arraycopy(rowIndexs, _from, rows, 0, rows.length);
        return new CrossRowSet(srcRowSet.getDataSet(), key, rows, cols);
    }
}
