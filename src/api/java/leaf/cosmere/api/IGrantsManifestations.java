package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public interface IGrantsManifestations
{
	public ArrayList<Manifestation> determineManifestations(ItemStack itemStack);

	public void grantManifestations(LivingEntity livingEntity, ArrayList<Manifestation> manifestations, int strength);
}
