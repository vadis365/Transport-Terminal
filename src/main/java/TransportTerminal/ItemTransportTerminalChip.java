package TransportTerminal;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTransportTerminalChip extends Item {
	public ItemTransportTerminalChip() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (hasTag(stack)) {
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("chipDim")) {
				list.add(stack.getTagCompound().getString("description"));
				list.add("Dimension: " + stack.getTagCompound().getInteger("chipDim")  + " " + stack.getTagCompound().getString("dimName"));
				list.add("Target X: " + stack.getTagCompound().getInteger("chipX"));
				list.add("Target Y: " + stack.getTagCompound().getInteger("chipY"));
				list.add("Target Z: " + stack.getTagCompound().getInteger("chipZ"));
			} else {
				list.add("Empty Transport Chip");
			}
		}
	}

	private boolean hasTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			return false;
		}
		return true;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		if (hasTag(stack))
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("chipDim"))
				return true;
		return false;
	}
}