package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.network.message.ContainerMessage;
import transportterminal.tileentites.TileEntityInventoryEnergy;

public abstract class ContainerEnergy extends Container {

	protected final TileEntityInventoryEnergy tile;
	private int lastEnergyAmount = -1;

	protected ContainerEnergy(TileEntityInventoryEnergy tile) {
		this.tile = tile;
	}

	public TileEntityInventoryEnergy tile() {
		return tile;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!TransportTerminal.IS_RF_PRESENT)
			return;
		int currentEnergyAmount = tile.getEnergyStored(ForgeDirection.UNKNOWN);
		if (currentEnergyAmount == lastEnergyAmount)
			return;

		for (Object obj : crafters)
			if (obj instanceof EntityPlayerMP)
				TransportTerminal.networkWrapper.sendTo(new ContainerMessage(windowId, currentEnergyAmount), (EntityPlayerMP) obj);

		lastEnergyAmount = currentEnergyAmount;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}