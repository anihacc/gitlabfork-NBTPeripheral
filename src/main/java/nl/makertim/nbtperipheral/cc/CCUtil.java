package nl.makertim.nbtperipheral.cc;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;
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

	public static Map<Integer, Object> listHandler(List<Object> list) {
		Map<Integer, Object> objectList = new LinkedTreeMap<>(Integer::compareTo);
		for (int i = 0; i < list.size(); i++) {
			objectList.put(i + 1, list.get(i));
		}
		return objectList;
	}
}
