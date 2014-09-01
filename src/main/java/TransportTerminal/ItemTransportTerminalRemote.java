package TransportTerminal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTransportTerminalRemote extends Item {
	public ItemTransportTerminalRemote() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (hasTag(stack)) {
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("dim")) {
				list.add("Terminal Dimension: " + stack.getTagCompound().getInteger("dim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Target X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Target Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Target Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click on a Block");
				list.add("to set as target.");
				list.add("Sneak + Right click");
				list.add("to save Location.");
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && hasTag(stack)) {
			Block block = world.getBlock(x, y, z);
			if (!world.isRemote && block != null && player.isSneaking()) {
				if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("dim")) {
					world = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
					int homeX = stack.getTagCompound().getInteger("homeX");
					int homeY = stack.getTagCompound().getInteger("homeY");
					int homeZ = stack.getTagCompound().getInteger("homeZ");
					TileEntity console = world.getTileEntity(homeX, homeY, homeZ);
					if (console != null && console instanceof TileEntityTransportTerminal) {
						for (int slot = 2; slot < 16; slot++)
							if (((IInventory) console).getStackInSlot(slot) != null && ((IInventory) console).getStackInSlot(slot).getItem() == TransportTerminal.transportTerminalChip) {
								ItemStack chipStack = ((IInventory) console).getStackInSlot(slot);
								if (chipStack.stackTagCompound != null && !chipStack.stackTagCompound.hasKey("chipX")) {
									((TileEntityTransportTerminal) console).setTempSlot(slot);
									chipStack.getTagCompound().setString("dimName", player.worldObj.provider.getDimensionName());
									chipStack.getTagCompound().setInteger("chipDim", player.dimension);
									chipStack.getTagCompound().setInteger("chipX", x);
									chipStack.getTagCompound().setInteger("chipY", y);
									chipStack.getTagCompound().setInteger("chipZ", z);
									System.out.println("Before Gui");
									player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_REMOTE, world, homeX, homeY, homeZ);
									System.out.println("After Gui");
									player.swingItem();
									//play sound good beep
									return true;
								}
								else {
									//play sound bad beep
								}
							}
					}
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && !player.isSneaking() && stack.stackTagCompound.hasKey("homeX")) {
			int x = stack.getTagCompound().getInteger("homeX");
			int y = stack.getTagCompound().getInteger("homeY");
			int z = stack.getTagCompound().getInteger("homeZ");
			player.swingItem();
			int newDim = stack.getTagCompound().getInteger("dim");
			TransportTerminal.networkWrapper.sendToServer(new TeleportMessage(player, x, y, z, newDim));
			player.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
		}
		return stack;
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
	public boolean hasEffect(ItemStack stack, int pass) {
		if (hasTag(stack))
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("dim"))
				return true;
		return false;
	}
}
