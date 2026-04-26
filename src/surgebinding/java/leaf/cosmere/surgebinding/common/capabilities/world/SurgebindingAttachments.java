package leaf.cosmere.surgebinding.common.capabilities.world;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static leaf.cosmere.surgebinding.common.Surgebinding.MODID;

public class SurgebindingAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

	public static final Supplier<AttachmentType<RosharCapability>> ROSHAR =
			ATTACHMENT_TYPES.register("roshar", () ->
					AttachmentType.serializable(holder -> new RosharCapability()).build());
}
