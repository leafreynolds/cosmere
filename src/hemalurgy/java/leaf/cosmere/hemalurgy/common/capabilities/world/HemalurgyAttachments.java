/*
 * File created ~ 2026-04-26 ~ Leaf (NeoForge 1.21.1 port)
 */

package leaf.cosmere.hemalurgy.common.capabilities.world;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class HemalurgyAttachments
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Hemalurgy.MODID);

	public static final Supplier<AttachmentType<HemalurgyWorldCapability>> HEMALURGY_WORLD =
			ATTACHMENT_TYPES.register(
					"hemalurgy_world",
					() -> AttachmentType
							.serializable((IAttachmentHolder holder) -> new HemalurgyWorldCapability((Level) holder))
							.build());

	private HemalurgyAttachments()
	{
	}
}
