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

	public static Map<String, Object> nbtToMap(NBTTagCompound nbt) {
		Map<String, Object> nbtMap = new LinkedTreeMap<>(String::compareTo);
		nbt.getKeySet().forEach(key -> {
			Object value = getValueOf(nbt.getTag(key));
			nbtMap.put(key, value);
		});
		System.out.println(nbtMap);
		return nbtMap;
	}

	public static Object getValueOf(NBTBase base) {
		Object value = null;
		if (base instanceof NBTTagCompound) {
			value = nbtToMap((NBTTagCompound) base);
		} else if (base instanceof NBTPrimitive) {
			if (base instanceof NBTTagDouble) {
				value = ((NBTPrimitive) base).getDouble();
			} else if (base instanceof NBTTagFloat) {
				value = ((NBTPrimitive) base).getFloat();
			} else {
				value = ((NBTPrimitive) base).getInt();
			}
		} else if (base instanceof NBTTagString) {
			value = ((NBTTagString) base).getString();
		} else if (base instanceof NBTTagLongArray) {
			value = base.toString();
		} else if (base instanceof NBTTagByteArray) {
			byte[] bytes = ((NBTTagByteArray) base).getByteArray();
			Object[] objs = new Object[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				objs[i] = (int) bytes[i];
			}
			value = listHandler(objs);
		} else if (base instanceof NBTTagIntArray) {
			int[] ints = ((NBTTagIntArray) base).getIntArray();
			Object[] objs = new Object[ints.length];
			for (int i = 0; i < ints.length; i++) {
				objs[i] = ints[i];
			}
			value = listHandler(objs);
		} else if (base instanceof NBTTagList) {
			List<Object> objects = new LinkedList<>();
			((NBTTagList) base).forEach(newBase -> objects.add(getValueOf(newBase)));
			value = listHandler(objects.toArray());
		}
		return value;
	}
}
