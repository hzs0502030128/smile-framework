package org.smile.dataset.group;

import java.util.LinkedHashMap;
import java.util.Map;

import org.smile.dataset.DataSet;
import org.smile.dataset.Key;
import org.smile.dataset.RandomRowSet;
import org.smile.dataset.Row;
import org.smile.dataset.RowSet;

public class GroupBuilder {
	DataSet data;
	Row[] rows;
	int[] ids;
	int col;

	public GroupBuilder(DataSet data, Row[] rows, int col) {
		this.data = data;
		this.rows = rows;
		this.ids = getIds(rows);
		this.col = col;
	}

	private int[] getIds(Row[] rows2) {
		int[] result = new int[rows.length];

		for (int i = 0; i < rows2.length; i++) {
			result[i] = rows2[i].index();
		}

		return result;
	}

	public Map<Key,RowSet> build() {
		
		Map<Key,RowSet> keyCache=new LinkedHashMap<Key,RowSet>();

		int from = 0;
		int to = this.rows.length - 1;

		int[] fields = new int[] { col };

		while (from <= to) {
			Row thisRow = rows[from];

			int row = from;

			while (row <= to) {
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

			keyCache.put(key,item);

			from = row;
		}

		return keyCache;
	}

	private RowSet getIndex(Key key, int _from, int _to) {
		int[] rows = new int[_to - _from + 1];
		System.arraycopy(ids, _from, rows, 0, rows.length);

		RandomRowSet rowset = new RandomRowSet(data, rows);
		rowset.setKey(key);
		return rowset;
	}
}
