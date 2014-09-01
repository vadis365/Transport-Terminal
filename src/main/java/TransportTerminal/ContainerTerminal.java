package TransportTerminal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTerminal extends Container {

	public int numRows = 2;
	public String name;
	private TileEntityTransportTerminal tileTerminal;
	public ContainerTerminal(InventoryPlayer playerInventory, TileEntityTransportTerminal tile, int id) {
		tileTerminal = tile;
		int i = (numRows - 4) * 18;
		int j;
		int k;
		if (id == 0) {
		addSlotToContainer(new SlotRemote(tile, 0, 15, 18));
		addSlotToContainer(new SlotRemote(tile, 1, 15, 54));

		for (j = 0; j < numRows; ++j) {
			for (k = 2; k < 9; ++k) {
				addSlotToContainer(new SlotChip(tile, k + j * 7, 8 + k * 18, 27 + j * 18));
			}
		}

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
			}
		}
		for (j = 0; j < 9; ++j) {
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
		}
		}
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
			if (slotIndex > 15) {
				if (stack1.getItem() == TransportTerminal.transportTerminalChip) {
					if (!mergeItemStack(stack1, 2, 16, false))
						return null;
				} else if (stack1.getItem() == TransportTerminal.transportTerminalRemote)
					if (!mergeItemStack(stack1, 0, 1, true))
						return null;
			} else if (!mergeItemStack(stack1, 16, inventorySlots.size(), false))
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