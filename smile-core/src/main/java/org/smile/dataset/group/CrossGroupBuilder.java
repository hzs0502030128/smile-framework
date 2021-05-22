package org.smile.dataset.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.smile.dataset.CrossRowSet;
import org.smile.dataset.Key;
import org.smile.dataset.RowSet;
import org.smile.dataset.index.CrossIndexRowSet;

public class CrossGroupBuilder {

	private static CrossGroupBuilder defaults;

	public static CrossGroupBuilder getDefaults() {
		if (defaults == null) {
			defaults = new CrossGroupBuilder();
		}

		return defaults;
	}

	public RowSet[] build(CrossIndexRowSet view, Key key, Key key2) {
		List<RowSet> result = new ArrayList<RowSet>();

		if ("*".equals(key.lastValue())) {
			key = key.getParent();

			Iterator<Key> it = view.keys();

			while (it.hasNext()) {
				Key k = it.next();

				if (k.isChildOf(key)) {
					CrossRowSet cset = view.locate(k);

					RowSet rset = cset.locate(key2);

					if (rset != null) {
						result.add(rset);
					}
				}
			}
		} else if ("*".equals(key2.lastValue())) {
			key2 = key2.getParent();

			CrossRowSet cset = view.locate(key);
			Iterator<Key> it = cset.keys();

			while (it.hasNext()) {
				Key k = it.next();

				if (k.isChildOf(key2)) {
					RowSet rset = cset.locate(k);

					if (rset != null) {
						result.add(rset);
					}
				}
			}
		} else {
			RowSet rset = view.locate(key, key2);

			if (rset != null) {
				result.add(rset);
			}
		}

		return (RowSet[]) result.toArray(new RowSet[0]);
	}
}
