package transportterminal.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.TeleportMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import transportterminal.utils.DimensionUtils;

public class ItemRemote extends ItemEnergy {

	public ItemRemote() {
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
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.CHIP) {
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
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.CHIP) {
						ItemStack chipStack = tile.getStackInSlot(slot);
						if (chipStack.getTagCompound() != null && !chipStack.getTagCompound().hasKey("chipX")) {
							if (!world2.isRemote) {
								tile.setTempSlot(slot);
								chipStack.getTagCompound().setString("dimName", player.worldObj.provider.getDimensionType().getName());
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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
	    if (hasTag(stack) && player.isSneaking() && hand.equals(EnumHand.MAIN_HAND)) {
	    	if(!world.isRemote){
	    		if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1) {
	    			world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
	    			return EnumActionResult.FAIL;
	    		}
	    		int dimensionID = stack.getTagCompound().getInteger("dim");
	    		int homeX = stack.getTagCompound().getInteger("homeX");
	    		int homeY = stack.getTagCompound().getInteger("homeY");
	    		int homeZ = stack.getTagCompound().getInteger("homeZ");
	    		DimensionUtils.forceChunkloading((EntityPlayerMP) player, dimensionID, homeX, homeY, homeZ);
			
	    		if (foundFreeChip(player, stack)) {
	    			world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.OK_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
	    			player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_REMOTE, world, pos.getX(), pos.getY(), pos.getZ());
	    			return EnumActionResult.SUCCESS;
	    		}

	    		if (!foundFreeChip(player, stack))
	    			world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
	    	}
	    	if (world.isRemote)
	    		if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1)
	    			player.addChatMessage(new TextComponentTranslation("chat.end_disabled_message"));
	    }
	    return EnumActionResult.FAIL;
	}

	@Override
	 public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (!player.isSneaking() && stack.getTagCompound().hasKey("homeX") && hand.equals(EnumHand.MAIN_HAND)) {
			int x = stack.getTagCompound().getInteger("homeX");
			int y = stack.getTagCompound().getInteger("homeY");
			int z = stack.getTagCompound().getInteger("homeZ");
			int newDim = stack.getTagCompound().getInteger("dim");
			if (!world.isRemote)
				if (canTeleport(stack))
					extractEnergy(stack, ConfigHandler.ENERGY_PER_REMOTE_USE, false);
			if (world.isRemote)
				if (canTeleport(stack))
					TransportTerminal.NETWORK_WRAPPER.sendToServer(new TeleportMessage(player, x, y, z, newDim));
		}
		return new ActionResult(EnumActionResult.PASS, stack);
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