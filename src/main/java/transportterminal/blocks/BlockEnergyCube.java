package transportterminal.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntityEnergyCube;
import transportterminal.tileentites.TileEntityInventoryEnergy;

public class BlockEnergyCube extends BlockContainer {

	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> NORTH = PropertyEnum.create("north", TileEntityEnergyCube.EnumStatus.class);
	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> SOUTH = PropertyEnum.create("south", TileEntityEnergyCube.EnumStatus.class);
	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> WEST = PropertyEnum.create("west", TileEntityEnergyCube.EnumStatus.class);
	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> EAST = PropertyEnum.create("east", TileEntityEnergyCube.EnumStatus.class);
	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> UP = PropertyEnum.create("up", TileEntityEnergyCube.EnumStatus.class);
	 public static final PropertyEnum<TileEntityEnergyCube.EnumStatus> DOWN = PropertyEnum.create("down", TileEntityEnergyCube.EnumStatus.class);

	 public BlockEnergyCube() {
		 super(Material.IRON);
		 setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEnergyCube();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
			return true;
	
        if (!world.isRemote) {
        	TileEntity tile = world.getTileEntity(pos);

        	if (tile instanceof TileEntityEnergyCube) {
        		TileEntityEnergyCube energyCube = (TileEntityEnergyCube) tile;

        		if(!player.isSneaking()) {
        			world.notifyBlockUpdate(pos, state, state, 3);
        			player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_ENERGY_CUBE, world, pos.getX(), pos.getY(), pos.getZ());
        		}
        		else
        			energyCube.toggleMode(side);

        		world.notifyBlockUpdate(pos, state, state, 3);
        	}
        }
        return true;
	}

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityEnergyCube) {
            TileEntityEnergyCube te = (TileEntityEnergyCube) tileEntity;
            TileEntityEnergyCube.EnumStatus north = te.getSideStatus(EnumFacing.NORTH);
            TileEntityEnergyCube.EnumStatus south = te.getSideStatus(EnumFacing.SOUTH);
            TileEntityEnergyCube.EnumStatus west = te.getSideStatus(EnumFacing.WEST);
            TileEntityEnergyCube.EnumStatus east = te.getSideStatus(EnumFacing.EAST);
            TileEntityEnergyCube.EnumStatus up = te.getSideStatus(EnumFacing.UP);
            TileEntityEnergyCube.EnumStatus down = te.getSideStatus(EnumFacing.DOWN);
            return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east).withProperty(UP, up).withProperty(DOWN, down);
        }
        return state.withProperty(NORTH, TileEntityEnergyCube.EnumStatus.STATUS_NONE)
                .withProperty(SOUTH, TileEntityEnergyCube.EnumStatus.STATUS_NONE)
                .withProperty(WEST, TileEntityEnergyCube.EnumStatus.STATUS_NONE)
                .withProperty(EAST, TileEntityEnergyCube.EnumStatus.STATUS_NONE)
                .withProperty(UP, TileEntityEnergyCube.EnumStatus.STATUS_NONE)
                .withProperty(DOWN, TileEntityEnergyCube.EnumStatus.STATUS_NONE);
    }

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityEnergyCube) {
				NBTTagCompound nbt = new NBTTagCompound();
				tileentity.writeToNBT(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
				if(((TileEntityEnergyCube) tileentity).getEnergyStored(null) > 0)
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
			if (tileentity instanceof TileEntityEnergyCube && stack.hasTagCompound() && stack.getTagCompound().hasKey("energy")) {
				int energy = stack.getTagCompound().getInteger("energy");
				((TileEntityInventoryEnergy) tileentity).setEnergy(energy);
			}
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}