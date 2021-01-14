package nl.makertim.nbtperipheral.cc;

import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.nbt.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Biesenbeek
 */
public class NBTUtil extends CCUtil {

	public static Map<String, Object> nbtToMap(CompoundNBT nbt) {
		Map<String, Object> nbtMap = new LinkedTreeMap<>(String::compareTo);
		nbt.getAllKeys().forEach(key -> {
			Object value = getValueOf(nbt.get(key));
			nbtMap.put(key, value);
		});
		return nbtMap;
	}

	public static Object getValueOf(INBT base) {
		Object value = null;
		if (base instanceof CompoundNBT) {
			value = nbtToMap((CompoundNBT) base);
		} else if (base instanceof NumberNBT) {
			if (base instanceof DoubleNBT) {
				value = ((DoubleNBT) base).getAsDouble();
			} else if (base instanceof FloatNBT) {
				value = ((NumberNBT) base).getAsFloat();
			} else {
				value = ((NumberNBT) base).getAsInt();
			}
		} else if (base instanceof StringNBT) {
			value = base.getAsString();
		} else if (base instanceof LongArrayNBT) {
			value = base.getAsString();
		} else if (base instanceof ByteArrayNBT) {
			byte[] bytes = ((ByteArrayNBT) base).getAsByteArray();
			Object[] objs = new Object[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				objs[i] = (int) bytes[i];
			}
			value = listHandler(objs);
		} else if (base instanceof IntArrayNBT) {
			int[] ints = ((IntArrayNBT) base).getAsIntArray();
			Object[] objs = new Object[ints.length];
			for (int i = 0; i < ints.length; i++) {
				objs[i] = ints[i];
			}
			value = listHandler(objs);
		} else if (base instanceof ListNBT) {
			List<Object> objects = new LinkedList<>();
			((ListNBT) base).forEach(newBase -> objects.add(getValueOf(newBase)));
			value = listHandler(objects.toArray());
		}
		return value;
	}
}
