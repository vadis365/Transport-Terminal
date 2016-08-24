package transportterminal.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import transportterminal.items.ItemRemoteQuantumCrate;

public class SlotRemoteCrate extends Slot {

	public SlotRemoteCrate(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
		slotNumber = slotIndex;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemRemoteQuantumCrate;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		System.out.println("PICKED UP Here" +slotNumber);
		if (stack != null && slotNumber == 0)
			inventory.setInventorySlotContents(1, null);

		if (stack != null && slotNumber == 1) {
			inventory.setInventorySlotContents(0, null);
			System.out.println("PICKED UP");
			stack.getTagCompound().setString("dimName", player.worldObj.provider.getDimensionType().getName());
			stack.getTagCompound().setInteger("dim", player.dimension);
		}
		super.onPickupFromSlot(player, stack);
	}
}