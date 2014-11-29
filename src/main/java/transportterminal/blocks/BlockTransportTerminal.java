package transportterminal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class BlockTransportTerminal extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockTransportTerminal() {
		super(Material.iron);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTransportTerminal();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}


    private void func_176445_e(World worldIn, BlockPos p_176445_2_, IBlockState p_176445_3_)
    {
        if (!worldIn.isRemote)
        {
            Block block = worldIn.getBlockState(p_176445_2_.offsetNorth()).getBlock();
            Block block1 = worldIn.getBlockState(p_176445_2_.offsetSouth()).getBlock();
            Block block2 = worldIn.getBlockState(p_176445_2_.offsetWest()).getBlock();
            Block block3 = worldIn.getBlockState(p_176445_2_.offsetEast()).getBlock();
            EnumFacing enumfacing = (EnumFacing)p_176445_3_.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(p_176445_2_, p_176445_3_.withProperty(FACING, enumfacing), 2);
        }
    }
    
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.func_174811_aO().getOpposite()), 2);
    }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		if (worldIn.getTileEntity(pos) != null)
			player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_TERMINAL, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityTransportTerminal)
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityTransportTerminal) tileentity);
		super.breakBlock(worldIn, pos, state);
	}

    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_180356_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002111";

            static
            {
                try
                {
                    field_180356_a[EnumFacing.WEST.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.EAST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_180356_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}