package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import transportterminal.TransportTerminal;
import transportterminal.network.message.ContainerMessage;
import transportterminal.tileentites.TileEntityInventoryEnergy;

public abstract class ContainerEnergy extends Container {
	protected final EntityPlayer player;
	protected final TileEntityInventoryEnergy tile;
	private int lastEnergyAmount = -1;

	protected ContainerEnergy(EntityPlayer player, TileEntityInventoryEnergy tile) {
		this.player = player;
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
		int currentEnergyAmount = tile.getEnergyStored(null);
		if (currentEnergyAmount == lastEnergyAmount)
			return;
		 
		for (Object obj : listeners)  
			if (obj instanceof EntityPlayerMP)  
				TransportTerminal.NETWORK_WRAPPER.sendTo(new ContainerMessage(windowId, currentEnergyAmount), (EntityPlayerMP) obj);  

		lastEnergyAmount = currentEnergyAmount;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}