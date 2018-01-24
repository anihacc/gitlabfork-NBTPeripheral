package nl.makertim.nbtperipheral.cc;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
		return PRESSURE_AABB;
	}

	@Override
	protected Vec3d getCenter() {
		return new Vec3d(0.5D, 0D, 0.5D);
	}

	@Nonnull
	@Override
	public String getType() {
		return "Plate_Observer";
	}
}
