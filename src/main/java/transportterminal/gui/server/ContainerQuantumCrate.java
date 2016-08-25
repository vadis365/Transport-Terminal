package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.TransportTerminal;
import transportterminal.gui.slot.SlotRemoteCrate;
import transportterminal.tileentites.TileEntityQuantumCrate;

public class ContainerQuantumCrate extends ContainerEnergy {

	public int numRows = 8;

	public ContainerQuantumCrate(EntityPlayer player, TileEntityQuantumCrate tile) {
		super(player, tile);
		InventoryPlayer playerInventory = player.inventory;
		
		int i = (numRows - 4) * 18;
		int j;
		int k;

		addSlotToContainer(new SlotRemoteCrate(tile, 104, 12, 171));
		addSlotToContainer(new SlotRemoteCrate(tile, 105, 12, 207));
		
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
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack is = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack is1 = slot.getStack();
			is = is1.copy();

			if (slotIndex < (numRows * 13) + 2) {
				if (!mergeItemStack(is1, (numRows * 13) + 2, inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(is1, 0, (numRows * 13) + 2, false))
				return null;

			if (is1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();

			if (is1.stackSize != is.stackSize)
				slot.onPickupFromSlot(player, is1);
			else
				return null;
		}

		return is;
	}
	
	@Override
	public ItemStack slotClick(int slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == TransportTerminal.REMOTE_QUANTUM_CRATE)
			if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItemMainhand())
				return null;
		return super.slotClick(slot, dragType, clickTypeIn, player);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}
}