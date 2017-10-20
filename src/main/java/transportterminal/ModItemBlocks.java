package transportterminal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItemBlocks {

	public static final ItemBlock TERMINAL_ITEM = new ItemBlock(ModBlocks.TERMINAL) {
			@Override
			@SideOnly(Side.CLIENT)
		    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Used to teleport to locations stored on chips.");
				list.add("Can used for teleporting to other players.");
			}
		};

		public static final ItemBlock UTILS_ITEM = new ItemBlock(ModBlocks.UTILS) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Used to copy, erase and name chips.");
				list.add("Requires no RF to run.");
			}
		};

		public static final ItemBlock SUMMONER_ITEM = new ItemBlock(ModBlocks.SUMMONER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Used to summon players to it's location.");
				list.add("Must contain a named player location chip to work.");
			}
		};

		public static final ItemBlock CHARGER_ITEM = new ItemBlock(ModBlocks.CHARGER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Can charge up to 6 RF items at a time.");
			}
		};

		public static final ItemBlock ENERGY_CUBE_ITEM = new ItemBlock(ModBlocks.ENERGY_CUBE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Stores and outputs RF.");
				list.add("All sides configurable.");
				list.add("Right Click to open configuration gui or");
				list.add("Sneak + Right Click with empty hand to toggle side's state.");
			}
		};

		public static final ItemBlock GENERATOR_ITEM = new ItemBlock(ModBlocks.GENERATOR) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("Uses redstone to provide RF.");
			}
		};

		public static final ItemBlock METAL_CRATE_ITEM = new ItemBlock(ModBlocks.METAL_CRATE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {

				if (stack.hasTagCompound() && stack.getTagCompound().getTagList("Items", 10) != null) {
					NBTTagList tags = stack.getTagCompound().getTagList("Items", 10);

					for (int i = 0; i < tags.tagCount(); i++) {
						NBTTagCompound data = tags.getCompoundTagAt(i);
						int j = data.getByte("Slot") & 255;

						if (i >= 0 && i <= 51 && !GuiControls.isShiftKeyDown()) {
							list.add("Slot " + (j + 1) + ": " + TextFormatting.GREEN
									+ new ItemStack(data).getDisplayName() + " x " + new ItemStack(data).getCount());

							if (i == 51)
								list.add("Hold Shift for more.");
						} else if (i > 51 && i <= 103 && GuiControls.isShiftKeyDown())
							list.add("Slot " + (j + 1) + ": " + TextFormatting.GREEN
									+ new ItemStack(data).getDisplayName() + " x " + new ItemStack(data).getCount());
					}
				}
			}
		};

		public static final ItemBlock QUANTUM_CRATE_ITEM = new ItemBlock(ModBlocks.QUANTUM_CRATE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {

				if (stack.hasTagCompound() && stack.getTagCompound().getTagList("Items", 10) != null) {
					NBTTagList tags = stack.getTagCompound().getTagList("Items", 10);

					for (int i = 0; i < tags.tagCount(); i++) {
						NBTTagCompound data = tags.getCompoundTagAt(i);
						int j = data.getByte("Slot") & 255;

						if (i >= 0 && i <= 51 && !GuiControls.isShiftKeyDown()) {
							list.add("Slot " + (j + 1) + ": " + TextFormatting.GREEN
									+ new ItemStack(data).getDisplayName() + " x " + new ItemStack(data).getCount());

							if (i == 51)
								list.add("Hold Shift for more.");
						} else if (i > 51 && i <= 105 && GuiControls.isShiftKeyDown())
							list.add("Slot " + (j + 1) + ": " + TextFormatting.GREEN
									+ new ItemStack(data).getDisplayName() + " x " + new ItemStack(data).getCount());
					}
				}
			}
		};

		public static final ItemBlock ITEM_TRANSPORTER_ITEM = new ItemBlock(ModBlocks.ITEM_TRANSPORTER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
				list.add("WARNING! Not Fully Implemented.");
				list.add("Use at own risk! It May Crash!");
				list.add("Sends Items to stored chip's location.");
			}
		};
}
