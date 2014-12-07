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
		for (int i = 0; i < crafters.size(); i++)
			TransportTerminal.networkWrapper.sendTo(new ContainerMessage(windowId, tile.getEnergyStored(ForgeDirection.UNKNOWN)), (EntityPlayerMP) crafters.get(i));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}