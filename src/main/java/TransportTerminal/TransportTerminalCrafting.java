package TransportTerminal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class TransportTerminalCrafting {
	
	public static void addRecipes() {		
		GameRegistry.addShapelessRecipe(new ItemStack(TransportTerminal.transportTerminalChip, 1), new Object[] { Items.potato, Blocks.sand});
		GameRegistry.addShapelessRecipe(new ItemStack(TransportTerminal.transportTerminalChip, 1), new Object[] { Items.iron_ingot, Blocks.sand, Items.redstone});
		GameRegistry.addRecipe(new ItemStack(TransportTerminal.transportTerminalRemote, 1), new Object[] { "RER", " C ", "IBI", 'R', Items.redstone, 'E', Items.ender_pearl, 'C', TransportTerminal.transportTerminalChip, 'I', Items.iron_ingot, 'B', Blocks.stone_button });
		GameRegistry.addRecipe(new ItemStack(TransportTerminal.transportTerminal, 1), new Object[] { "EGE", "IRI", "SSS", 'E', Items.ender_pearl, 'E', Items.ender_pearl, 'G', Blocks.glass, 'I', Items.iron_ingot, 'R', Blocks.redstone_block, 'S', Blocks.stone_slab });
	}
}