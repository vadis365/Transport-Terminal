package transportterminal.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import transportterminal.ModItems;
import transportterminal.TransportTerminal;
import transportterminal.gui.client.GuiCharger;
import transportterminal.gui.client.GuiChipUtils;
import transportterminal.gui.client.GuiConsole;
import transportterminal.gui.client.GuiEnergyCube;
import transportterminal.gui.client.GuiGenerator;
import transportterminal.gui.client.GuiItemTransporter;
import transportterminal.gui.client.GuiMetalCrate;
import transportterminal.gui.client.GuiNaming;
import transportterminal.gui.client.GuiQuantumCrate;
import transportterminal.gui.client.GuiRemoteQuantumCrate;
import transportterminal.gui.client.GuiSummoner;
import transportterminal.gui.client.GuiUtilsNaming;
import transportterminal.gui.client.GuiWirelessConsole;
import transportterminal.gui.server.ContainerCharger;
import transportterminal.gui.server.ContainerChipUtils;
import transportterminal.gui.server.ContainerEnergyCube;
import transportterminal.gui.server.ContainerGenerator;
import transportterminal.gui.server.ContainerItemTransporter;
import transportterminal.gui.server.ContainerMetalCrate;
import transportterminal.gui.server.ContainerQuantumCrate;
import transportterminal.gui.server.ContainerSummoner;
import transportterminal.gui.server.ContainerTerminal;
import transportterminal.items.ItemRemote;
import transportterminal.tileentites.TileEntityCharger;
import transportterminal.tileentites.TileEntityChipUtilities;
import transportterminal.tileentites.TileEntityEnergyCube;
import transportterminal.tileentites.TileEntityGenerator;
import transportterminal.tileentites.TileEntityItemTransporter;
import transportterminal.tileentites.TileEntityMetalCrate;
import transportterminal.tileentites.TileEntityQuantumCrate;
import transportterminal.tileentites.TileEntitySummoner;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class CommonProxy implements IGuiHandler {

	public final int GUI_ID_TERMINAL = 0, GUI_ID_REMOTE = 1, GUI_ID_CHIP_UTILS = 2, GUI_ID_CHIP_UTILS_NAMING = 3, GUI_ID_CHARGER = 4, GUI_ID_REMOTE_TERMINAL = 5, GUI_ID_SUMMONER = 6, GUI_ID_ENERGY_CUBE = 7, GUI_ID_GENERATOR = 8, GUI_ID_METAL_CRATE = 9, GUI_ID_QUANTUM_CRATE = 10, GUI_ID_REMOTE_QUANTUM_CRATE = 11, GUI_ID_ITEM_TRANSPORTER = 12;

	public void registerRenderInformation() {
	}

	public void registerTileEntities() {
		registerTileEntity(TileEntityTransportTerminal.class, "console");
		registerTileEntity(TileEntityChipUtilities.class, "utils");
		registerTileEntity(TileEntityCharger.class, "charger");
		registerTileEntity(TileEntitySummoner.class, "summoner");
		registerTileEntity(TileEntityEnergyCube.class, "energy_cube");
		registerTileEntity(TileEntityGenerator.class, "generator");
		registerTileEntity(TileEntityMetalCrate.class, "metal_crate");
		registerTileEntity(TileEntityQuantumCrate.class, "quantum_crate");
		registerTileEntity(TileEntityItemTransporter.class, "item_transporter");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.transportterminal." + baseName);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityTransportTerminal)
				return new ContainerTerminal(player, (TileEntityTransportTerminal) tileentity, 0);
		}

		if (ID == GUI_ID_REMOTE) {
			ItemStack stack = player.getHeldItemMainhand();
			world = DimensionManager.getWorld(player.dimension);
			ItemRemote.getTile(player, stack, x, y, z);
			return new ContainerTerminal(player, null, 1);
		}

		if (ID == GUI_ID_CHIP_UTILS) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityChipUtilities)
				return new ContainerChipUtils(player, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHIP_UTILS_NAMING)
			return new ContainerChipUtils(player, null);

		if (ID == GUI_ID_CHARGER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityCharger)
				return new ContainerCharger(player, (TileEntityCharger) tileentity);
		}

		if (ID == GUI_ID_REMOTE_TERMINAL) {
			if (getTile(player, world, x, y, z, true) instanceof TileEntityTransportTerminal)
				return new ContainerTerminal(player, (TileEntityTransportTerminal) getTile(player, world, x, y, z, false), 0);
		}

		if (ID == GUI_ID_SUMMONER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntitySummoner)
				return new ContainerSummoner(player, (TileEntitySummoner) tileentity);
		}

		if (ID == GUI_ID_ENERGY_CUBE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityEnergyCube)
				return new ContainerEnergyCube(player, (TileEntityEnergyCube) tileentity);
		}

		if (ID == GUI_ID_GENERATOR) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityGenerator)
				return new ContainerGenerator(player, (TileEntityGenerator) tileentity);
		}

		if (ID == GUI_ID_METAL_CRATE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityMetalCrate)
				return new ContainerMetalCrate(player, (TileEntityMetalCrate) tileentity);
		}

		if (ID == GUI_ID_QUANTUM_CRATE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityQuantumCrate)
				return new ContainerQuantumCrate(player, (TileEntityQuantumCrate) tileentity);
		}

		if (ID == GUI_ID_REMOTE_QUANTUM_CRATE) {
			if (getTile(player, world, x, y, z, true) instanceof TileEntityQuantumCrate)
				return new ContainerQuantumCrate(player, (TileEntityQuantumCrate) getTile(player, world, x, y, z, false));
		}

		if (ID == GUI_ID_ITEM_TRANSPORTER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityItemTransporter)
				return new ContainerItemTransporter(player, (TileEntityItemTransporter) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_TERMINAL) {
			TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileentity instanceof TileEntityTransportTerminal)
				return new GuiConsole(player, (TileEntityTransportTerminal) tileentity, 0);
		}

		if (ID == GUI_ID_REMOTE)
			return new GuiNaming(player);

		if (ID == GUI_ID_CHIP_UTILS) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityChipUtilities)
				return new GuiChipUtils(player, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHIP_UTILS_NAMING) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityChipUtilities)
				return new GuiUtilsNaming(player, (TileEntityChipUtilities) tileentity);
		}

		if (ID == GUI_ID_CHARGER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityCharger)
				return new GuiCharger(player, (TileEntityCharger) tileentity);
		}

		if (ID == GUI_ID_REMOTE_TERMINAL)
			return new GuiWirelessConsole(player);

		if (ID == GUI_ID_SUMMONER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntitySummoner)
				return new GuiSummoner(player, (TileEntitySummoner) tileentity);
		}

		if (ID == GUI_ID_ENERGY_CUBE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityEnergyCube)
				return new GuiEnergyCube(player, (TileEntityEnergyCube) tileentity);
		}

		if (ID == GUI_ID_GENERATOR) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityGenerator)
				return new GuiGenerator(player, (TileEntityGenerator) tileentity);
		}

		if (ID == GUI_ID_METAL_CRATE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityMetalCrate)
				return new GuiMetalCrate(player, (TileEntityMetalCrate) tileentity);
		}

		if (ID == GUI_ID_QUANTUM_CRATE) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityQuantumCrate)
				return new GuiQuantumCrate(player, (TileEntityQuantumCrate) tileentity);
		}

		if (ID == GUI_ID_REMOTE_QUANTUM_CRATE)
			return new GuiRemoteQuantumCrate(player);

		if (ID == GUI_ID_ITEM_TRANSPORTER) {
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityItemTransporter)
				return new GuiItemTransporter(player, (TileEntityItemTransporter) tileentity);
		}
		return null;
	}

	public TileEntity getTile(EntityPlayer player, World world, int x, int y, int z, boolean loadDimension) {
		TileEntity tileentity;
		ItemStack stack = player.getHeldItemMainhand();
		if (!stack.isEmpty() && (stack.getItem() == ModItems.REMOTE_TERMINAL || stack.getItem() == ModItems.REMOTE_QUANTUM_CRATE)) {
			WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
			int xx = stack.getTagCompound().getInteger("homeX");
			int yy = stack.getTagCompound().getInteger("homeY");
			int zz = stack.getTagCompound().getInteger("homeZ");
			BlockPos pos = new BlockPos(xx, yy, zz);
			tileentity = world2.getTileEntity(pos);
		} else {
			BlockPos pos = new BlockPos(x, y, z);
			tileentity = world.getTileEntity(pos);
		}
		return tileentity;
	}
}
