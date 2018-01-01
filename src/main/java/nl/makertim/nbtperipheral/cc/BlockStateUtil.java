package nl.makertim.nbtperipheral.cc;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.*;

/**
 * @author Tim Biesenbeek
 */
public class BlockStateUtil extends CCUtil {

	public static Map<String, Object> stateToMap(IBlockState state) {
		Map<String, Object> nbtMap = new LinkedTreeMap<>(String::compareTo);
		state.getPropertyKeys().forEach(stateKey -> {
			Comparable<?> c = state.getValue(stateKey);
			String value = c.toString();
			nbtMap.put(stateKey.getName(), value);
		});
		return nbtMap;
	}
}
