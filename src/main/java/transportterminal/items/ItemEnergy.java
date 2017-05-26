package transportterminal.items;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;

@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI")
public abstract class ItemEnergy extends Item implements IEnergyContainerItem {

	private final int capacity;

	public ItemEnergy(int capacity) {
		this.capacity = capacity;
		setMaxStackSize(1);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - (double) getEnergyStored(stack) / (double) getMaxEnergyStored(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDurabilityForDisplay(stack) > 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	 public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		super.getSubItems(item, tab, list);
			ItemStack charged = new ItemStack(item);
			receiveEnergy(charged, capacity, false);
			list.add(charged);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add("Charge: " + getEnergyStored(stack) + "RF / " + getMaxEnergyStored(stack) + "RF");
	}

	/* ENERGY */

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (container.getTagCompound() == null)
			container.setTagCompound(new NBTTagCompound());
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, maxReceive);

		if (!simulate) {
			energy += energyReceived;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy"))
			return 0;
		int energy = container.getTagCompound().getInteger("Energy");
		int energyExtracted = Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy"))
			return 0;
		return container.getTagCompound().getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return capacity;
	}

	/* TELEPORT */

	protected static boolean hasTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			return false;
		}
		return true;
	}

	protected boolean canTeleport(ItemStack stack) {
		return getEnergyStored(stack) >= ConfigHandler.ENERGY_PER_REMOTE_USE;
	}
}
