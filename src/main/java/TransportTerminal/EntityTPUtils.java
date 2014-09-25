package TransportTerminal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class EntityTPUtils {

	public static Entity travelToDimension(Entity e, int dimention, double x, double y, double z) {
		if (!e.worldObj.isRemote && !e.isDead) {
			e.worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = e.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimention);
			e.dimension = dimention;
			if (j == 1 && dimention == 1) {
				worldserver1 = minecraftserver.worldServerForDimension(0);
				e.dimension = 0;
			}
			e.worldObj.removeEntity(e);
			e.isDead = false;
			e.worldObj.theProfiler.startSection("reposition");
			minecraftserver.getConfigurationManager().transferEntityToWorld(e, j, worldserver, worldserver1, new Teleporter2(worldserver1));
			e.worldObj.theProfiler.endStartSection("reloading");
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(e), worldserver1);
			if (entity != null) {
				entity.copyDataFrom(e, true);
				entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
				worldserver1.spawnEntityInWorld(entity);
			}
			e.isDead = true;
			e.worldObj.theProfiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			e.worldObj.theProfiler.endSection();
			return entity;
		}
		return null;
	}

	private static class Teleporter2 extends Teleporter {

		public Teleporter2(WorldServer server) {
			super(server);
		}

		@Override
		public boolean makePortal(Entity par1Entity) {
			return false;
		}

		@Override
		public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
			return false;
		}

		@Override
		public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		}

		@Override
		public void removeStalePortalLocations(long par1) {
		}
	}
}