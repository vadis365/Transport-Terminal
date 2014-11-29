package transportterminal.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
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
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI")
public class ItemRemoteTerminal extends Item implements IEnergyContainerItem {

	private Ticket ticket;
	private final int capacity;

	public ItemRemoteTerminal() {
		capacity = ConfigHandler.REMOTE_TERMINAL_MAX_ENERGY;
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
				list.add("Terminal X: " + stack.getTagCompound().getInteger("homeX"));
				list.add("Terminal Y: " + stack.getTagCompound().getInteger("homeY"));
				list.add("Terminal Z: " + stack.getTagCompound().getInteger("homeZ"));
			} else {
				list.add("Right click to open");
				list.add("your linked Terminal.");
			}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && hasTag(stack) && !player.isSneaking()) {
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
		if (!player.isSneaking() && stack.stackTagCompound.hasKey("homeX"))
			if (!world.isRemote)
				if (canTeleport(stack)) {
					extractEnergy(stack, ConfigHandler.ENERGY_PER_TELEPORT, false);
					world.playSoundEffect(player.posX, player.posY, player.posZ, "transportterminal:oksound", 1.0F, 1.0F);
					player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_REMOTE_TERMINAL, world, (int) player.posX, (int) player.posY, (int) player.posZ);
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