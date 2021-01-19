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
import net.minecraft.world.chunk.Chunk;

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
			TileEntity tile = world.getChunkAt(pos).getBlockEntity(relativePos, Chunk.CreateEntityType.IMMEDIATE);
			switch (methods) {
				default:
				case READ_NBT:
					if (tile == null) {
						throw new LuaException("No TileEntity Found");
					}
					return MethodResult.of(NBTUtil.nbtToMap(tile.serializeNBT()));
				case HAS_NBT:
					return MethodResult.of(tile != null);
				case READ_STATE:
					if (state.getValues().isEmpty()) {
						throw new LuaException("No State Found");
					}
					return MethodResult.of(BlockStateUtil.stateToMap(state));
				case HAS_STATE:
					return MethodResult.of(!state.getValues().isEmpty());
//				case DEBUG:
//					Map<String, Object> debugMap = new LinkedTreeMap<>(String::compareTo);
//					debugMap.put("tileX", relativePos.getX());
//					debugMap.put("tileY", relativePos.getY());
//					debugMap.put("tileZ", relativePos.getZ());
//					debugMap.put("tile", tile == null ? "null" : tile.getClass().getName());
//					debugMap.put("chunkX", world.getChunkAt(pos).getPos().x);
//					debugMap.put("chunkZ", world.getChunkAt(pos).getPos().z);
//					debugMap.put("chunkTilesCount", world.getChunkAt(pos).getBlockEntities().size());
//
//					tile = world.getChunkAt(relativePos).getBlockEntities().get(relativePos);
//					debugMap.put("chunkTile", tile == null ? "null" : tile.getClass().getName());
//
//					Map<BlockPos, TileEntity> list = world.getChunkAt(relativePos).getBlockEntities();
//					int[] i = { 0 };
//					list.forEach((pos, tilePos) -> {
//						if (!(tilePos instanceof ChestTileEntity)) {
//							return;
//						}
//						i[0]++;
//						debugMap.put("chunkTile_" + i[0], tilePos.getClass().getSimpleName() + " " + pos.toString());
//						debugMap.put("chunkTile_" + i[0] + "x", NBTUtil.nbtToMap(tilePos.save(new CompoundNBT())));
//					});
//					debugMap.put("chunkTileTest", pos.toString());
//					debugMap.put("chunkTileTestx", world.getChunkAt(relativePos).getBlockEntities().get(relativePos).toString());
//					debugMap.put("chunkTileTestxz", world.getChunkAt(relativePos)
//							.getBlockEntities()
//							.entrySet()
//							.stream()
//							.filter(entry -> {
//								return entry.getKey().equals(pos);
//							}).findFirst()
//							.orElse(null)
//					);
//
//					return MethodResult.of(debugMap);
			}
		} catch (Exception ex) {
			throw new LuaException(ex.getMessage());
		}
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return other instanceof ObserverPeripheral
				&& ((ObserverPeripheral) other).world.equals(world)
				&& ((ObserverPeripheral) other).pos.equals(pos);
	}

	public enum Methods {
		READ_NBT, HAS_NBT, READ_STATE, HAS_STATE/*, DEBUG*/;

		public String getName() {
			return name().toLowerCase();
		}
	}
}
