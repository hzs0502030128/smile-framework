package org.smile.dataset.sort;

import java.util.Comparator;

import org.smile.dataset.FormulaUtils;
import org.smile.dataset.Row;

/**
 * 行排序比较器
 * @author 胡真山
 *
 */
public class RowComparator implements Comparator<Row> {
	/**
	 * 用于排序的列
	 */
    private int[] sortOrder;
    /**
     * 排序方向
     */
    private boolean[] directions;

    
    public RowComparator(int[] sortOrder) {
        this.sortOrder = sortOrder;
    }

    
    public RowComparator(int[] sortOrder, boolean[] directions) {
        this.sortOrder = sortOrder;
        this.directions = directions;
    }

    
    public RowComparator() {
        sortOrder = null;
    }

    @Override
    public int compare(Row row1, Row row2) {
        if (sortOrder == null) {
            int columnCount = row1.getColumnCount();
            for (int i = 1; i <= columnCount; i++){
                int value = FormulaUtils.compare((Comparable) row1.value(i),(Comparable) row2.value(i));
                if (value > 0) {
                    return value;
                } else if (value < 0) {
                    return value;
                }
            }
        } else {
            int columnCount = row1.getColumnCount();
            for (int i = 0; i < sortOrder.length; i++) {
                if ((sortOrder[i] >= 0) && (sortOrder[i] <= (columnCount - 1))) {
                    int value =  FormulaUtils.compare((Comparable) row1.value(sortOrder[i]),(Comparable) row2.value(sortOrder[i]));
                    if (value != 0) {
                        return ((directions == null) || directions[i]) ? value : (-value);
                    }
                }
            }
        }
        return 0;
    }
}
