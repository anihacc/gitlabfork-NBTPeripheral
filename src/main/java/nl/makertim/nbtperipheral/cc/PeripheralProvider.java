package nl.makertim.nbtperipheral.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Tim Biesenbeek
 */
public class PeripheralProvider implements IPeripheralProvider {
	@Nullable
	@Override
	public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
		if (world.getBlockState(pos).getBlock() == Blocks.OBSERVER) {
			return new ObserverPeripheral(world, pos);
		}
		if (world.getBlockState(pos).getBlock() instanceof BlockPressurePlateWeighted) {
			return new PlatePeripheral(world, pos);
		}
		if (world.getBlockState(pos).getBlock() instanceof BlockBeacon) {
			return new BeaconPeripheral(world, pos);
		}
		return null;
	}
}
