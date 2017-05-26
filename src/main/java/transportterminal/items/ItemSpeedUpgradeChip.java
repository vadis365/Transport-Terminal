package transportterminal.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;

public class ItemSpeedUpgradeChip extends Item {

	public ItemSpeedUpgradeChip() {
		super();
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add("Speed Modifier: x" + (stack.getMetadata() + 2));
	}

	@Override
	@SideOnly(Side.CLIENT)
	 public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack((item), 1, 0));
		list.add(new ItemStack((item), 1, 1));
		list.add(new ItemStack((item), 1, 2));
	}
}