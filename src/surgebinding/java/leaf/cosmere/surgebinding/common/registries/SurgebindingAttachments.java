/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.world.RosharCapability;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

//only Level/Entity/BlockEntity can hold attachments in 1.21. item data is on data components
public class SurgebindingAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Surgebinding.MODID);

	public static final Supplier<AttachmentType<RosharCapability>> ROSHAR = ATTACHMENTS.register(
			"roshar",
			() -> AttachmentType
					.serializable((IAttachmentHolder holder) -> new RosharCapability((Level) holder))
					.build());
}
