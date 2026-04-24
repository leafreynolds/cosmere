/*
 * File created ~ 2026-04-25 ~ Leaf (NeoForge 1.21.1 port)
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class AllomancyAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Allomancy.MODID);

	public static final Supplier<AttachmentType<ScadrialCapability>> SCADRIAL =
			ATTACHMENT_TYPES.register(
					"scadrial",
					() -> AttachmentType
							.serializable((IAttachmentHolder holder) -> new ScadrialCapability((Level) holder))
							.build());

	private AllomancyAttachments()
	{
	}
}
