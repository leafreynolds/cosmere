package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CosmereAttachmentsRegistry
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Cosmere.MODID);

	public static final Supplier<AttachmentType<SpiritwebCapability>> SPIRITWEB = ATTACHMENTS.register(
			"spiritweb",
			() -> AttachmentType
					.serializable((IAttachmentHolder holder) -> new SpiritwebCapability((LivingEntity) holder))
					.copyOnDeath()
					.build());
}
