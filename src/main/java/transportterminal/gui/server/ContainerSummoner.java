package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.TransportTerminal;
import transportterminal.gui.slot.SlotChip;
import transportterminal.tileentites.TileEntitySummoner;

public class ContainerSummoner extends ContainerEnergy {

	private final int numRows = 2;

	public ContainerSummoner(EntityPlayer player, TileEntitySummoner tile) {
		super(player, tile);
		InventoryPlayer playerInventory = player.inventory;
		int i = (numRows - 4) * 18;

		addSlotToContainer(new SlotChip(tile, 0, 80, 9));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 0) {
				if (stack1.getItem() == TransportTerminal.PLAYER_CHIP)
					if (!mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;
			} else if (!mergeItemStack(stack1, 1, inventorySlots.size(), false))
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