package transportterminal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void addRecipes() {
		ItemStack nbtChip = new ItemStack(TransportTerminal.chip);
		nbtChip.setTagCompound(new NBTTagCompound());
		ItemStack nbtPlayerChip = new ItemStack(TransportTerminal.playerChip);
		nbtPlayerChip.setTagCompound(new NBTTagCompound());

		addShapelessRecipe(nbtChip, "cropPotato", Blocks.sand);
		addShapelessRecipe(nbtChip, "ingotIron", Blocks.sand, "dustRedstone");
		addShapelessRecipe(nbtPlayerChip, Items.name_tag, "gemDiamond", TransportTerminal.chip);

		addShapedRecipe(new ItemStack(TransportTerminal.remote), "RER", " C ", "IBI", 'R', "dustRedstone", 'E', Items.ender_pearl, 'C', TransportTerminal.chip, 'I', "ingotIron", 'B', Blocks.stone_button);
		addShapedRecipe(new ItemStack(TransportTerminal.remoteTerminal), "IGI", "RXR", "ICI", 'R', "dustRedstone", 'G', "blockGlass", 'C', TransportTerminal.chip, 'I', "ingotIron", 'X', TransportTerminal.remote);
		addShapedRecipe(new ItemStack(TransportTerminal.terminal), "EGE", "IRI", "SSS", 'E', Items.ender_eye, 'G', "blockGlass", 'I', "blockIron", 'R', "blockRedstone", 'S', Blocks.stone_slab);
		addShapedRecipe(new ItemStack(TransportTerminal.utils), "CTC", "IRI", "xxx", 'C', TransportTerminal.chip, 'T', Items.name_tag, 'I', "blockIron", 'R', "blockRedstone", 'x', "ingotIron");
		if (TransportTerminal.IS_RF_PRESENT) // removes crash if derps try to craft it
			addShapedRecipe(new ItemStack(TransportTerminal.charger), "CRC", "GQG", "CLC", 'C', TransportTerminal.chip, 'G', "blockGold", 'Q', "blockQuartz", 'R', "blockRedstone", 'L', "blockLapis");
		addShapedRecipe(new ItemStack(TransportTerminal.summoner), "P P", "ERE", "P P", 'P', TransportTerminal.playerChip, 'E', "gemEmerald", 'R', "blockRedstone");
	}

	private static void addShapelessRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, parameters));
	}

	private static void addShapedRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, parameters));
	}
}