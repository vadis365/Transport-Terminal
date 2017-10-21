package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import transportterminal.ModItems;
import transportterminal.core.confighandler.ConfigHandler;

public class TileEntityTransportTerminal extends TileEntityInventoryEnergy {

	private String chipName = "Blank";
	private int tempSlot = 0;

	public TileEntityTransportTerminal() {
		super(ConfigHandler.TERMINAL_MAX_ENERGY, 16);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		getItems().set(slot, is);
		 if (is.getCount() > getInventoryStackLimit())
			 is.setCount(this.getInventoryStackLimit());
		if (!is.isEmpty() && slot == 0 && is.getItem() == ModItems.REMOTE || !is.isEmpty() && slot == 0 && is.getItem() == ModItems.REMOTE_TERMINAL) {
			ItemStack stack = is.copy();
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("homeX", pos.getX());
			stack.getTagCompound().setInteger("homeY", pos.getY());
			stack.getTagCompound().setInteger("homeZ", pos.getZ());
			setInventorySlotContents(1, stack);
		}
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tempSlot = nbt.getInteger("tempSlot");
		chipName = nbt.getString("chipName");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("tempSlot", tempSlot);
		nbt.setString("chipName", chipName);
		return nbt;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == ModItems.REMOTE || slot == 0 && is.getItem() == ModItems.REMOTE_TERMINAL)
			return true;
		return false;
	}

	@Override
	public String getName() {
		return "Location X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ();
	}

	public void setName(String text) {
		chipName = text;
		ItemStack is = getStackInSlot(getTempSlot());
		if (!is.isEmpty() && is.getItem() == ModItems.CHIP)
			is.getTagCompound().setString("description", chipName);
	}

	public int getTempSlot() {
		return tempSlot;
	}

	public void setTempSlot(int slot) {
		tempSlot = slot;
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

	public boolean canTeleport() {
		return getEnergyStored(null) >= ConfigHandler.ENERGY_PER_TELEPORT;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}
}