package transportterminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import transportterminal.core.confighandler.ConfigHandler;
import cofh.api.energy.IEnergyContainerItem;

public class TileEntityCharger extends TileEntityInventoryEnergy implements ITickable {

	public TileEntityCharger() {
		super(ConfigHandler.CHARGER_MAX_ENERGY, 6);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	
		@Override
		public void update() {
			if (worldObj.isRemote)
				return;

			int stored = getEnergyStored(null);
			for (ItemStack stack : inventory) {
				if (stored <= 0)
					return;
				if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.stackSize == 1) {
					IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
					int received = item.receiveEnergy(stack, stored, false);
					extractEnergy(null, received, false);

					stored = getEnergyStored(null);
				}
			}
		}
	 
}