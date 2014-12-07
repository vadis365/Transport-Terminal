package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class ContainerMessage implements IMessage {

	public int windowId, power;

	public ContainerMessage() {
	}

	public ContainerMessage(int windowId, int power) {
		this.windowId = windowId;
		this.power = power;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		windowId = buf.readInt();
		power = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(windowId);
		buf.writeInt(power);
	}
}