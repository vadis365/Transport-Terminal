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
import transportterminal.tileentites.TileEntityTransportTerminal;

public class BlockTransportTerminal extends BlockDirectional {

	public BlockTransportTerminal() {
		super(Material.iron);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTransportTerminal();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		if (worldIn.getTileEntity(pos) != null)
			player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_TERMINAL, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityTransportTerminal)
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTransportTerminal) tileentity);
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}
}