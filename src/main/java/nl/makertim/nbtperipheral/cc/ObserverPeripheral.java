package nl.makertim.nbtperipheral.cc;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tim Biesenbeek
 */
public class ObserverPeripheral implements IDynamicPeripheral {

	private final World world;
	private final BlockPos pos;

	public ObserverPeripheral(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	@Nonnull
	protected Optional<Direction> getFacing() {
		BlockState state = world.getBlockState(pos);

		if (state.getValues().containsKey(DirectionalBlock.FACING)) {
			return Optional.of(state.getValue(DirectionalBlock.FACING));
		}
		return Optional.empty();
	}

	protected BlockPos relative(Direction facing) {
		return pos.relative(facing);
	}

	@Nonnull
	@Override
	public String getType() {
		return "NBT_Observer";
	}

	@Nullable
	@Override
	public Object getTarget() {
		Optional<Direction> facingOptional = getFacing();
		return facingOptional
				.map(direction -> world.getBlockState(this.pos.relative(direction)))
				.orElse(null);
	}

	@Nonnull
	@Override
	public String[] getMethodNames() {
		return Arrays
				.stream(Methods.values())
				.map(Methods::getName)
				.toArray(String[]::new);
	}

	@Nonnull
	@Override
	public MethodResult callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull IArguments arguments) throws LuaException {
		try {
			Methods methods = Methods.values()[method];
			Optional<Direction> facingOptional = getFacing();
			if (!facingOptional.isPresent()) {
				throw new LuaException("Cant read direction from the observer");
			}
			BlockPos relativePos = relative(facingOptional.get());
			BlockState state = world.getBlockState(relativePos);
			TileEntity tile = world.getBlockEntity(relativePos);
			switch (methods) {
				case READ_NBT:
					if (tile == null) {
						throw new LuaException("No TileEntity Found");
					}
					CompoundNBT compound = new CompoundNBT();
					tile.deserializeNBT(compound);
					return MethodResult.of(NBTUtil.nbtToMap(compound));
				case HAS_NBT:
					return MethodResult.of(tile != null);
				case READ_STATE:
					if (state.getValues().isEmpty()) {
						throw new LuaException("No State Found");
					}
					return MethodResult.of(BlockStateUtil.stateToMap(state));
				case HAS_STATE:
					return MethodResult.of(state.getValues().isEmpty());
			}
		} catch (Exception ex) {
			throw new LuaException(ex.getMessage());
		}
		return MethodResult.of();
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return other instanceof ObserverPeripheral
				&& ((ObserverPeripheral) other).world.equals(world)
				&& ((ObserverPeripheral) other).pos.equals(pos);
	}

	public enum Methods {
		READ_NBT("read_nbt"), HAS_NBT("has_nbt"), READ_STATE("read_state"), HAS_STATE("has_state");

		Methods(String name) {
			this.name = name;
		}

		private final String name;

		public String getName() {
			return name;
		}
	}
}
