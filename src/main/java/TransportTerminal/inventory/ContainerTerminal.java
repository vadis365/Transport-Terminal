package TransportTerminal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import TransportTerminal.TransportTerminal;
import TransportTerminal.tileentites.TileEntityTransportTerminal;

public class ContainerTerminal extends Container {

	private final int numRows = 2;
	private TileEntityTransportTerminal tile;

	public ContainerTerminal(InventoryPlayer playerInventory, TileEntityTransportTerminal tile, int id) {
		this.tile = tile;
		int i = (numRows - 4) * 18;

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
/*  TODO FIX THIS (it causes a crash when using the remote with Sneak + R Click)
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); i++)
			((ICrafting) crafters.get(i)).sendProgressBarUpdate(this, 0, tile.getEnergyStored(ForgeDirection.UNKNOWN));
	}

	@Override
	public void updateProgressBar(int id, int value) {
		if (id == 0)
			tile.setEnergy(value);
	}
*/
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
				if (stack1.getItem() == TransportTerminal.transportTerminalChip ||stack1.getItem() == TransportTerminal.transportTerminalPlayerChip) {
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