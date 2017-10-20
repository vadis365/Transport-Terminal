package transportterminal.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;

public class ItemChip extends Item {

	public ItemChip() {
		super();
		setMaxStackSize(1);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if (hasTag(stack))
			if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("chipDim")) {
				list.add(TextFormatting.GREEN + stack.getTagCompound().getString("description"));
				list.add("Dimension: " + stack.getTagCompound().getInteger("chipDim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Target X: " + stack.getTagCompound().getInteger("chipX"));
				list.add("Target Y: " + stack.getTagCompound().getInteger("chipY"));
				list.add("Target Z: " + stack.getTagCompound().getInteger("chipZ"));
			} else
				list.add("Empty Transport Chip");
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		stack.setTagCompound(new NBTTagCompound());
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
	public boolean hasEffect(ItemStack stack) {
		if (hasTag(stack))
			if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("chipDim"))
				return true;
		return false;
	}
}