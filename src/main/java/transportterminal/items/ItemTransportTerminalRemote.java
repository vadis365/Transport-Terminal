package transportterminal.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.TeleportMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class ItemTransportTerminalRemote extends ItemEnergy {

	private Ticket ticket;

	public ItemTransportTerminalRemote() {
		super(ConfigHandler.REMOTE_MAX_ENERGY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		if (hasTag(stack))
			if (stack.getTagCompound().hasKey("dim")) {
				list.add("Terminal Dimension: " + stack.getTagCompound().getInteger("dim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Target X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Target Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Target Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click to Teleport");
				list.add("to your linked Terminal.");
				list.add("Sneak + Right click");
				list.add("to save Location.");
			}
	}

	public static boolean foundFreeChip(EntityPlayer player, ItemStack stack) {
		if (hasTag(stack) && player.isSneaking() && stack.getTagCompound().hasKey("dim")) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			if (world2 == null)
				return false;

			int homeX = stack.getTagCompound().getInteger("homeX");
			int homeY = stack.getTagCompound().getInteger("homeY");
			int homeZ = stack.getTagCompound().getInteger("homeZ");
			BlockPos pos = new BlockPos(homeX, homeY, homeZ);
			TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(pos);
			if (tile != null)
				for (int slot = 2; slot < 16; slot++)
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.chip) {
						ItemStack chipStack = tile.getStackInSlot(slot);
						if (chipStack.getTagCompound() != null && !chipStack.getTagCompound().hasKey("chipX"))
							return true;
					}
		}
		return false;
	}

	public static TileEntityTransportTerminal getTile(EntityPlayer player, ItemStack stack, int x, int y, int z) {
		if (hasTag(stack) && player.isSneaking() && stack.getTagCompound().hasKey("dim")) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			if (world2 == null)
				return null;

			int homeX = stack.getTagCompound().getInteger("homeX");
			int homeY = stack.getTagCompound().getInteger("homeY");
			int homeZ = stack.getTagCompound().getInteger("homeZ");
			BlockPos pos = new BlockPos(homeX, homeY, homeZ);
			TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(pos);
			if (tile != null)
				for (int slot = 2; slot < 16; slot++)
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.chip) {
						ItemStack chipStack = tile.getStackInSlot(slot);
						if (chipStack.getTagCompound() != null && !chipStack.getTagCompound().hasKey("chipX")) {
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
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hasTag(stack) && playerIn.isSneaking()) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));

			if (ticket == null)
				ticket = ForgeChunkManager.requestTicket(TransportTerminal.instance, world2, ForgeChunkManager.Type.NORMAL);

			if (ticket != null)
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(stack.getTagCompound().getInteger("homeX"), stack.getTagCompound().getInteger("homeZ")));

			if (foundFreeChip(playerIn, stack)) {
				worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "transportterminal:oksound", 1.0F, 1.0F);
				playerIn.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_REMOTE, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}

			if (!foundFreeChip(playerIn, stack))
				worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "transportterminal:errorsound", 1.0F, 1.0F);
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!player.isSneaking() && stack.getTagCompound().hasKey("homeX")) {
			int x = stack.getTagCompound().getInteger("homeX");
			int y = stack.getTagCompound().getInteger("homeY");
			int z = stack.getTagCompound().getInteger("homeZ");
			int newDim = stack.getTagCompound().getInteger("dim");
			if (!world.isRemote)
				if (canTeleport(stack))
					extractEnergy(stack, ConfigHandler.ENERGY_PER_TELEPORT, false);
			if (world.isRemote)
				if (canTeleport(stack))
					TransportTerminal.networkWrapper.sendToServer(new TeleportMessage(player, x, y, z, newDim));
		}
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		if (hasTag(stack))
			if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("dim"))
				return true;
		return false;
	}
}