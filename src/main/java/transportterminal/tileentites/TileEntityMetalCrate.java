package transportterminal.tileentites;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMetalCrate extends TileEntity implements IInventory {

	public ItemStack[] inventory = new ItemStack[104];

	public TileEntityMetalCrate() {
		super();
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

	//@Override
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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
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
		return nbt;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) != this ? false : true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		return true;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public String getName() {
		return "Metal Crate";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {
	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; i++)
			inventory[i] = null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}
}
