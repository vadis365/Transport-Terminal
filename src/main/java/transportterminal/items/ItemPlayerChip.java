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

public class ItemPlayerChip extends Item {

	public ItemPlayerChip() {
		super();
		setMaxStackSize(1);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if (hasTag(stack))
			if (stack.getTagCompound() != null && stack.hasDisplayName())
				list.add(TextFormatting.GREEN + "Player: " + stack.getDisplayName());
			else
				list.add("Empty Player Location Chip");
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
			if (stack.getTagCompound() != null && stack.hasDisplayName())
				return true;
		return false;
	}
}