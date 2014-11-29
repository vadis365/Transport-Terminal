package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemTransportTerminalPlayerChip;

public class TileEntitySummoner extends TileEntityInventoryEnergy {

	public TileEntitySummoner() {
		super(ConfigHandler.SUMMONER_MAX_ENERGY, 1);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		inventory[slot] = is;
		if (is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == TransportTerminal.playerChip)
			return true;
		return false;
	}

	public String getInventoryName() {
		if (getStackInSlot(0) != null && getStackInSlot(0).hasDisplayName() && getStackInSlot(0).getItem() instanceof ItemTransportTerminalPlayerChip)
			return getStackInSlot(0).getDisplayName();
		return "Empty";
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	public boolean canTeleport() {
		return !TransportTerminal.IS_RF_PRESENT || getEnergyStored(null) >= ConfigHandler.ENERGY_PER_TELEPORT;
	}

	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
		return 0;
	}
}