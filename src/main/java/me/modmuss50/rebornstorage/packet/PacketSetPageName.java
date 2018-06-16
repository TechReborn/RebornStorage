package me.modmuss50.rebornstorage.packet;

import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import me.modmuss50.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;


public class PacketSetPageName implements INetworkPacket<PacketSetPageName> {

	BlockPos blockPos;
	int pageID;
	String pageName;

	public PacketSetPageName(BlockPos blockPos, int pageID, String pageName) {
		this.blockPos = blockPos;
		this.pageID = pageID;
		this.pageName = pageName;
	}

	public PacketSetPageName() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) {
		buffer.writeBlockPos(blockPos);
		buffer.writeInt(pageID);
		buffer.writeInt(pageName.length());
		buffer.writeString(pageName);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) {
		blockPos = buffer.readBlockPos();
		pageID = buffer.readInt();
		pageName = buffer.readString(buffer.readInt());
	}

	@Override
	public void processData(PacketSetPageName message, MessageContext context) {
		context.getServerHandler().player.world.getMinecraftServer().addScheduledTask(() -> {
			World world = context.getServerHandler().player.world;
			if(!world.isBlockLoaded(blockPos)){
				return;
			}
			TileEntity tileEntity = world.getTileEntity(blockPos);
			if(!(tileEntity instanceof TileMultiCrafter)){
				return;
			}
			MultiBlockCrafter multiBlockCrafter = ((TileMultiCrafter) tileEntity).getMultiBlock();
			multiBlockCrafter.pageNameMap.remove(pageID);
			multiBlockCrafter.pageNameMap.put(pageID, pageName);
			tileEntity.markDirty();
			System.out.println("Set:" + multiBlockCrafter.pageNameMap.get(pageID) + "&" + pageID);

		});
	}
}
