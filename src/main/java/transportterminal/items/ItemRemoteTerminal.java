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

public class ItemRemoteTerminal extends ItemEnergy {

	private Ticket ticket;

	public ItemRemoteTerminal() {
		super(ConfigHandler.REMOTE_TERMINAL_MAX_ENERGY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		if (hasTag(stack))
			if (stack.getTagCompound().hasKey("dim")) {
				list.add("Terminal Dimension: " + stack.getTagCompound().getInteger("dim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Terminal X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Terminal Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Terminal Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click to open");
				list.add("your linked Terminal.");
			}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hasTag(stack) && !playerIn.isSneaking()) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));

			if (ticket == null)
				ticket = ForgeChunkManager.requestTicket(TransportTerminal.instance, world2, ForgeChunkManager.Type.NORMAL);

			if (ticket != null)
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(stack.getTagCompound().getInteger("homeX"), stack.getTagCompound().getInteger("homeZ")));
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!player.isSneaking() && stack.getTagCompound().hasKey("homeX"))
			if (!world.isRemote)
				if (canTeleport(stack)) {
					extractEnergy(stack, ConfigHandler.ENERGY_PER_TELEPORT, false);
					world.playSoundEffect(player.posX, player.posY, player.posZ, "transportterminal:oksound", 1.0F, 1.0F);
					player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_REMOTE_TERMINAL, world, (int) player.posX, (int) player.posY, (int) player.posZ);
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