package TransportTerminal;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class NamingMessage implements IMessage {

	 public int dimension, entityID, tileX, tileY, tileZ;
	String name;

	public NamingMessage() {}

	public NamingMessage(EntityPlayer player, int x, int y, int z, String string) {
		this.dimension = player.dimension;
		this.entityID = player.getEntityId();
		this.name = string;
		this.tileX = x;
		this.tileY = y;
		this.tileZ = z;
	 }
	 
	 /** enconding */
	@Override
	public void toBytes(ByteBuf buf) { 
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	 }

	/** decoding */
	@Override
	public void fromBytes(ByteBuf buf) { 
		this.dimension = buf.readInt();
		this.entityID = buf.readInt();
		this.name = ByteBufUtils.readUTF8String(buf); ;
		this.tileX = buf.readInt();
		this.tileY = buf.readInt();
		this.tileZ = buf.readInt();
	 }

	}
