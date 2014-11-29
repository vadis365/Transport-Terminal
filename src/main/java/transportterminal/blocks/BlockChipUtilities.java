package transportterminal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntityChipUtilities;

public class BlockChipUtilities extends BlockDirectional {

	public BlockChipUtilities() {
		super(Material.iron);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityChipUtilities();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		if (world.getTileEntity(pos) instanceof TileEntityChipUtilities)
			player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_CHIP_UTILS, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityChipUtilities)
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityChipUtilities) tile);
		super.breakBlock(world, pos, state);
	}
}