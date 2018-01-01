package nl.makertim.nbtperipheral.cc;

import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

/**
 * @author Tim Biesenbeek
 */
public class CCUtil {

	public static Map<Integer, Object> listHandler(Object[] array) {
		Map<Integer, Object> objectList = new LinkedTreeMap<>(Integer::compareTo);
		for (int i = 0; i < array.length; i++) {
			objectList.put(i + 1, array[i]);
		}
		return objectList;
	}
}
