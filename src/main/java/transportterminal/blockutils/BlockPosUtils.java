package transportterminal.blockutils;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPosUtils {

	public static Block getBlock(World world, int x, int y, int z, BlockPos pos) {
		return world.getBlockState(pos.add(x - pos.getX(), y - pos.getY(), z - pos.getZ())).getBlock();
	}

	public static void setBlock(World world, int x, int y, int z, BlockPos pos, Block block) {
		world.setBlockState(pos.add(x - pos.getX(), y - pos.getY(), z - pos.getZ()), block.getDefaultState());
	}
}