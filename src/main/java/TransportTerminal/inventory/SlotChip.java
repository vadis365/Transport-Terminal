package TransportTerminal.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import TransportTerminal.items.ItemTransportTerminalChip;
import TransportTerminal.items.ItemTransportTerminalPlayerChip;

public class SlotChip extends Slot {

	public SlotChip(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemTransportTerminalChip || stack.getItem() instanceof ItemTransportTerminalPlayerChip;
	}
}