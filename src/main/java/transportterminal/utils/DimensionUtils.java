package transportterminal.utils;

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

	public static void loadDimension(int dimensionID) {
		DIM_MANAGER.initDimension(dimensionID);
	}

	public static boolean isPlayerInDimension(int dimensionID) {
		return getWorldFromDimID(dimensionID) != null && !getWorldFromDimID(dimensionID).playerEntities.isEmpty();
	}

	public static boolean isDimensionAreaLoaded(int dimensionID, int posX, int posY, int posZ) {
		return getWorldFromDimID(dimensionID).isAreaLoaded(new BlockPos(posX, posY, posZ), 2); //radius of 2 should do
	}

	public static WorldServer getWorldFromDimID(int dimensionID) {
		return DIM_MANAGER.getWorld(dimensionID);
	}
	
	public static void forceChunkloading(int dimensionID, int posX, int posY, int posZ) {
		if (CHUNK_TICKET == null && !isDimensionAreaLoaded(dimensionID, posX, posY, posZ))
			CHUNK_TICKET = ForgeChunkManager.requestTicket(TransportTerminal.INSTANCE, getWorldFromDimID(dimensionID), ForgeChunkManager.Type.NORMAL);

		if (CHUNK_TICKET != null && !isDimensionAreaLoaded(dimensionID, posX, posY, posZ))
			ForgeChunkManager.forceChunk(CHUNK_TICKET, new ChunkPos(posX, posY));
	}
}
