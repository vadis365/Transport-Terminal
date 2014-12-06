package transportterminal.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemTransportTerminalChip;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class TeleportUtils {
	
	public static void dimensionTransfer(WorldServer worldserver, EntityPlayerMP player, int newDim) {
		if (player.dimension != newDim && player.dimension != 1)
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
		if (player.dimension != newDim && player.dimension == 1) {
			//this has to be done twice because of stupid vanilla hacks
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
		}
	}


	public static void teleportToConsole(World world, EntityPlayerMP player, int x, int y, int z) {
		switch (world.getBlockMetadata(x, y, z)) {
			case 2:
				if (world.isAirBlock(x, y, z - 1) && world.isAirBlock(x, y + 1, z - 1))
					teleportPlayer(player, x + 0.5D, y, z - 0.5D, 0, player.rotationPitch);
				break;
			case 3:
				if (world.isAirBlock(x, y, z + 1) && world.isAirBlock(x, y + 1, z + 1))
					teleportPlayer(player, x + 0.5D, y, z + 1.5D, 180, player.rotationPitch);
				break;
			case 4:
				if (world.isAirBlock(x - 1, y, z) && world.isAirBlock(x - 1, y + 1, z))
					teleportPlayer(player, x - 0.5D, y, z + 0.5D, 270, player.rotationPitch);
				break;
			case 5:
				if (world.isAirBlock(x + 1, y, z) && world.isAirBlock(x + 1, y + 1, z))
					teleportPlayer(player, x + 1.5D, y, z + 0.5D, 90, player.rotationPitch);
				break;
		}
	}


	public static void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}


	public static boolean isConsole(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) instanceof BlockTransportTerminal;
	}

	public static boolean freeSpace(World world, int x, int y, int z) {
		return world.isAirBlock(x, y + 1, z) && world.isAirBlock(x, y + 2, z);
	}

 	public static boolean isValidInterfacePlayerChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).hasDisplayName() && tile.getStackInSlot(buttonID).getItem() instanceof ItemTransportTerminalPlayerChip;
	}


	public static boolean isValidInterfaceStandardChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).stackTagCompound.hasKey("chipX") && tile.getStackInSlot(buttonID).getItem() instanceof ItemTransportTerminalChip;
	}

	public static void consumeEnergy(TileEntityTransportTerminal tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

}
