package nl.makertim.nbtperipheral.cc;

import static nl.makertim.nbtperipheral.cc.BlockStateUtil.stateToMap;
import static nl.makertim.nbtperipheral.cc.NBTUtil.nbtToMap;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Tim Biesenbeek
 */
public class ObserverPeripheral implements IPeripheral {

	private World world;
	private BlockPos pos;

	public ObserverPeripheral(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	@Nonnull
	protected Optional<EnumFacing> getFacing() {
		IBlockState state = world.getBlockState(pos);
		if (state.getPropertyKeys().contains(BlockDirectional.FACING)) {
			return Optional.of(state.getValue(BlockDirectional.FACING));
		}
		return Optional.empty();
	}

	protected BlockPos relative(EnumFacing facing) {
		return pos.add(facing.getDirectionVec());
	}

	@Nonnull
	@Override
	public String getType() {
		return "NBT_Observer";
	}

	@Nonnull
	@Override
	public String[] getMethodNames() {
		return Arrays.stream(Methods.values()).map(Methods::getName).toArray(String[]::new);
	}

	@Nullable
	@Override
	public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException {
		try {
			Methods methods = Methods.values()[method];
			Optional<EnumFacing> facingOptional = getFacing();
			if (!facingOptional.isPresent()) {
				throw new LuaException("Cant read direction from the observer");
			}
			BlockPos relativePos = relative(facingOptional.get());
			IBlockState state = world.getBlockState(relativePos);
			TileEntity tile = world.getTileEntity(relativePos);
			switch (methods) {
			case READ_NBT:
				if (tile == null) {
					throw new LuaException("No TileEntity Found");
				}
				NBTTagCompound compound = new NBTTagCompound();
				tile.writeToNBT(compound);
				return new Object[]{nbtToMap(compound)};
			case HAS_NBT:
				return new Object[]{tile != null};
			case READ_STATE:
				if (state.getPropertyKeys().isEmpty()) {
					throw new LuaException("No State Found");
				}
				return new Object[]{stateToMap(state)};
			case HAS_STATE:
				return new Object[]{state.getPropertyKeys().isEmpty()};
			}
			return new Object[0];
		} catch (Exception ex) {
			throw new LuaException(ex.getMessage());
		}
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return other instanceof ObserverPeripheral && ((ObserverPeripheral) other).world.equals(world)
				&& ((ObserverPeripheral) other).pos.equals(pos);
	}

	public enum Methods {
		READ_NBT("read_nbt"), HAS_NBT("has_nbt"), READ_STATE("read_state"), HAS_STATE("has_state");

		Methods(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}
	}
}
