package transportterminal.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import transportterminal.TransportTerminal;

public class DimensionUtils {

	public static DimensionManager DIM_MANAGER;
	private static Ticket CHUNK_TICKET;

	public static boolean isDimensionAreaLoaded(int dimensionID, int posX, int posY, int posZ) {
		return getWorldFromDimID(dimensionID).isAreaLoaded(new BlockPos(posX, posY, posZ), 2); //radius of 2 should do
	}

	public static WorldServer getWorldFromDimID(int dimensionID) {
		return DIM_MANAGER.getWorld(dimensionID);
	}

	public static void forceChunkloading(EntityPlayerMP player, final int dimensionID, final int posX, final int posY, final int posZ) {
		player.getServer().addScheduledTask(new Runnable() {
			public void run() {
				if (CHUNK_TICKET == null && !isDimensionAreaLoaded(dimensionID, posX, posY, posZ))
					CHUNK_TICKET = ForgeChunkManager.requestTicket(TransportTerminal.INSTANCE, getWorldFromDimID(dimensionID), ForgeChunkManager.Type.NORMAL);

				if (CHUNK_TICKET != null && !isDimensionAreaLoaded(dimensionID, posX, posY, posZ))
					ForgeChunkManager.forceChunk(CHUNK_TICKET, new ChunkPos(posX >> 4, posZ >> 4));
			}
		});
	}

	public static void releaseChunks() {
		if(CHUNK_TICKET != null) {
			System.out.println("RELEASING CHUNK!: " + CHUNK_TICKET);
			ForgeChunkManager.releaseTicket(CHUNK_TICKET);
			CHUNK_TICKET = null;
		}
	}
}
