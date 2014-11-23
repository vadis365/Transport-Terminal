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
import transportterminal.gui.slot.SlotRemote;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class ContainerTerminalShadow extends Container {

	private final int numRows = 2;
	private TileEntityTransportTerminal tile;
	private int id;

	public ContainerTerminalShadow(InventoryPlayer playerInventory, TileEntityTransportTerminal tile, int id) {
		this.tile = tile;
		this.id = id;
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

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if(id == 0) {
			if (!TransportTerminal.IS_RF_PRESENT)
				return;
			for (int i = 0; i < crafters.size(); i++)
				((ICrafting) crafters.get(i)).sendProgressBarUpdate(this, 0, tile.getEnergyStored(ForgeDirection.UNKNOWN));
		}
	}

	@Override
	public void updateProgressBar(int id, int value) {
		if (!TransportTerminal.IS_RF_PRESENT)
			return;
		if (id == 0)
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
}