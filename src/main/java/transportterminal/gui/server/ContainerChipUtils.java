package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.TransportTerminal;
import transportterminal.gui.slot.SlotChip;
import transportterminal.tileentites.TileEntityChipUtilities;

public class ContainerChipUtils extends Container {

	public int numRows = 2;

	public ContainerChipUtils(InventoryPlayer playerInventory, TileEntityChipUtilities tile) {
		int i = (numRows - 4) * 18;

		addSlotToContainer(new SlotChip(tile, 0, 62, 9)); // Origin
		addSlotToContainer(new SlotChip(tile, 1, 98, 9)); // Result

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
			if (slotIndex > 1) {
				if (stack1.getItem() == TransportTerminal.chip || stack1.getItem() == TransportTerminal.playerChip) {
					if (!mergeItemStack(stack1, 0, inventorySlots.size(), false))
						return null;
				} else if (stack1.getItem() != TransportTerminal.chip || stack1.getItem() != TransportTerminal.playerChip)
					if (!mergeItemStack(stack1, 0, 1, true))
						return null;
			} else if (!mergeItemStack(stack1, 2, inventorySlots.size(), false))
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