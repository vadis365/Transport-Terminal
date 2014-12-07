package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.TransportTerminal;
import transportterminal.gui.slot.SlotChip;
import transportterminal.gui.slot.SlotRemote;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class ContainerTerminal extends ContainerEnergy {

	private final int numRows = 2;
	private int idType;
	public ContainerTerminal(InventoryPlayer playerInventory, TileEntityTransportTerminal tile, int id) {
		super(tile);
		int i = (numRows - 4) * 18;
		idType = id;

		if (id == 0) {
			addSlotToContainer(new SlotRemote(tile, 0, 15, 18));
			addSlotToContainer(new SlotRemote(tile, 1, 15, 54));

			for (int j = 0; j < numRows; j++)
				for (int k = 2; k < 9; k++)
					addSlotToContainer(new SlotChip(tile, k + j * 7, 8 + k * 18, 27 + j * 18));

			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 9; k++)
					addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 15) {
				if (stack1.getItem() == TransportTerminal.chip || stack1.getItem() == TransportTerminal.playerChip) {
					if (!mergeItemStack(stack1, 2, 16, false))
						return null;
				} else if (stack1.getItem() == TransportTerminal.remote)
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
	
	@Override
	public void detectAndSendChanges() {
		if (idType == 0)
			super.detectAndSendChanges();	
	}
}