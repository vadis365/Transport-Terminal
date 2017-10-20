package transportterminal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import transportterminal.core.reference.Reference;

public class ModRecipes {
	public static final List<IRecipe> RECIPES = new ArrayList<IRecipe>();
	public static final ItemStack NBT_CHIP = new ItemStack(ModItems.CHIP);
	public static final ItemStack NBT_CHIP_PLAYER = new ItemStack(ModItems.PLAYER_CHIP);

	public static final IRecipe CHIP_TYPE_1 = new ShapelessOreRecipe(getResource("recipe_chip_1"), NBT_CHIP, "cropPotato", new ItemStack(Blocks.SAND));
	public static final IRecipe CHIP_TYPE_2 = new ShapelessOreRecipe(getResource("recipe_chip_2"), NBT_CHIP, "ingotIron", new ItemStack(Blocks.SAND), "dustRedstone");
	public static final IRecipe PLAYER_CHIP = new ShapelessOreRecipe(getResource("recipe_player_chip"), NBT_CHIP_PLAYER, new ItemStack(Items.NAME_TAG), "gemDiamond", new ItemStack(ModItems.CHIP));
	
	public static final IRecipe REMOTE = new ShapedOreRecipe(getResource("recipe_remote"), new ItemStack(ModItems.REMOTE), "RER", " C ", "IBI", 'R', "dustRedstone", 'E', new ItemStack(Items.ENDER_PEARL), 'C', new ItemStack(ModItems.CHIP), 'I', "ingotIron", 'B', new ItemStack(Blocks.STONE_BUTTON));
	public static final IRecipe REMOTE_TERMINAL = new ShapedOreRecipe(getResource("recipe_remote_terminal"), new ItemStack(ModItems.REMOTE_TERMINAL), "IGI", "RXR", "ICI", 'R', "dustRedstone", 'G', "blockGlass", 'C', new ItemStack(ModItems.CHIP), 'I', "ingotIron", 'X', new ItemStack(ModItems.REMOTE));
	public static final IRecipe TERMINAL = new ShapedOreRecipe(getResource("recipe_terminal"), new ItemStack(ModItemBlocks.TERMINAL_ITEM), "EGE", "IRI", "SSS", 'E', new ItemStack(Items.ENDER_EYE), 'G', "blockGlass", 'I', "blockIron", 'R', "blockRedstone", 'S', new ItemStack(Blocks.STONE_SLAB));
	public static final IRecipe UTILS = new ShapedOreRecipe(getResource("recipe_utils"), new ItemStack(ModItemBlocks.UTILS_ITEM), "CTC", "IRI", "xxx", 'C', new ItemStack(ModItems.CHIP), 'T', new ItemStack(Items.NAME_TAG), 'I', "blockIron", 'R', "blockRedstone", 'x', "ingotIron");
	public static final IRecipe SUMMONER = new ShapedOreRecipe(getResource("recipe_summoner"), new ItemStack(ModItemBlocks.SUMMONER_ITEM), "P P", "ERE", "P P", 'P', new ItemStack(ModItems.PLAYER_CHIP), 'E', "gemEmerald", 'R', "blockRedstone");
	public static final IRecipe CHARGER = new ShapedOreRecipe(getResource("recipe_charger"), new ItemStack(ModItemBlocks.CHARGER_ITEM), "CRC", "GQG", "CLC", 'C', new ItemStack(ModItems.CHIP), 'G', "blockGold", 'Q', "blockQuartz", 'R', "blockRedstone", 'L', "blockLapis");
	
	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(Reference.MOD_ID, inName);
	}

	public static void init() {
		makeNBTStuffForMePlease();
		try {
			for (Field field : ModRecipes.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof IRecipe) {
					IRecipe recipe = (IRecipe) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerRecipe(name, recipe);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static void makeNBTStuffForMePlease() {
		NBT_CHIP.setTagCompound(new NBTTagCompound());
		NBT_CHIP_PLAYER.setTagCompound(new NBTTagCompound());
	}

	public static void registerRecipe(String inName, IRecipe recipe) {
		RECIPES.add(recipe);
		recipe.setRegistryName(getResource(inName));
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	public static class RegistrationHandlerRecipes {

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
			final IForgeRegistry<IRecipe> registry = event.getRegistry();
			for (IRecipe recipes : RECIPES)
				registry.register(recipes);
		}
	}
}