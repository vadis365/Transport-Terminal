package TransportTerminal.tileentites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import TransportTerminal.TransportTerminal;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.TileEnergyHandler;

public class TileEntityTransportTerminal extends TileEnergyHandler implements IInventory {

	private ItemStack[] inventory = new ItemStack[16];
	private String chipName = "Blank";
	private int tempSlot = 0;

	public TileEntityTransportTerminal() {
		storage = new EnergyStorage(TransportTerminal.TERMINAL_MAX_ENERGY);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if (inventory[slot] != null) {
			ItemStack is;
			if (inventory[slot].stackSize <= size) {
				is = inventory[slot];
				inventory[slot] = null;
				return is;
			} else {
				is = inventory[slot].splitStack(size);
				if (inventory[slot].stackSize == 0)
					inventory[slot] = null;
				return is;
			}
		} else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (inventory[slot] != null) {
			ItemStack is = inventory[slot];
			inventory[slot] = null;
			return is;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		inventory[slot] = is;
		if (is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
		if (is != null && slot == 0 && is.getItem() == TransportTerminal.transportTerminalRemote) {
			ItemStack stack = is.copy();
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("homeX", xCoord);
			stack.getTagCompound().setInteger("homeY", yCoord);
			stack.getTagCompound().setInteger("homeZ", zCoord);
			setInventorySlotContents(1, stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList tags = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound data = tags.getCompoundTagAt(i);
			int j = data.getByte("Slot") & 255;

			if (j >= 0 && j < inventory.length)
				inventory[j] = ItemStack.loadItemStackFromNBT(data);
		}
		tempSlot = nbt.getInteger("tempSlot");
		chipName = nbt.getString("chipName");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList tags = new NBTTagList();

		for (int i = 0; i < inventory.length; i++)
			if (inventory[i] != null) {
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(data);
				tags.appendTag(data);
			}

		nbt.setTag("Items", tags);
		nbt.setInteger("tempSlot", tempSlot);
		nbt.setString("chipName", chipName);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == TransportTerminal.transportTerminalRemote)
			return true;
		return false;
	}

	@Override
	public String getInventoryName() {
		return "Location X: " + xCoord + " Y: " + yCoord + " Z: " + zCoord;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void setName(String text) {
		chipName = text;
		ItemStack is = getStackInSlot(getTempSlot());
		if (is != null && is.getItem() == TransportTerminal.transportTerminalChip)
			is.getTagCompound().setString("description", chipName);
	}

	public int getTempSlot() {
		return tempSlot;
	}

	public void setTempSlot(int slot) {
		tempSlot = slot;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	/**
	 * Returns whether or not this terminal has enough energy to teleport a
	 * player. Should return true always if RF mode is not enabled.
	 *
	 */
	public boolean canTeleport() {
		return getEnergyStored(ForgeDirection.UNKNOWN) >= TransportTerminal.ENERGY_PER_TELEPORT;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}
}