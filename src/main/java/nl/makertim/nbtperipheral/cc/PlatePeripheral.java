package nl.makertim.nbtperipheral.cc;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author Tim Biesenbeek
 */
public class PlatePeripheral extends EntityPeripheral {

	// BlockBasePressurePlate.PRESSURE_AABB - is protected
	private static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);

	public PlatePeripheral(World world, BlockPos pos) {
		super(world, pos);
	}

	protected AxisAlignedBB getRange() {
		return PRESSURE_AABB.move(pos);
	}

	@Override
	protected Vector3d getCenter() {
		return new Vector3d(0.5D, 0D, 0.5D);
	}

	@Nonnull
	@Override
	public String getType() {
		return "Plate_Observer";
	}
}
