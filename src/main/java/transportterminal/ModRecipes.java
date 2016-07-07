package transportterminal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void addRecipes() {
		ItemStack nbtChip = new ItemStack(TransportTerminal.CHIP);
		nbtChip.setTagCompound(new NBTTagCompound());
		ItemStack nbtPlayerChip = new ItemStack(TransportTerminal.PLAYER_CHIP);
		nbtPlayerChip.setTagCompound(new NBTTagCompound());

		addShapelessRecipe(nbtChip, "cropPotato", Blocks.SAND);
		addShapelessRecipe(nbtChip, "ingotIron", Blocks.SAND, "dustRedstone");
		addShapelessRecipe(nbtPlayerChip, Items.NAME_TAG, "gemDiamond", TransportTerminal.CHIP);

		addShapedRecipe(new ItemStack(TransportTerminal.REMOTE), "RER", " C ", "IBI", 'R', "dustRedstone", 'E', Items.ENDER_PEARL, 'C', TransportTerminal.CHIP, 'I', "ingotIron", 'B', Blocks.STONE_BUTTON);
		addShapedRecipe(new ItemStack(TransportTerminal.REMOTE_TERMINAL), "IGI", "RXR", "ICI", 'R', "dustRedstone", 'G', "blockGlass", 'C', TransportTerminal.CHIP, 'I', "ingotIron", 'X', TransportTerminal.REMOTE);
		addShapedRecipe(new ItemStack(TransportTerminal.TERMINAL), "EGE", "IRI", "SSS", 'E', Items.ENDER_EYE, 'G', "blockGlass", 'I', "blockIron", 'R', "blockRedstone", 'S', Blocks.STONE_SLAB);
		addShapedRecipe(new ItemStack(TransportTerminal.UTILS), "CTC", "IRI", "xxx", 'C', TransportTerminal.CHIP, 'T', Items.NAME_TAG, 'I', "blockIron", 'R', "blockRedstone", 'x', "ingotIron");
		addShapedRecipe(new ItemStack(TransportTerminal.SUMMONER), "P P", "ERE", "P P", 'P', TransportTerminal.PLAYER_CHIP, 'E', "gemEmerald", 'R', "blockRedstone");

		if (TransportTerminal.IS_RF_PRESENT)
			addShapedRecipe(new ItemStack(TransportTerminal.CHARGER), "CRC", "GQG", "CLC", 'C', TransportTerminal.CHIP, 'G', "blockGold", 'Q', "blockQuartz", 'R', "blockRedstone", 'L', "blockLapis");
	}

	private static void addShapelessRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, parameters));
	}

	private static void addShapedRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, parameters));
	}
}