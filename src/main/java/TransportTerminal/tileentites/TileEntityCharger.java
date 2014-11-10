package TransportTerminal.tileentites;

import net.minecraft.item.ItemStack;
import TransportTerminal.TransportTerminal;
import cofh.api.energy.IEnergyContainerItem;

public class TileEntityCharger extends TileEntityInventoryEnergy {

	public TileEntityCharger() {
		super(TransportTerminal.CHARGER_MAX_ENERGY, 1);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof IEnergyContainerItem;
	}
}