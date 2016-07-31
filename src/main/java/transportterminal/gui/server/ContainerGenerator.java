package transportterminal.gui.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import transportterminal.tileentites.TileEntityGenerator;

public class ContainerGenerator extends ContainerEnergy {
	private final int numRows = 2;

	public ContainerGenerator(EntityPlayer player, TileEntityGenerator tileentity) {
		super(player, tileentity);
		InventoryPlayer playerInventory = player.inventory;
		int i = (numRows - 4) * 18;
		
		addSlotToContainer(new Slot(tile, 0, 80, 53));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 122 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 180 + i));
	}
}