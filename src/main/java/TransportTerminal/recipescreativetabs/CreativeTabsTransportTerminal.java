package TransportTerminal.recipescreativetabs;

import TransportTerminal.TransportTerminal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class CreativeTabsTransportTerminal extends CreativeTabs  {
	
	public CreativeTabsTransportTerminal(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return TransportTerminal.transportTerminalRemote;
	}

}
