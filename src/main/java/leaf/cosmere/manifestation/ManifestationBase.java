/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Suff and their mod Regeneration.
 * That mod taught me how to add ticking capabilities to entities and have them sync
 * https://github.com/WhoCraft/Regeneration
 *
 */

package leaf.cosmere.manifestation;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;

//Manifestation of investiture
public class ManifestationBase extends AManifestation
{
    final protected Manifestations.ManifestationTypes manifestationType;
    final protected int color;

    public ManifestationBase(Manifestations.ManifestationTypes manifestationType, int color)
    {
        this.manifestationType = manifestationType;
        this.color = color;
    }

    @Override
    public Manifestations.ManifestationTypes getManifestationType()
    {
        return manifestationType;
    }

    @Override
    public int getPowerID()
    {
        return 0;
    }

    @Override
    public void onModeChange(ISpiritweb data)
    {

    }

    @Override
    public int getMode(ISpiritweb data)
    {
        return data.getMode(manifestationType, getPowerID());
    }

    @Override
    public int modeMax(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return false;
    }

    @Override
    public void tick(ISpiritweb data)
    {

    }

    @Override
    public boolean isActive(ISpiritweb data)
    {
        return getMode(data) != 0;
    }
}
