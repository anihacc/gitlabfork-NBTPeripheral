package nl.makertim.nbtperipheral;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import nl.makertim.nbtperipheral.cc.PeripheralProvider;

@Mod(modid = NBTPeripheralMod.MODID, version = NBTPeripheralMod.VERSION, dependencies = "required:computercraft")
public class NBTPeripheralMod {
	public static final String MODID = "nbtperipheral";
	public static final String VERSION = "1.0";

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
	}
}
