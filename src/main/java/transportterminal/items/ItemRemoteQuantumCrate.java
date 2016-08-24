package transportterminal.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
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
import transportterminal.tileentites.TileEntityQuantumCrate;
import transportterminal.utils.DimensionUtils;
import transportterminal.utils.TeleportUtils;

public class ItemRemoteQuantumCrate extends ItemEnergy {

	public ItemRemoteQuantumCrate() {
		super(ConfigHandler.REMOTE_TERMINAL_MAX_ENERGY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		if (hasTag(stack))
			if (stack.getTagCompound().hasKey("dim")) {
				list.add("Crate Dimension: " + stack.getTagCompound().getInteger("dim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Crate X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Crate Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Crate Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click to open");
				list.add("your linked Crate.");
			}
	}

	@Override
	 public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (stack.getTagCompound().hasKey("homeX") && hand.equals(EnumHand.MAIN_HAND)) {
			if (!world.isRemote) {
				if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1) {
					world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
					return new ActionResult(EnumActionResult.FAIL, stack);
				}
				if (canTeleport(stack)) {
					int dimensionID = stack.getTagCompound().getInteger("dim");
					int homeX = stack.getTagCompound().getInteger("homeX");
					int homeY = stack.getTagCompound().getInteger("homeY");
					int homeZ = stack.getTagCompound().getInteger("homeZ");
					DimensionUtils.forceChunkloading((EntityPlayerMP) player, dimensionID, homeX, homeY, homeZ);
					extractEnergy(stack, ConfigHandler.ENERGY_PER_TELEPORT, false);
					world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.OK_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
					player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_REMOTE_QUANTUM_CRATE, world, (int) player.posX, (int) player.posY, (int) player.posZ);
				
					WorldServer world2 = DimensionManager.getWorld(dimensionID);
					BlockPos pos = new BlockPos(homeX, homeY, homeZ);
					TileEntity tile = world2.getTileEntity(pos);
					if(tile instanceof TileEntityQuantumCrate)
						TeleportUtils.consumeQuantumCrateEnergy((TileEntityQuantumCrate) tile);
				}
			}
			if (world.isRemote)
				if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1)
					player.addChatMessage(new TextComponentTranslation("chat.end_disabled_message"));
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