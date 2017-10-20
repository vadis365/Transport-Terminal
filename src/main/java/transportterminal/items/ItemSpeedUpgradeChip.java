package transportterminal.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.ModItems.ISubItemsItem;
import transportterminal.TransportTerminal;

public class ItemSpeedUpgradeChip extends Item implements ISubItemsItem {

	public ItemSpeedUpgradeChip() {
		super();
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		list.add("Speed Modifier: x" + (stack.getMetadata() + 2));
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
		list.add(new ItemStack(this, 1, 2));
	}

	@Override
	public List<String> getModels() {
		List<String> models = new ArrayList<String>();
		models.add("upgrade_chip_red");
		models.add("upgrade_chip_green");
		models.add("upgrade_chip_blue");
		return models;
	}
}