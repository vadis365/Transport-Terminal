package transportterminal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;

public class BlockGenerator extends BlockDirectional {

	public BlockGenerator() {
		super(Material.IRON);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;//new TileEntityGenerator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
/*
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
			return true;
		if (world.getTileEntity(pos) instanceof TileEntityGenerator) {
			world.notifyBlockUpdate(pos, state, state, 3);
			player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_GENERATOR, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityGenerator) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityGenerator) tileentity);

				for (int i = 0; i < ((TileEntityInventoryEnergy) tileentity).getSizeInventory(); ++i) {
					ItemStack itemstack = ((TileEntityInventoryEnergy) tileentity).getStackInSlot(i);
					if (itemstack != null)
						((TileEntityGenerator) tileentity).setInventorySlotContents(i, null);
				}

				NBTTagCompound nbt = new NBTTagCompound();
				tileentity.writeToNBT(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
				stack.setTagCompound(nbt);
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if(!world.isRemote) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityGenerator)
				tileentity.readFromNBT(stack.getTagCompound());
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
*/
}