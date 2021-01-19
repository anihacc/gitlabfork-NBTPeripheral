package nl.makertim.nbtperipheral.cc;

import com.google.gson.internal.LinkedTreeMap;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tim Biesenbeek
 */
public abstract class EntityPeripheral implements IDynamicPeripheral {
	protected World world;
	protected BlockPos pos;

	public EntityPeripheral(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	protected Entity[] getEntities() {
		return world.getEntities(null, getRange()).toArray(new Entity[0]);
	}

	protected abstract AxisAlignedBB getRange();

	protected abstract Vector3d getCenter();

	@Nonnull
	@Override
	public abstract String getType();

	@Nonnull
	@Override
	public String[] getMethodNames() {
		return Arrays.stream(Methods.values()).map(Methods::getName).toArray(String[]::new);
	}

	@Nonnull
	@Override
	public MethodResult callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull IArguments arguments) throws LuaException {
		try {
			Methods methods = Methods.values()[method];
			Entity[] entities = getEntities();
			switch (methods) {
				default:
				case READ:
					if (entities.length == 0) {
						return MethodResult.of();
					}
					return MethodResult.of(
							Arrays.stream(entities).parallel().map(entity -> {
								CompoundNBT nbt = entity.serializeNBT();
								Vector3d entityPos = new Vector3d(entity.getX(), entity.getY(), entity.getZ());
								CompoundNBT relative = new CompoundNBT();
								Vector3d center = getCenter();
								relative.putDouble("x", (pos.getX() + center.x) - entityPos.x);
								relative.putDouble("y", (pos.getY() + center.y) - entityPos.y);
								relative.putDouble("z", (pos.getZ() + center.z) - entityPos.z);
								nbt.putDouble("_distanceSq", pos.distSqr(entityPos.x, entityPos.y, entityPos.z, false));
								nbt.put("_relative", relative);
								nbt.putString("_name", entity.getName().getString());
								nbt.putString("_displayName", entity.getDisplayName().getString());
								Optional<Team> team = Optional.ofNullable(entity.getTeam());
								team.ifPresent(t -> {
									nbt.putString("_team", t.getName());
									nbt.putString("_teamColor", t.getColor().name());
								});
								nbt.remove("Attributes");
								nbt.remove("recipeBook");
								nbt.remove("toBeDisplayed");
								return NBTUtil.nbtToMap(nbt);
							}).toArray(Object[]::new));
				case COUNT:
					return MethodResult.of(entities.length);
				case IS_EMPTY:
					return MethodResult.of(entities.length == 0);
				// case DEBUG:
				// 	AxisAlignedBB aabb = getRange();
				// 	Map<String, Object> aabbMap = new LinkedTreeMap<>(String::compareTo);
				// 	aabbMap.put("tile", world.getBlockEntity(pos) == null ? null : world.getBlockEntity(pos).getClass().toString());
				// 	aabbMap.put("tilePosX", pos.getX());
				// 	aabbMap.put("tilePosY", pos.getY());
				// 	aabbMap.put("tilePosZ", pos.getZ());
				// 	aabbMap.put("minX", aabb.minX);
				// 	aabbMap.put("minY", aabb.minY);
				// 	aabbMap.put("minZ", aabb.minZ);
				// 	aabbMap.put("maxX", aabb.maxX);
				// 	aabbMap.put("maxY", aabb.maxY);
				// 	aabbMap.put("maxZ", aabb.maxZ);
				// 	return MethodResult.of(aabbMap);
			}
		} catch (Exception ex) {
			throw new LuaException(ex.getMessage());
		}
	}

	@Override
	public boolean equals(@Nullable IPeripheral other) {
		return other instanceof EntityPeripheral && other.getClass().equals(getClass())
				&& ((EntityPeripheral) other).world.equals(world) && ((EntityPeripheral) other).pos.equals(pos);
	}

	public enum Methods {
		IS_EMPTY, COUNT, READ/*, DEBUG*/;

		public String getName() {
			return name().toLowerCase();
		}
	}
}
