package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.ModItems;
import transportterminal.gui.slot.SlotChip;
import transportterminal.tileentites.TileEntityChipUtilities;

public class ContainerChipUtils extends Container {

	public int numRows = 2;

	public ContainerChipUtils(EntityPlayer player, TileEntityChipUtilities tile) {
		InventoryPlayer playerInventory = player.inventory;
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
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 1) {
				if (stack1.getItem() == ModItems.CHIP || stack1.getItem() == ModItems.PLAYER_CHIP) {
					if (!mergeItemStack(stack1, 0, inventorySlots.size(), false))
						return ItemStack.EMPTY;
				} else if (stack1.getItem() != ModItems.CHIP || stack1.getItem() != ModItems.PLAYER_CHIP)
					if (!mergeItemStack(stack1, 0, 1, true))
						return ItemStack.EMPTY;
			} else if (!mergeItemStack(stack1, 2, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if (stack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if (stack1.getCount() != stack.getCount())
				slot.onTake(player, stack1);
			else
				return ItemStack.EMPTY;
		}
		return stack;
	}
}
