package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.tileentites.TileEntityMetalCrate;

public class ContainerMetalCrate extends Container {

	public int numRows = 8;

	public ContainerMetalCrate(EntityPlayer player, TileEntityMetalCrate tile) {
		InventoryPlayer playerInventory = player.inventory;
		
		int i = (numRows - 4) * 18;
		int j;
		int k;

		for (j = 0; j < numRows; ++j)
			for (k = 0; k < 13; ++k)
				addSlotToContainer(new Slot(tile, k + j * 13, 12 + k * 18, 15 + j * 18));

		for (j = 0; j < 3; ++j)
			for (k = 0; k < 9; ++k)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 48 + k * 18, 99 + j * 18 + i));

		for (j = 0; j < 9; ++j)
			addSlotToContainer(new Slot(playerInventory, j, 48 + j * 18, 157 + i));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
		ItemStack is = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {
			ItemStack is1 = slot.getStack();
			is = is1.copy();

			if (par2 < numRows * 13) {
				if (!mergeItemStack(is1, numRows * 13, inventorySlots.size(), true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(is1, 0, numRows * 13, false))
				return ItemStack.EMPTY;

			if (is1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}

		return is;
	}
}