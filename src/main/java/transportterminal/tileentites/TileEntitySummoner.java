package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import transportterminal.ModItems;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemPlayerChip;

public class TileEntitySummoner extends TileEntityInventoryEnergy {

	public TileEntitySummoner() {
		super(ConfigHandler.SUMMONER_MAX_ENERGY, 1);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == ModItems.PLAYER_CHIP)
			return true;
		return false;
	}

	public String getInventoryName() {
		if (!getStackInSlot(0).isEmpty() && getStackInSlot(0).hasDisplayName() && getStackInSlot(0).getItem() instanceof ItemPlayerChip)
			return getStackInSlot(0).getDisplayName();
		return "Empty";
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 0, tag);
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	public boolean canTeleport() {
		return getEnergyStored(null) >= ConfigHandler.ENERGY_PER_TELEPORT;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}
}