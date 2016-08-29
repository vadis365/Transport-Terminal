package transportterminal.blocks;

import java.util.Random;

import com.sun.istack.internal.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import transportterminal.TransportTerminal;
import transportterminal.tileentites.TileEntityInventoryEnergy;
import transportterminal.tileentites.TileEntityItemTransporter;

public class BlockItemTransporter extends BlockDirectional {

	public BlockItemTransporter() {
		super(Material.IRON);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityItemTransporter();
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

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
			return true;
		if (world.getTileEntity(pos) instanceof TileEntityItemTransporter) {
			world.notifyBlockUpdate(pos, state, state, 3);
			player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_ITEM_TRANSPORTER, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
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
			if (tileentity instanceof TileEntityItemTransporter) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityItemTransporter) tileentity);

				for (int i = 0; i < ((TileEntityInventoryEnergy) tileentity).getSizeInventory(); ++i) {
					ItemStack itemstack = ((TileEntityInventoryEnergy) tileentity).getStackInSlot(i);
					if (itemstack != null)
						((TileEntityItemTransporter) tileentity).setInventorySlotContents(i, null);
				}

				NBTTagCompound nbt = new NBTTagCompound();
				tileentity.writeToNBT(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
				if(((TileEntityItemTransporter) tileentity).getEnergyStored(null) > 0)
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
			if (tileentity instanceof TileEntityItemTransporter && stack.hasTagCompound() && stack.getTagCompound().hasKey("energy")) {
				int energy = stack.getTagCompound().getInteger("energy");
				((TileEntityInventoryEnergy) tileentity).setEnergy(energy);
			}
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
}