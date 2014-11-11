package TransportTerminal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import TransportTerminal.TransportTerminal;
import TransportTerminal.tileentites.TileEntityChipUtilities;

public class ContainerChipUtils  extends Container {
	
	public int numRows = 2;

	public ContainerChipUtils(InventoryPlayer playerInventory, TileEntityChipUtilities tile) {
		int i = (numRows - 4) * 18;

		addSlotToContainer(new SlotChip(tile, 0, 62, 45)); // Slot for Result Chip
		addSlotToContainer(new SlotChip(tile, 1, 98, 9)); // Slot for Blank Chip
		addSlotToContainer(new SlotChip(tile, 2, 62, 9)); // Slot for Origin Chip
		
		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 2) {
				if (stack1.getItem() == TransportTerminal.transportTerminalChip || stack1.getItem() == TransportTerminal.transportTerminalPlayerChip) {
					if (!mergeItemStack(stack1, 1, inventorySlots.size(), false))
						return null;
				} else if (stack1.getItem() != TransportTerminal.transportTerminalChip || stack1.getItem() != TransportTerminal.transportTerminalPlayerChip)
					if (!mergeItemStack(stack1, 1, 2, true))
						return null;
			} else if (!mergeItemStack(stack1, 3, inventorySlots.size(), false))
				return null;
			if (stack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
			if (stack1.stackSize != stack.stackSize)
				slot.onPickupFromSlot(player, stack1);
			else
				return null;
		}
		return stack;
	}
}

