package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;

public class TileEntityItemTransporter extends TileEntityInventoryEnergy implements ITickable {
    private static final float ROTATION_SPEED = 2.0F;
    public float rotation;
    public float prevRotation;

	public TileEntityItemTransporter() {
		super(ConfigHandler.ITEM_TRANSPORTER_MAX_ENERGY, 2);
	}

	@Override
	public void update() {
		if (this.worldObj.isRemote) {
			this.prevRotation = this.rotation;
			this.rotation += ROTATION_SPEED;
			if (this.rotation >= 360.0F) {
				this.rotation -= 360.0F;
				this.prevRotation -= 360.0F;
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
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if (slot == 0 && is.getItem() == TransportTerminal.CHIP)
			return true;
		return false;
	}

	public boolean canTeleport() {
		return !TransportTerminal.IS_RF_PRESENT || getEnergyStored(null) >= ConfigHandler.ENERGY_PER_ITEM_TRANSFER;
	}
}


