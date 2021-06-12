package com.wuest.prefab.Structures.Messages;

import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class StructureHandler {
	/**
	 * Initializes a new instance of the StructureHandler class.
	 */
	public StructureHandler() {
	}

	public static void handle(final StructureTagMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			// This is server side. Build the structure.
			EnumStructureConfiguration structureConfig = message.getStructureConfig();

			StructureConfiguration configuration = structureConfig.structureConfig.ReadFromCompoundNBT(message.getMessageTag());
			configuration.BuildStructure(context.getSender(), context.getSender().getLevel());
		});

		context.setPacketHandled(true);
	}

}
