package nl.makertim.nbtperipheral.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class PeripheralProvider implements IPeripheralProvider {

	@Nonnull
	@Override
	public LazyOptional<IPeripheral> getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side) {
		if (world.getBlockState(pos).getBlock() == Blocks.OBSERVER) {
			return LazyOptional.of(() -> new ObserverPeripheral(world, pos));
		}
		if (world.getBlockState(pos).getBlock() instanceof AbstractPressurePlateBlock) {
			return LazyOptional.of(() -> new PlatePeripheral(world, pos));
		}
		if (world.getBlockState(pos).getBlock() instanceof BeaconBlock) {
			return LazyOptional.of(() ->new BeaconPeripheral(world, pos));
		}
		return LazyOptional.empty();
	}
}
