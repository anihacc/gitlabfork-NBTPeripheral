package nl.makertim.nbtperipheral.cc;

import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.block.BlockState;

import java.util.Map;

/**
 * @author Tim Biesenbeek
 */
public class BlockStateUtil extends CCUtil {

	public static Map<String, Object> stateToMap(BlockState state) {
		Map<String, Object> nbtMap = new LinkedTreeMap<>(String::compareTo);
		state.getValues().forEach((stateName, comparable) -> {
			Comparable<?> c = state.getValue(stateName);
			String value = c.toString();
			nbtMap.put(stateName.getName(), value);
		});
		return nbtMap;
	}
}
