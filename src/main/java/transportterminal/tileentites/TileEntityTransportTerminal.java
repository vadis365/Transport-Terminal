package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;

public class TileEntityTransportTerminal extends TileEntityInventoryEnergy {

	private String chipName = "Blank";
	private int tempSlot = 0;

	public TileEntityTransportTerminal() {
		super(ConfigHandler.TERMINAL_MAX_ENERGY, 16);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack is) {
		inventory[slot] = is;
		if (is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
		if (is != null && slot == 0 && is.getItem() == TransportTerminal.remote) {
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tempSlot = nbt.getInteger("tempSlot");
		chipName = nbt.getString("chipName");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("tempSlot", tempSlot);
		nbt.setString("chipName", chipName);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == TransportTerminal.remote)
			return true;
		return false;
	}

	@Override
	public String getInventoryName() {
		return "Location X: " + xCoord + " Y: " + yCoord + " Z: " + zCoord;
	}

	public void setName(String text) {
		chipName = text;
		ItemStack is = getStackInSlot(getTempSlot());
		if (is != null && is.getItem() == TransportTerminal.chip)
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

	public boolean canTeleport() {
		return !TransportTerminal.IS_RF_PRESENT || getEnergyStored(ForgeDirection.UNKNOWN) >= ConfigHandler.ENERGY_PER_TELEPORT;
	}

	/**
	 * This is not a battery, energy should not be extractable
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}
}