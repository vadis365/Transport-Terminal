package transportterminal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import transportterminal.blocks.BlockCharger;
import transportterminal.blocks.BlockChipUtilities;
import transportterminal.blocks.BlockEnergyCube;
import transportterminal.blocks.BlockGenerator;
import transportterminal.blocks.BlockItemTransporter;
import transportterminal.blocks.BlockMetalCrate;
import transportterminal.blocks.BlockQuantumCrate;
import transportterminal.blocks.BlockSummoner;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.reference.Reference;

public class ModBlocks {

	private static final List<Block> BLOCKS = new LinkedList<Block>();
	public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<ItemBlock>();
	public static final Block TERMINAL = new BlockTransportTerminal().setHardness(3.0F);
	public static final Block UTILS = new BlockChipUtilities().setHardness(3.0F);
	public static final Block CHARGER = new BlockCharger().setHardness(3.0F);
	public static final Block SUMMONER = new BlockSummoner().setHardness(3.0F);
	public static final Block ENERGY_CUBE = new BlockEnergyCube().setHardness(3.0F);
	public static final Block GENERATOR = new BlockGenerator().setHardness(3.0F);
	public static final Block METAL_CRATE = new BlockMetalCrate().setHardness(3.0F);
	public static final Block QUANTUM_CRATE = new BlockQuantumCrate().setHardness(3.0F);
	public static final Block ITEM_TRANSPORTER = new BlockItemTransporter().setHardness(3.0F);

	public static void init() {
		try {
			for (Field field : ModBlocks.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof Block) {
					Block block = (Block) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerBlock(name, block);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void registerBlock(String name, Block block) {
		BLOCKS.add(block);
		block.setRegistryName(Reference.MOD_ID, name).setUnlocalizedName(Reference.MOD_ID + "." + name);

		ItemBlock item;
		if (block instanceof IHasCustomItem)
			item = ((IHasCustomItem) block).getItemBlock();
		else
			item = new ItemBlock(block);
		ITEM_BLOCKS.add(item);
		item.setRegistryName(Reference.MOD_ID, name).setUnlocalizedName(Reference.MOD_ID + "." + name);
	}
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	public static class RegistrationHandlerBlocks {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (Block block : BLOCKS) {
				registry.register(block);
			}
		}

		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
				for (ItemBlock item : ITEM_BLOCKS) {
				registry.register(item);
			}
		}
		
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			for (Block block : BLOCKS)
				if (block instanceof ISubBlocksBlock) {
					List<String> models = ((ISubBlocksBlock) block).getModels();
					for (int i = 0; i < models.size(); i++)
						ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(Reference.MOD_ID + ":blocks/" + models.get(i), "inventory"));
				} else {
					ResourceLocation name = block.getRegistryName();
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":blocks/" + name.getResourcePath(), "inventory"));
				}
		}
	}

	public static interface IHasCustomItem {
		ItemBlock getItemBlock();
	}

	public static interface ISubBlocksBlock {
		List<String> getModels();
	}
}