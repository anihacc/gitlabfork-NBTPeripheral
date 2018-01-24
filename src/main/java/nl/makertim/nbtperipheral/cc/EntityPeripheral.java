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
public abstract class EntityPeripheral implements IPeripheral {
	protected World world;
	protected BlockPos pos;

	public EntityPeripheral(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	protected Entity[] getEntities() {
		return world.getEntitiesWithinAABB(Entity.class, getRange().offset(pos)).stream().toArray(Entity[]::new);
	}

	protected abstract AxisAlignedBB getRange();

	protected abstract Vec3d getCenter();

	@Nonnull
	@Override
	public abstract String getType();

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
			Entity[] entities = getEntities();
			switch (methods) {
			case READ:
				if (entities.length == 0) {
					break;
				}
				return Arrays.stream(entities).parallel().map(entity -> {
					NBTTagCompound nbt = new NBTTagCompound();
					entity.writeToNBT(nbt);
					Vec3d entityPos = entity.getPositionVector();
					NBTTagCompound relative = new NBTTagCompound();
					Vec3d center = getCenter();
					relative.setDouble("x", (pos.getX() + center.x) - entityPos.x);
					relative.setDouble("y", (pos.getY() + center.y) - entityPos.y);
					relative.setDouble("z", (pos.getZ() + center.z) - entityPos.z);
					nbt.setDouble("_distanceSq", pos.distanceSqToCenter(entityPos.x, entityPos.y, entityPos.z));
					nbt.setTag("_relative", relative);
					nbt.setString("_name", entity.getName());
					nbt.setString("_displayName", entity.getDisplayName().getUnformattedText());
					Optional<Team> team = Optional.ofNullable(entity.getTeam());
					team.ifPresent(t -> {
						nbt.setString("_team", t.getName());
						nbt.setString("_teamColor", t.getColor().name());
					});
					nbt.removeTag("Attributes");
					nbt.removeTag("recipeBook");
					nbt.removeTag("toBeDisplayed");
					return NBTUtil.nbtToMap(nbt);
				}).toArray(Object[]::new);
			case COUNT:
				return new Object[]{entities.length};
			case IS_EMPTY:
				return new Object[]{entities.length == 0};
			}
			return new Object[0];
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
		IS_EMPTY("is_empty"), COUNT("count"), READ("read");

		Methods(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}
	}
}
