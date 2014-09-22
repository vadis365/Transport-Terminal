package TransportTerminal.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import TransportTerminal.TransportTerminal;
import TransportTerminal.network.TeleportMessage;
import TransportTerminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTransportTerminalRemote extends Item {

	private Ticket ticket;

	public ItemTransportTerminalRemote() {
		super();
		setMaxStackSize(1);
		setCreativeTab(TransportTerminal.creativeTabsTT);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (hasTag(stack))
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

	public static boolean foundFreeChip(EntityPlayer player, ItemStack stack) {
		if (hasTag(stack) && player.isSneaking() && stack.stackTagCompound.hasKey("dim")) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			if (world2 == null)
				return false;

			int homeX = stack.getTagCompound().getInteger("homeX");
			int homeY = stack.getTagCompound().getInteger("homeY");
			int homeZ = stack.getTagCompound().getInteger("homeZ");

			TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(homeX, homeY, homeZ);
			if (tile != null)
				for (int slot = 2; slot < 16; slot++)
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.transportTerminalChip) {
						ItemStack chipStack = tile.getStackInSlot(slot);
						if (chipStack.stackTagCompound != null && !chipStack.stackTagCompound.hasKey("chipX"))
							return true;
					}
		}
		return false;
	}

	public static TileEntityTransportTerminal getTile(EntityPlayer player, ItemStack stack, int x, int y, int z) {
		if (hasTag(stack) && player.isSneaking() && stack.stackTagCompound.hasKey("dim")) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			if (world2 == null)
				return null;

			int homeX = stack.getTagCompound().getInteger("homeX");
			int homeY = stack.getTagCompound().getInteger("homeY");
			int homeZ = stack.getTagCompound().getInteger("homeZ");

			TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(homeX, homeY, homeZ);
			if (tile != null)
				for (int slot = 2; slot < 16; slot++)
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.transportTerminalChip) {
						ItemStack chipStack = tile.getStackInSlot(slot);
						if (chipStack.stackTagCompound != null && !chipStack.stackTagCompound.hasKey("chipX")) {
							if (!world2.isRemote) {
								tile.setTempSlot(slot);
								chipStack.getTagCompound().setString("dimName", player.worldObj.provider.getDimensionName());
								chipStack.getTagCompound().setInteger("chipDim", player.dimension);
								chipStack.getTagCompound().setInteger("chipX", x);
								chipStack.getTagCompound().setInteger("chipY", y);
								chipStack.getTagCompound().setInteger("chipZ", z);
							}
							return tile;
						}
					}
		}
		return null;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && hasTag(stack) && player.isSneaking()) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));

			if (ticket == null)
				ticket = ForgeChunkManager.requestTicket(TransportTerminal.instance, world2, ForgeChunkManager.Type.NORMAL);

			if (ticket != null)
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(stack.getTagCompound().getInteger("homeX"), stack.getTagCompound().getInteger("homeZ")));
		
			if (foundFreeChip(player, stack)) {
				world.playSoundEffect(player.posX, player.posY, player.posZ, "transportterminal:oksound", 1.0F, 1.0F);
				player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_REMOTE, world, x, y, z);
				return true;
			}
			
			if (!foundFreeChip(player, stack))
				world.playSoundEffect(player.posX, player.posY, player.posZ, "transportterminal:errorsound", 1.0F, 1.0F);
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote && !player.isSneaking() && stack.stackTagCompound.hasKey("homeX")) {
			int x = stack.getTagCompound().getInteger("homeX");
			int y = stack.getTagCompound().getInteger("homeY");
			int z = stack.getTagCompound().getInteger("homeZ");
			int newDim = stack.getTagCompound().getInteger("dim");
			TransportTerminal.networkWrapper.sendToServer(new TeleportMessage(player, x, y, z, newDim));
		}
		return stack;
	}

	private static boolean hasTag(ItemStack stack) {
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
	
	public void releaseChunkLoad() {
		
	}
}
