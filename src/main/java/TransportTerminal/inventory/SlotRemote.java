package TransportTerminal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import TransportTerminal.items.ItemTransportTerminalRemote;

public class SlotRemote extends Slot {
	public SlotRemote(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
		this.slotNumber = slotIndex;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemTransportTerminalRemote;
	}

	@Override	
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if(stack !=null && slotNumber == 0)
        	inventory.setInventorySlotContents(1, null);
        
        if(stack !=null && slotNumber == 1) {
        	inventory.setInventorySlotContents(0, null);
			stack.getTagCompound().setString("dimName", player.worldObj.provider.getDimensionName());
			stack.getTagCompound().setInteger("dim", player.dimension);
        }
        super.onPickupFromSlot(player, stack);
    }
}
