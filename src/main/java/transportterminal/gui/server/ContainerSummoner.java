package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.gui.slot.SlotChip;
import transportterminal.tileentites.TileEntitySummoner;

public class ContainerSummoner extends Container {

	private final int numRows = 2;
	private TileEntitySummoner tile;

	public ContainerSummoner(InventoryPlayer playerInventory, TileEntitySummoner tile) {
		this.tile = tile;
		int i = (numRows - 4) * 18;

			addSlotToContainer(new SlotChip(tile, 0, 80, 18));

			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 9; k++)
					addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!TransportTerminal.IS_RF_PRESENT)
			return;
		for (int i = 0; i < crafters.size(); i++)
			((ICrafting) crafters.get(i)).sendProgressBarUpdate(this, 0, tile.getEnergyStored(ForgeDirection.UNKNOWN));
	}

	@Override
	public void updateProgressBar(int id, int value) {
		if (!TransportTerminal.IS_RF_PRESENT)
			return;
			tile.setEnergy(value);
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
			if (slotIndex > 0) {
				if (stack1.getItem() == TransportTerminal.playerChip) {
					if (!mergeItemStack(stack1, 0, 1, false))
						return null;
				}
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