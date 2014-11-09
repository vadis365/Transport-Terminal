package TransportTerminal.recipescreativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import TransportTerminal.TransportTerminal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabsTransportTerminal extends CreativeTabs {

	public CreativeTabsTransportTerminal(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return TransportTerminal.transportTerminalRemote;
	}
}