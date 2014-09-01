package TransportTerminal;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockTransportTerminal extends BlockContainer {

	protected BlockTransportTerminal() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabTools);
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

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack is) {
		int rot = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, rot == 0 ? 2 : rot == 1 ? 5 : rot == 2 ? 3 : 4, 3);
		System.out.println("Metadata is: "+ world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityTransportTerminal) {
			ItemStack current = player.inventory.getCurrentItem();
			if (current != null && current.getItem() == Item.getItemFromBlock(this) || current != null && current.getItem() == TransportTerminal.transportTerminalRemote) {
				return false;
			}
			player.openGui(TransportTerminal.instance, TransportTerminal.proxy.GUI_ID_TERMINAL, world, x, y, z);
		}
		return true;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB box, List list, Entity entity) {
		float pixel = 0.0625F; // 1 pixel
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 2) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, pixel * 2, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
			setBlockBounds(0.0F, 0.0F, pixel * 13, 1.0F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
		}
		
		if (meta == 3) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, pixel * 2, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, pixel * 3);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
		}
		
		if (meta == 4) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, pixel * 2, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
			setBlockBounds(pixel * 13, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);	
		}
		
		if (meta == 5) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, pixel * 2, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
			setBlockBounds(0.0F, 0.0F, 0.0F, pixel * 3, 1.0F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, box, list, entity);
		}
		//setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
