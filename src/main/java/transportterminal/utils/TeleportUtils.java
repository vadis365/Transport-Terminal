package transportterminal.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import transportterminal.TransportTerminal;
import transportterminal.blocks.BlockDirectional;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemChip;
import transportterminal.items.ItemPlayerChip;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.tileentites.TileEntityCharger;
import transportterminal.tileentites.TileEntityEnergyCube;
import transportterminal.tileentites.TileEntityGenerator;
import transportterminal.tileentites.TileEntityQuantumCrate;
import transportterminal.tileentites.TileEntitySummoner;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class TeleportUtils {

	public static void dimensionTransfer(WorldServer worldserver, EntityPlayerMP player, int newDim) {
		if (player.dimension != newDim && player.dimension != 1)
			player.mcServer.getPlayerList().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
		if (player.dimension != newDim && player.dimension == 1) {
			//this has to be done twice because of stupid vanilla hacks
			player.mcServer.getPlayerList().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
			player.mcServer.getPlayerList().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
		}
	}

	public static void dimensionTransfer(WorldServer worldserver, EntityPlayerMP player, EntityPlayerMP playerOnChip) {
		if (player.dimension != playerOnChip.dimension && playerOnChip.dimension != 1)
			playerOnChip.mcServer.getPlayerList().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
		if (player.dimension != playerOnChip.dimension && playerOnChip.dimension == 1) {
			//this has to be done twice because of stupid vanilla hacks
			playerOnChip.mcServer.getPlayerList().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
			playerOnChip.mcServer.getPlayerList().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
		}
	}

	public static void teleportToConsole(World world, EntityPlayerMP player, BlockPos pos) {
		EnumFacing face = (EnumFacing) world.getBlockState(pos).getValue(BlockDirectional.FACING);
		if (world.isAirBlock(pos.offset(face)) && world.isAirBlock(pos.offset(face).add(0, 1, 0)))
			teleportPlayer(player, pos.getX() + face.getFrontOffsetX() + 0.5, pos.getY() + face.getFrontOffsetY(), pos.getZ() + face.getFrontOffsetZ() + 0.5, face.getOpposite().getHorizontalAngle(), player.rotationPitch);
	}

	public static void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.connection.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.TELEPORT_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	public static boolean isConsole(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof BlockTransportTerminal;
	}

	public static boolean freeSpace(World world, BlockPos pos) {
		return world.isAirBlock(pos.add(0, 1, 0)) && world.isAirBlock(pos.add(0, 2, 0));
	}

	public static boolean isValidInterfacePlayerChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).hasDisplayName() && tile.getStackInSlot(buttonID).getItem() instanceof ItemPlayerChip;
	}

	public static boolean isValidSummonerPlayerChip(TileEntitySummoner tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).hasDisplayName() && tile.getStackInSlot(buttonID).getItem() instanceof ItemPlayerChip;
	}

	public static boolean isValidInterfaceStandardChip(TileEntityTransportTerminal tile, int buttonID) {
		return tile.getStackInSlot(buttonID) != null && tile.getStackInSlot(buttonID).getTagCompound().hasKey("chipX") && tile.getStackInSlot(buttonID).getItem() instanceof ItemChip;
	}

	public static void consumeConsoleEnergy(TileEntityTransportTerminal tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	public static void consumeSummonerEnergy(TileEntitySummoner tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	public static void consumeChargerEnergy(TileEntityCharger tile, int energyExtracted) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - energyExtracted);
	}

	public static void consumeEnergyCubeEnergy(TileEntityEnergyCube tile, int energyExtracted) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - energyExtracted);
	}

	public static void consumeGeneratorEnergy(TileEntityGenerator tile, int energyExtracted) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - energyExtracted);
	}
	
	public static void consumeQuantumCrateEnergy(TileEntityQuantumCrate tile) {
		if (TransportTerminal.IS_RF_PRESENT)
			tile.setEnergy(tile.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	public static EntityPlayerMP getPlayerByUsername(String name) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(name);
	}
}