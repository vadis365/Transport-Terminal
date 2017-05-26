package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.utils.TeleportUtils;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

public class TileEntityEnergyCube extends TileEntityInventoryEnergy implements ITickable {

	public TileEntityEnergyCube() {
		super(ConfigHandler.ENERGY_CUBE_MAX_ENERGY, 1);
	}

	public enum EnumStatus implements IStringSerializable {
		STATUS_NONE("none"),
		STATUS_INPUT("input"),
		STATUS_OUTPUT("output");

		private final String name;

		EnumStatus(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	public EnumStatus status[] = new EnumStatus[] { EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE };

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		EnumStatus[] old = new EnumStatus[] { status[0], status[1], status[2], status[3], status[4], status[5] };
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (old[facing.ordinal()] != status[facing.ordinal()]) {
				getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
				return;
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
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		status[0] = EnumStatus.values()[tagCompound.getByte("down")];
		status[1] = EnumStatus.values()[tagCompound.getByte("up")];
		status[2] = EnumStatus.values()[tagCompound.getByte("north")];
		status[3] = EnumStatus.values()[tagCompound.getByte("south")];
		status[4] = EnumStatus.values()[tagCompound.getByte("west")];
		status[5] = EnumStatus.values()[tagCompound.getByte("east")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setByte("down", (byte) status[0].ordinal());
		tagCompound.setByte("up", (byte) status[1].ordinal());
		tagCompound.setByte("north", (byte) status[2].ordinal());
		tagCompound.setByte("south", (byte) status[3].ordinal());
		tagCompound.setByte("west", (byte) status[4].ordinal());
		tagCompound.setByte("east", (byte) status[5].ordinal());
		return tagCompound;
	}

	public EnumStatus getSideStatus(EnumFacing side) {
		return status[side.ordinal()];
	}

	public void toggleMode(EnumFacing side) {
		switch (status[side.ordinal()]) {
		case STATUS_NONE:
			status[side.ordinal()] = EnumStatus.STATUS_INPUT;
			break;
		case STATUS_INPUT:
			status[side.ordinal()] = EnumStatus.STATUS_OUTPUT;
			break;
		case STATUS_OUTPUT:
			status[side.ordinal()] = EnumStatus.STATUS_NONE;
			break;
		}
		markDirty();
	}

	@Override
	public void update() {
		if (getWorld().isRemote)
			return;

		int stored = getEnergyStored(null);
		if ((stored > 0)) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT) {
					TileEntity tile = getWorld().getTileEntity(pos.offset(facing));
					if (tile != null && tile instanceof IEnergyHandler) {
						int received = ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(), stored, false);
						extractEnergy(facing, received, false);
						TeleportUtils.consumeEnergyCubeEnergy(this, received);
						stored = getEnergyStored(null);
					}
				}
			}
		}
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (status[from.ordinal()] != EnumStatus.STATUS_INPUT)
			return 0;
		return super.receiveEnergy(from, maxReceive, simulate);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

}