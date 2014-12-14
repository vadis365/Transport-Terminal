package transportterminal.utils;

import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import transportterminal.TransportTerminal;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemTransportTerminalChip;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.tileentites.TileEntitySummoner;
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

	public static void dimensionTransfer(WorldServer worldserver, EntityPlayerMP player, EntityPlayerMP playerOnChip) {
		if (player.dimension != playerOnChip.dimension && playerOnChip.dimension != 1)
			playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
		if (player.dimension != playerOnChip.dimension && playerOnChip.dimension == 1) {
			//this has to be done twice because of stupid vanilla hacks
			playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
			playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
		}
	}

	public static void teleportToConsole(World world, EntityPlayerMP player, BlockPos pos) {
		EnumFacing face = (EnumFacing) world.getBlockState(pos).getValue(BlockDirectional.FACING);
		if (world.isAirBlock(pos.offset(face)) && world.isAirBlock(pos.offset(face).add(0, 1, 0)))
			teleportPlayer(player, pos.getX() + face.getFrontOffsetX() * 0.5, pos.getY() + face.getFrontOffsetY() * 0.5, pos.getZ() + face.getFrontOffsetZ() * 0.5, 0, player.rotationPitch);
	}

	public static void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}

	public static boolean isConsole(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof BlockTransportTerminal;
	}

	public static boolean freeSpace(World world, BlockPos pos) {
		return world.isAirBlock(pos.add(0, 1, 0)) && world.isAirBlock(pos.add(0, 2, 0));
	}

	public static boolean isValidInterfacePlayerChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).hasDisplayName() && tile.getStackInSlot(buttonID).getItem() instanceof ItemTransportTerminalPlayerChip;
	}

	public static boolean isValidInterfaceStandardChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).getTagCompound().hasKey("chipX") && tile.getStackInSlot(buttonID).getItem() instanceof ItemTransportTerminalChip;
	}

	public static void consumeConsoleEnergy(TileEntityTransportTerminal tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	public static void consumeSummonerEnergy(TileEntitySummoner tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	public static EntityPlayerMP getPlayerByUsername(String name) {
		return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name);
	}
}