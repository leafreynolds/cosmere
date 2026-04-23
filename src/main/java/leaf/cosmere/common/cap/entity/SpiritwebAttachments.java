/*
 * File created ~ 2026-04-23 ~ Leaf (NeoForge 1.21.1 port)
 */

package leaf.cosmere.common.cap.entity;

import leaf.cosmere.common.Cosmere;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class SpiritwebAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Cosmere.MODID);

	public static final Supplier<AttachmentType<SpiritwebCapability>> SPIRITWEB =
			ATTACHMENT_TYPES.register(
					"spiritweb",
					() -> AttachmentType
							.serializable((IAttachmentHolder holder) -> new SpiritwebCapability((LivingEntity) holder))
							.build());

	private SpiritwebAttachments()
	{
	}
}
