package nl.makertim.nbtperipheral.cc;

import net.minecraft.tags.BlockTags;
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
		double levels = 0;

		for (int i = 1; i <= 4; levels = i++) {
			int j = pos.getY() - i;
			if (j < 0) {
				break;
			}

			boolean flag = true;

			for (int k = pos.getX() - i; k <= pos.getX() + i && flag; ++k) {
				for (int l = pos.getZ() - i; l <= pos.getZ() + i; ++l) {
					if (!world.getBlockState(new BlockPos(k, j, l)).is(BlockTags.BEACON_BASE_BLOCKS)) {
						flag = false;
						break;
					}
				}
			}

			if (!flag) {
				break;
			}
		}

		return new AxisAlignedBB(pos)
				.inflate(levels * 10 + 10)
				.expandTowards(0, world.getMaxBuildHeight(), 0);
	}

	@Override
	protected Vector3d getCenter() {
		return new Vector3d(0.5, 0.5, 0.5);
	}

	@Nonnull
	@Override
	public String getType() {
		return "Beacon_Observer";
	}
}
