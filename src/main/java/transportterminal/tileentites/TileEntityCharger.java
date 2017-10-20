package transportterminal.tileentites;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.utils.TeleportUtils;

public class TileEntityCharger extends TileEntityInventoryEnergy implements ITickable {

	public TileEntityCharger() {
		super(ConfigHandler.CHARGER_MAX_ENERGY, 6);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void update() {
		if (getWorld().isRemote)
			return;

		int stored = getEnergyStored(null);
		for (ItemStack stack : getItems()) {
			if (stored <= 0)
				return;
			if (!stack.isEmpty() && stack.getItem() instanceof IEnergyContainerItem && stack.getCount() == 1) {
				IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
				int received = item.receiveEnergy(stack, stored, false);
				extractEnergy(null, received, false);
				TeleportUtils.consumeChargerEnergy(this, received);
				stored = getEnergyStored(null);
			}
		}
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}
}