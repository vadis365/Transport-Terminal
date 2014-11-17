package transportterminal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void addRecipes() {
		ItemStack nbtChip = new ItemStack(TransportTerminal.chip);
		nbtChip.setTagCompound(new NBTTagCompound());
		ItemStack nbtPlayerChip = new ItemStack(TransportTerminal.playerChip);
		nbtPlayerChip.setTagCompound(new NBTTagCompound());

		addShapelessRecipe(nbtChip, Items.potato, Blocks.sand);
		addShapelessRecipe(nbtChip, "ingotIron", Blocks.sand, "dustRedstone");
		addShapelessRecipe(nbtPlayerChip, Items.name_tag, "gemDiamond", TransportTerminal.chip);

		addShapedRecipe(new ItemStack(TransportTerminal.remote), "RER", " C ", "IBI", 'R', "dustRedstone", 'E', Items.ender_pearl, 'C', TransportTerminal.chip, 'I', "ingotIron", 'B', Blocks.stone_button);
		addShapedRecipe(new ItemStack(TransportTerminal.terminal), "EGE", "IRI", "SSS", 'E', Items.ender_pearl, 'E', Items.ender_pearl, 'G', "blockGlass", 'I', "ingotIron", 'R', "blockRedstone", 'S', Blocks.stone_slab);
	}

	private static void addShapelessRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, parameters));
	}

	private static void addShapedRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, parameters));
	}
}