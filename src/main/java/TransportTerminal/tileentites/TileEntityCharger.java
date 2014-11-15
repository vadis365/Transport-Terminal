package TransportTerminal.tileentites;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import TransportTerminal.TransportTerminal;
import cofh.api.energy.IEnergyContainerItem;

public class TileEntityCharger extends TileEntityInventoryEnergy {

	public TileEntityCharger() {
		super(TransportTerminal.CHARGER_MAX_ENERGY, 6);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;

		int stored = getEnergyStored(ForgeDirection.UNKNOWN);
		for (ItemStack stack : inventory) {
			if (stored <= 0)
				return;
			if (stack != null) {
				IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
				int received = item.receiveEnergy(stack, stored, false);
				extractEnergy(ForgeDirection.UNKNOWN, received, false);

				stored = getEnergyStored(ForgeDirection.UNKNOWN);
			}
		}
	}
}