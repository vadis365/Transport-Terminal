package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
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
}