package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.utils.TeleportUtils;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

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
		return null;
	}

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;

		int stored = getEnergyStored(null);
		for (ItemStack stack : inventory) {
			if (stored <= 0)
				return;
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.stackSize == 1) {
				IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
				int received = item.receiveEnergy(stack, stored, false);
				extractEnergy(null, received, false);
				TeleportUtils.consumeChargerEnergy(this, received);
				stored = getEnergyStored(null);
			}
		}
		
		if ((stored > 0)) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
				if (tile != null && tile instanceof IEnergyHandler) {
					int received = ((IEnergyReceiver)tile).receiveEnergy(facing.getOpposite(), stored, false);
					extractEnergy(facing, received, false);
					TeleportUtils.consumeChargerEnergy(this, received);
				}
			}
		}
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