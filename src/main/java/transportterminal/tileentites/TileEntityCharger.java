package transportterminal.tileentites;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import transportterminal.core.confighandler.ConfigHandler;
import cofh.api.energy.IEnergyContainerItem;

public class TileEntityCharger extends TileEntityInventoryEnergy {

	public TileEntityCharger() {
		super(ConfigHandler.CHARGER_MAX_ENERGY, 6);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}
/*
	@Override
	public void updateContainingBlockInfo() {
		if (worldObj.isRemote)
			return;

		int stored = getEnergyStored(ForgeDirection.UNKNOWN);
		for (ItemStack stack : inventory) {
			if (stored <= 0)
				return;
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.stackSize == 1) {
				IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
				int received = item.receiveEnergy(stack, stored, false);
				extractEnergy(ForgeDirection.UNKNOWN, received, false);

				stored = getEnergyStored(ForgeDirection.UNKNOWN);
			}
		}
	}
*/
	@Override
	public void openInventory(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clearInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
}