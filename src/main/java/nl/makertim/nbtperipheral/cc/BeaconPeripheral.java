package nl.makertim.nbtperipheral.cc;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author Tim Biesenbeek
 */
public class BeaconPeripheral extends EntityPeripheral {

	public BeaconPeripheral(World world, BlockPos pos) {
		super(world, pos);
	}

	protected AxisAlignedBB getRange() {
		return new AxisAlignedBB(pos).inflate(64, world.getMaxBuildHeight(), 64);
	}

	@Override
	protected Vector3d getCenter() {
		return new Vector3d(0.5D, 0.5D, 0.5D);
	}

	@Nonnull
	@Override
	public String getType() {
		return "Beacon_Observer";
	}
}
