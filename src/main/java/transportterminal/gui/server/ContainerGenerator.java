package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import transportterminal.ModItems;
import transportterminal.gui.slot.SlotUpgradeChip;
import transportterminal.tileentites.TileEntityGenerator;

public class ContainerGenerator extends ContainerEnergy {
	private final int numRows = 2;
	TileEntityGenerator generator;
	public ContainerGenerator(EntityPlayer player, TileEntityGenerator tileentity) {
		super(player, tileentity);
		InventoryPlayer playerInventory = player.inventory;
		generator = tileentity;
		int i = (numRows - 4) * 18;
		
		addSlotToContainer(new Slot(tile, 0, 80, 53));
		addSlotToContainer(new SlotUpgradeChip(tile, 1, 44, 53));

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
			if (slotIndex > 1) {
				if (stack1.getItem() == Items.REDSTONE || stack1.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
					if (!mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.UPGRADE_CHIP)
					if (!mergeItemStack(stack1, 1, 2, false))
						return ItemStack.EMPTY;
			} else if (!mergeItemStack(stack1, 2, inventorySlots.size(), false))
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
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		generator.sendGUIData(this, listener);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener listener : listeners) {
			generator.sendGUIData(this, listener);
		}
	}

	@Override
	public void updateProgressBar(int id, int value) {
		generator.getGUIData(id, value);
	}
}