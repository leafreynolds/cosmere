package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.capabilities.world.ScadrialCapability;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AllomancyAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Allomancy.MODID);

	public static final Supplier<AttachmentType<ScadrialCapability>> SCADRIAL = ATTACHMENTS.register(
			"scadrial",
			() -> AttachmentType
					.serializable((IAttachmentHolder holder) -> new ScadrialCapability((Level) holder))
					.copyOnDeath()
					.build());
}
