package transportterminal.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
import transportterminal.tileentites.TileEntityQuantumCrate;

public class BlockQuantumCrate extends BlockDirectional {

	public BlockQuantumCrate() {
		super(Material.IRON);
		setCreativeTab(TransportTerminal.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityQuantumCrate();
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
		if (world.getTileEntity(pos) instanceof TileEntityQuantumCrate) {
			world.notifyBlockUpdate(pos, state, state, 3);
			player.openGui(TransportTerminal.INSTANCE, TransportTerminal.PROXY.GUI_ID_QUANTUM_CRATE, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityQuantumCrate) {
				for (int i = ((TileEntityInventoryEnergy) tileentity).getSizeInventory() - 2 ; i < ((TileEntityInventoryEnergy) tileentity).getSizeInventory(); ++i) {
					ItemStack itemstack = ((TileEntityInventoryEnergy) tileentity).getStackInSlot(i);
					if (itemstack != null) {
						InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
						((TileEntityInventoryEnergy) tileentity).setInventorySlotContents(i, null);
					}
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
		if(!world.isRemote && stack.hasTagCompound() && stack.getTagCompound().hasKey("Items")) {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof TileEntityQuantumCrate) {
				NBTTagList tags = stack.getTagCompound().getTagList("Items", 10);
				((TileEntityQuantumCrate) tileentity).inventory = new ItemStack[((TileEntityQuantumCrate) tileentity).getSizeInventory()];

				for (int i = 0; i < tags.tagCount(); i++) {
					NBTTagCompound data = tags.getCompoundTagAt(i);
					int j = data.getByte("Slot") & 255;

					if (j >= 0 && j < ((TileEntityQuantumCrate) tileentity).inventory.length)
						((TileEntityQuantumCrate) tileentity).inventory[j] = ItemStack.loadItemStackFromNBT(data);
				}

				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("energy")) {
					int energy = stack.getTagCompound().getInteger("energy");
					((TileEntityInventoryEnergy) tileentity).setEnergy(energy);
				}
			}
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

}