package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import transportterminal.ModItems;
import transportterminal.core.confighandler.ConfigHandler;

public class TileEntityQuantumCrate extends TileEntityInventoryEnergy {

	public TileEntityQuantumCrate() {
		super(ConfigHandler.QUANTUM_CRATE_MAX_ENERGY, 106);
	}

	@Override
	public String getName() {
		return "Quantum Crate " + "Location X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ();
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

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		getItems().set(slot, is);
		if (!is.isEmpty() && is.getCount() > getInventoryStackLimit())
			is.setCount(getInventoryStackLimit());
		if (!is.isEmpty() && slot == 104 && is.getItem() == ModItems.REMOTE_QUANTUM_CRATE) {
			ItemStack stack = is.copy();
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("homeX", pos.getX());
			stack.getTagCompound().setInteger("homeY", pos.getY());
			stack.getTagCompound().setInteger("homeZ", pos.getZ());
			setInventorySlotContents(105, stack);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 104 && is.getItem() == ModItems.REMOTE_QUANTUM_CRATE || slot == 105 && is.getItem() == ModItems.REMOTE_QUANTUM_CRATE)
			return true;
		else
			if (slot < 104)
				return true;
		return false;
	}
}
