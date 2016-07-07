package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import transportterminal.TransportTerminal;
import transportterminal.network.message.ContainerMessage;
import transportterminal.tileentites.TileEntityInventoryEnergy;

public abstract class ContainerEnergy extends Container {

	protected final TileEntityInventoryEnergy tile;
	protected final EntityPlayer player;
	private int lastEnergyAmount = -1;

	protected ContainerEnergy(TileEntityInventoryEnergy tile, EntityPlayer player) {
		this.tile = tile;
		this.player = player;
	}

	public TileEntityInventoryEnergy tile() {
		return tile;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!TransportTerminal.IS_RF_PRESENT)
			return;
		int currentEnergyAmount = tile.getEnergyStored(null);
		if (currentEnergyAmount == lastEnergyAmount)
			return;

		TransportTerminal.NETWORK_WRAPPER.sendTo(new ContainerMessage(windowId, currentEnergyAmount), (EntityPlayerMP) player);
		lastEnergyAmount = currentEnergyAmount;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}