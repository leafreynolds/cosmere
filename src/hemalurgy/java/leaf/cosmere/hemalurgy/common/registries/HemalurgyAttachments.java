package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.world.HemalurgyWorldCapability;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class HemalurgyAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Hemalurgy.MODID);

	public static final Supplier<AttachmentType<HemalurgyWorldCapability>> WORLD_CAP = ATTACHMENTS.register(
			"world_cap",
			() -> AttachmentType
					.serializable((IAttachmentHolder holder) -> new HemalurgyWorldCapability((Level) holder))
					.build());
}
