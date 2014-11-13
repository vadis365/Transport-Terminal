package TransportTerminal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import TransportTerminal.TransportTerminal;
import TransportTerminal.tileentites.TileEntityCharger;

public class ContainerCharger extends Container {
	
	public int numRows = 2;

	public ContainerCharger(InventoryPlayer playerInventory, TileEntityCharger tile) {
		int i = (numRows - 4) * 18;
		
		addSlotToContainer(new Slot(tile, 0, 62, 9)); // Top
		addSlotToContainer(new Slot(tile, 1, 62, 31)); // Middle
		addSlotToContainer(new Slot(tile, 2, 62, 53)); // Bottom
		
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
				if (stack1.getItem() == TransportTerminal.transportTerminalRemote) {
					if (!mergeItemStack(stack1, 0, inventorySlots.size(), false))
						return null;
				} else if (stack1.getItem() != TransportTerminal.transportTerminalRemote)
					if (!mergeItemStack(stack1, 0, 3, true))
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

