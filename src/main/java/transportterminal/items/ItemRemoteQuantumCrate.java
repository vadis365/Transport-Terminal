package transportterminal.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.ModSounds;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.tileentites.TileEntityInventoryEnergy;
import transportterminal.tileentites.TileEntityQuantumCrate;
import transportterminal.utils.DimensionUtils;
import transportterminal.utils.TeleportUtils;

public class ItemRemoteQuantumCrate extends ItemEnergy {

	public ItemRemoteQuantumCrate() {
		super(ConfigHandler.REMOTE_QUANTUM_CRATE_MAX_ENERGY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		super.addInformation(stack, worldIn, list, flag);
		if (hasTag(stack))
			if (stack.getTagCompound().hasKey("dim")) {
				list.add("Crate Dimension: " + stack.getTagCompound().getInteger("dim") + " " + stack.getTagCompound().getString("dimName"));
				list.add("Crate X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Crate Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Crate Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click to open");
				list.add("your linked Quantum Crate.");
			}
	}

	@Override
	 public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getTagCompound().hasKey("homeX") && hand.equals(EnumHand.MAIN_HAND)) {
			if (!world.isRemote) {
				int dimensionID = stack.getTagCompound().getInteger("dim");
				int homeX = stack.getTagCompound().getInteger("homeX");
				int homeY = stack.getTagCompound().getInteger("homeY");
				int homeZ = stack.getTagCompound().getInteger("homeZ");
				DimensionUtils.forceChunkloading((EntityPlayerMP) player, dimensionID, homeX, homeY, homeZ);
				if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1) {
					world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
					return new ActionResult(EnumActionResult.FAIL, stack);
				}
				if (canTeleport(stack)) {

					WorldServer world2 = DimensionUtils.getWorldFromDimID(dimensionID);
					TileEntity tile = world2.getTileEntity(new BlockPos(homeX, homeY, homeZ));
					if (tile instanceof TileEntityInventoryEnergy) {
						if(((TileEntityInventoryEnergy) tile).getEnergyStored(null) >= ConfigHandler.ENERGY_PER_CRATE) {
							TeleportUtils.consumeQuantumCrateEnergy((TileEntityQuantumCrate) tile);
							extractEnergy(stack, ConfigHandler.ENERGY_PER_REMOTE_USE, false);
							world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.OK_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
							player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_REMOTE_QUANTUM_CRATE, world, (int) player.posX, (int) player.posY, (int) player.posZ);
						}
						else {
							world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
							return new ActionResult(EnumActionResult.FAIL, stack);
						}
					}
				}
			}
			if (world.isRemote)
				if(stack.getTagCompound().getInteger("dim") == 1 && player.dimension != 1)
					player.sendStatusMessage(new TextComponentTranslation("chat.end_crate_disabled_message"), false);
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