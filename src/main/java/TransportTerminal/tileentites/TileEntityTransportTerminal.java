package TransportTerminal.tileentites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import TransportTerminal.TransportTerminal;

public class TileEntityTransportTerminal extends TileEntity implements IInventory {
	
	private ItemStack[] inventory = new ItemStack[16];
	private String chipName ="Blank";
	private int tempSlot = 0;

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
			ItemStack stack = new ItemStack(TransportTerminal.transportTerminalRemote, 1, 0);
			setInventorySlotContents(1, stack);
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("homeX", xCoord);
				stack.getTagCompound().setInteger("homeY", yCoord);
				stack.getTagCompound().setInteger("homeZ", zCoord);
			}
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

			if (j >= 0 && j < inventory.length) {
				inventory[j] = ItemStack.loadItemStackFromNBT(data);
			}
		}
		tempSlot = nbt.getInteger("tempSlot");
		chipName = nbt.getString("chipName");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList tags = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(data);
				tags.appendTag(data);
			}
		}
		
		nbt.setTag("Items", tags);
		nbt.setInteger("tempSlot", tempSlot);
		nbt.setString("chipName", chipName);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if(slot == 0 && is.getItem() == TransportTerminal.transportTerminalRemote)
			return true;
		return false;
	}

	@Override
	public String getInventoryName() {
		return "Location X: " + xCoord +" Y: " + yCoord + " Z: " + zCoord;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

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
}
