package transportterminal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntitySummoner;

public class BlockSummoner extends BlockDirectional {

	public BlockSummoner() {
		super(Material.iron);
		setCreativeTab(TransportTerminal.tab);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySummoner();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		if (world.getTileEntity(pos) instanceof TileEntitySummoner)
			player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_SUMMONER, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TileEntitySummoner)
			InventoryHelper.dropInventoryItems(world, pos, (TileEntitySummoner) tileentity);
		super.breakBlock(world, pos, state);
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