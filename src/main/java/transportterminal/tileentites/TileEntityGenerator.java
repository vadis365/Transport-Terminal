package transportterminal.tileentites;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

public class TileEntityGenerator extends TileEntityInventoryEnergy implements ITickable {
	public int time = 0;
	public int MAX_TIME = ConfigHandler.GENERATOR_PROCESSING_TIME;
	public int MAX_EXTRACT_PER_TICK = ConfigHandler.GENERATOR_MAX_EXTRACT_PER_TICK;
	public TileEntityGenerator() {
		super(ConfigHandler.GENERATOR_MAX_ENERGY, 1);
	}

	public enum EnumStatus implements IStringSerializable {
		STATUS_NONE("off"),
		STATUS_OUTPUT("on");

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
				worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
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
		time = tagCompound.getInteger("progress");
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
		tagCompound.setInteger("progress", time);
		return tagCompound;
	}

	public EnumStatus getSideStatus(EnumFacing side) {
		return status[side.ordinal()];
	}

	public void toggleMode(EnumFacing side) {
		switch (status[side.ordinal()]) {
		case STATUS_NONE:
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
		if (worldObj.isRemote)
			return;
		int stored = getEnergyStored(null);
		
		if ((stored > 0)) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT) {
					TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
					if (tile != null && tile instanceof IEnergyHandler) {
						int received = ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(), stored >= MAX_EXTRACT_PER_TICK ? MAX_EXTRACT_PER_TICK : 0, false);
						extractEnergy(facing, received, false);
						TeleportUtils.consumeGeneratorEnergy(this, received);
						stored = getEnergyStored(null);
					}
				}
			}
		}

		if (hasFuel() && getEnergyStored(null) <= ConfigHandler.GENERATOR_MAX_ENERGY - getFuelTypeModifier()) {
			time++;
			if (time >= MAX_TIME) {
				time = 0;
				setEnergy(getEnergyStored(null) + getFuelTypeModifier());
				if (inventory[0] != null)
					if (--inventory[0].stackSize <= 0)
						inventory[0] = null;
			}
		}
		if (inventory[0] == null || getEnergyStored(null) == ConfigHandler.GENERATOR_MAX_ENERGY) {
			time = 0;
		}
	}
	
	private int getFuelTypeModifier() {
		return inventory[0] != null && inventory[0].getItem() == Items.REDSTONE ? 2000 : inventory[0] != null && inventory[0].getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK) ? 18000 : 0;
	}

	public boolean hasFuel() {
		return inventory[0] != null && (inventory[0].getItem() == Items.REDSTONE || inventory[0].getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK)) && inventory[0].stackSize >= 1;
	}

	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
		return 0;
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