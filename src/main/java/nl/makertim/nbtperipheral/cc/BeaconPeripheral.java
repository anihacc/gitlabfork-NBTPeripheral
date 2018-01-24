package nl.makertim.nbtperipheral.cc;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Tim Biesenbeek
 */
public class BeaconPeripheral extends EntityPeripheral {

	private static final AxisAlignedBB NON = new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D);

	public BeaconPeripheral(World world, BlockPos pos) {
		super(world, pos);
	}

	protected AxisAlignedBB getRange() {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityBeacon) {
			double levelRange = (double) (((TileEntityBeacon) tile).getLevels() * 10 + 10);
			return new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D).grow(levelRange).expand(0.0D,
				(double) this.world.getHeight(), 0.0D);
		}
		return NON;
	}

	@Override
	protected Vec3d getCenter() {
		return new Vec3d(0.5D, 0.5D, 0.5D);
	}

	@Nonnull
	@Override
	public String getType() {
		return "Beacon_Observer";
	}
}
