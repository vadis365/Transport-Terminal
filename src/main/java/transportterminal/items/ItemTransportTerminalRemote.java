package transportterminal.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.TeleportMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI")
public class ItemTransportTerminalRemote extends Item implements IEnergyContainerItem {

	private final int capacity;

	public ItemTransportTerminalRemote() {
		capacity = ConfigHandler.REMOTE_MAX_ENERGY;
		setMaxStackSize(1);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - (double) getEnergyStored(stack) / (double) getMaxEnergyStored(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return TransportTerminal.IS_RF_PRESENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		super.getSubItems(item, tab, list);
		if (TransportTerminal.IS_RF_PRESENT) {
			ItemStack charged = new ItemStack(item);
			receiveEnergy(charged, ConfigHandler.REMOTE_MAX_ENERGY, false);
			list.add(charged);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (TransportTerminal.IS_RF_PRESENT)
			list.add("Charge: " + getEnergyStored(stack) + "RF / " + getMaxEnergyStored(stack) + "RF");
		if (hasTag(stack))
			if (stack.stackTagCompound.hasKey("dim")) {
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
		if (hasTag(stack) && player.isSneaking() && stack.stackTagCompound.hasKey("dim")) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			if (world2 == null)
				return false;

			int homeX = stack.getTagCompound().getInteger("homeX");
			int homeY = stack.getTagCompound().getInteger("homeY");
			int homeZ = stack.getTagCompound().getInteger("homeZ");
			
			TransportTerminal.proxy.forceChunkload(world2, homeX, homeY);
			
			TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(homeX, homeY, homeZ);
			if (tile != null)
				for (int slot = 2; slot < 16; slot++)
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.chip) {
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
					if (tile.getStackInSlot(slot) != null && tile.getStackInSlot(slot).getItem() == TransportTerminal.chip) {
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
		if (!player.isSneaking() && stack.stackTagCompound.hasKey("homeX")) {
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

	private static boolean hasTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			return false;
		}
		return true;
	}

	private boolean canTeleport(ItemStack stack) {
		return !TransportTerminal.IS_RF_PRESENT || getEnergyStored(stack) >= ConfigHandler.ENERGY_PER_TELEPORT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		if (hasTag(stack))
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("dim"))
				return true;
		return false;
	}

	/* ENERGY */

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (container.stackTagCompound == null)
			container.stackTagCompound = new NBTTagCompound();
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, maxReceive);

		if (!simulate) {
			energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy"))
			return 0;
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy"))
			return 0;
		return container.stackTagCompound.getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return capacity;
	}
}