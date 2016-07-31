package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import transportterminal.tileentites.TileEntityGenerator;

public class ContainerGenerator extends ContainerEnergy {
	private final int numRows = 2;

	public ContainerGenerator(EntityPlayer player, TileEntityGenerator tileentity) {
		super(player, tileentity);
		InventoryPlayer playerInventory = player.inventory;
		int i = (numRows - 4) * 18;
		
		addSlotToContainer(new Slot(tile, 0, 80, 53));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 0) {
				if (stack1.getItem() == Items.REDSTONE || stack1.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
					if (!mergeItemStack(stack1, 0, 1, false))
						return null;
			} else if (!mergeItemStack(stack1, 1, inventorySlots.size(), false))
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