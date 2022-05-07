/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.hwyla;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.utils.helpers.TextHelper;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.world.entity.LivingEntity;

public class SpiritWebTooltip implements IEntityComponentProvider
{
	static final SpiritWebTooltip INSTANCE = new SpiritWebTooltip();


	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig iPluginConfig)
	{
/*
        //todo add other ways we would allow the user to see spiritweb info
        if (!accessor.getPlayer().isCreative())
        {
            return;
        }*/

		//check the entity we are trying to
		SpiritwebCapability.get((LivingEntity) accessor.getEntity()).ifPresent(iSpiritweb ->
		{

			//todo check mistborn/feruchemist status
			boolean mistborn = false;//iSpiritweb.isMistborn();
			boolean fullFeruchemist = false;//iSpiritweb.isFullFeruchemist();
			if (mistborn)
			{
				tooltip.add(TextHelper.createTranslatedText(Metals.MetalType.LERASIUM.getMistingName()));
			}
			if (fullFeruchemist)
			{
				tooltip.add(TextHelper.createTranslatedText(Metals.MetalType.LERASATIUM.getFerringName()));
			}


			//show all manifestations, including hemalurgic based ones.
			for (AManifestation manifestation : iSpiritweb.getAvailableManifestations())
			{
				if ((mistborn && manifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY)
						|| (fullFeruchemist && manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY))
				{
					continue;
				}
				tooltip.add(manifestation.translation());
			}
		});
	}
}
