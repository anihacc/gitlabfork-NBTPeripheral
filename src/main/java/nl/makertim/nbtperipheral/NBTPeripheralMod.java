package nl.makertim.nbtperipheral;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nl.makertim.nbtperipheral.cc.PeripheralProvider;

@Mod(NBTPeripheralMod.MODID)
public class NBTPeripheralMod {
	public static final String MODID = "nbtperipheral";
	public static final String VERSION = "1.1";

	public NBTPeripheralMod() {
		FMLJavaModLoadingContext.get().getModEventBus()
				.addListener(EventPriority.LOWEST, this::setup);
	}


	private void setup(final FMLCommonSetupEvent event) {
		ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
	}
}
