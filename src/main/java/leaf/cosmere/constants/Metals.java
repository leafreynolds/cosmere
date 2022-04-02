/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.items.MetalIngotItem;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.MetalRawOreItem;
import leaf.cosmere.items.curio.BraceletMetalmindItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.items.curio.NecklaceMetalmindItem;
import leaf.cosmere.items.curio.RingMetalmindItem;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 *   All hope abandon ye who enter here.
 *            ,,
 *          W ()
 *          |-><  v
 *          | )(\/
 *   You've found the monolith metal script!
 *   It ain't pretty, but I also don't really care :D -Leaf
 */

public class Metals
{

    public enum MetalType implements IItemTier
    {
        //Physical/Physical
        IRON(0),
        STEEL(1),//alloy of iron and carbon (coal/charcoal) 1/4 ?
        TIN(2),
        PEWTER(3),//alloy of tin and lead 4/1

        //Mental/Cognitive
        ZINC(4),
        BRASS(5),//allot of zinc and copper 50/50 ?
        COPPER(6),
        BRONZE(7),//alloy of copper and tin? 3/1 ?

        //Enhancement/spiritual
        ALUMINUM(8),
        DURALUMIN(9),//alloy of aluminum and copper 4/1
        CHROMIUM(10),
        NICROSIL(11),//alloy of chromium and nickel 1/3

        //temporal/hybrid
        CADMIUM(12),
        BENDALLOY(13),//alloy of 70% lead, 20% tin, and 10% cadmium by mass
        GOLD(14),
        ELECTRUM(15),//alloy of gold and silver 1/1

        //god metals
        ATIUM(16),
        LERASIUM(17),
        HARMONIUM(18),

        //god metal alloys
        MALATIUM(19),//atium and gold?
        LERASATIUM(20),// atium and lerasium


        //non-allomantic metals

        NICKEL(21),
        LEAD(22),
        SILVER(23);

        private final int id;

        MetalType(int id)
        {
            this.id = id;
        }

        public static Optional<MetalType> valueOf(int value)
        {
            return Arrays.stream(values())
                    .filter(metalType -> metalType.id == value)
                    .findFirst();
        }

        public int getID()
        {
            return id;
        }

        public String getName(){ return name().toLowerCase(Locale.ROOT);}

        public Rarity getRarity()
        {
            switch (this)
            {
                case ATIUM:
                case MALATIUM:
                    return Rarity.RARE;
                case LERASIUM:
                case LERASATIUM:
                case HARMONIUM:
                    return Rarity.EPIC;
                default:
                    return Rarity.COMMON;
            }
        }

        public boolean hasAssociatedManifestation()
        {
            switch (this)
            {
                case MALATIUM:
                case LERASIUM: //these are mistborn, but its handled by giving access to all other metals
                case LERASATIUM: //these are feruchemists, but its handled by giving access to all other metals
                case HARMONIUM: //different way of melding preservation and ruin. No idea what it does
                case NICKEL:
                case SILVER:
                case LEAD:
                    return false;
                default:
                    return true;
            }
        }

        public boolean hasHemalurgicEffect()
        {
            switch (this)
            {
                case MALATIUM:
                case LERASATIUM:
                case HARMONIUM:
                case NICKEL:
                case LEAD:
                case SILVER:
                    return false;
                default:
                    return true;
            }
        }

        public boolean hasFeruchemicalEffect()
        {
            switch (this)
            {
                case LERASIUM://no idea what lerasium would do
                case MALATIUM:
                case LERASATIUM:
                case HARMONIUM:
                case NICKEL:
                case SILVER:
                case LEAD:
                    return false;
                default:
                    return true;
            }
        }

        public boolean hasOre()
        {
            switch (this)
            {
                case ALUMINUM:
                case CADMIUM:
                case CHROMIUM:
                    //case IRON: // covered by minecraft
                case NICKEL:
                case COPPER:
                case ZINC:
                case SILVER:
                case TIN:
                    //case GOLD: // covered by minecraft
                case LEAD:
                    return true;
                default:
                    return false;
            }
        }

        public boolean isAlloy()
        {
            switch (this)
            {
                case STEEL:
                case PEWTER:
                case BRASS:
                case BRONZE:
                case DURALUMIN:
                case NICROSIL:
                case BENDALLOY:
                case ELECTRUM:
                    return true;
                default:
                    return false;
            }
        }

        //Used for metals that exist in the base minecraft
        //todo add copper in 1.18
        public boolean hasMaterialItem()
        {
            switch (this)
            {
                case IRON:
                case GOLD:
                //case COPPER:
                    return false;
                default:
                    return true;
            }
        }

        //All the helper functions.

        public Effect getStoringEffect()
        {
            return EffectsRegistry.STORING_EFFECTS.get(this).get();
        }

        public Effect getTappingEffect()
        {
            return EffectsRegistry.TAPPING_EFFECTS.get(this).get();
        }

        public NecklaceMetalmindItem getNecklaceItem()
        {
            return (NecklaceMetalmindItem) ItemsRegistry.METAL_NECKLACES.get(this).get();
        }

        public RingMetalmindItem getRingItem()
        {
            return (RingMetalmindItem) ItemsRegistry.METAL_RINGS.get(this).get();
        }

        public BraceletMetalmindItem getBraceletItem()
        {
            return (BraceletMetalmindItem) ItemsRegistry.METAL_BRACELETS.get(this).get();
        }

        public MetalIngotItem getIngotItem()
        {
            return (MetalIngotItem) ItemsRegistry.METAL_INGOTS.get(this).get();
        }

        public MetalNuggetItem getNuggetItem()
        {
            return (MetalNuggetItem) ItemsRegistry.METAL_NUGGETS.get(this).get();
        }

        public HemalurgicSpikeItem getSpikeItem()
        {
            return (HemalurgicSpikeItem) ItemsRegistry.METAL_SPIKE.get(this).get();
        }

        public MetalRawOreItem getRawMetalItem()
        {
            if (this.isAlloy())
            {
                return (MetalRawOreItem) ItemsRegistry.METAL_RAW_BLEND.get(this).get();
            }
            // if (this.hasOre())
            else
            {
                return (MetalRawOreItem) ItemsRegistry.METAL_RAW_ORE.get(this).get();
            }

        }

        public MetalBlock getBlock()
        {
            return BlocksRegistry.METAL_BLOCKS.get(this).get();
        }

        public OreBlock getOreBlock()
        {
            return BlocksRegistry.METAL_ORE.get(this).get();
        }

        public ITag.INamedTag<Item> getMetalRawTag()
        {
            return TagsRegistry.Items.METAL_RAW_TAGS.get(this);
        }

        public ITag.INamedTag<Item> getMetalIngotTag()
        {
            return TagsRegistry.Items.METAL_INGOT_TAGS.get(this);
        }

        public ITag.INamedTag<Item> getMetalNuggetTag()
        {
            return TagsRegistry.Items.METAL_NUGGET_TAGS.get(this);
        }


        public Color getColor()
        {
            switch (this)
            {
                default:
                case IRON:
                    return MetalColor.IRON;
                case STEEL:
                    return MetalColor.STEEL;
                case TIN:
                    return MetalColor.TIN;
                case PEWTER:
                    return MetalColor.PEWTER;
                case ALUMINUM:
                    return MetalColor.ALUMINUM;
                case DURALUMIN:
                    return MetalColor.DURALUMIN;
                case CHROMIUM:
                    return MetalColor.CHROMIUM;
                case NICROSIL:
                    return MetalColor.NICROSIL;
                case ZINC:
                    return MetalColor.ZINC;
                case BRASS:
                    return MetalColor.BRASS;
                case COPPER:
                    return MetalColor.COPPER;
                case BRONZE:
                    return MetalColor.BRONZE;
                case GOLD:
                    return MetalColor.GOLD;
                case ELECTRUM:
                    return MetalColor.ELECTRUM;
                case CADMIUM:
                    return MetalColor.CADMIUM;
                case BENDALLOY:
                    return MetalColor.BENDALLOY;
                case LERASIUM:
                    return MetalColor.LERASIUM;
                case ATIUM:
                    return MetalColor.ATIUM;
                case MALATIUM:
                    return MetalColor.MALATIUM;
                case LERASATIUM:
                    return MetalColor.LERASATIUM;
                case HARMONIUM:
                    return MetalColor.HARMONIUM;
                case NICKEL:
                    return MetalColor.NICKEL;
                case LEAD:
                    return MetalColor.LEAD;
                case SILVER:
                    return MetalColor.SILVER;
            }
        }

        public int getColorValue()
        {
            return getColor().getRGB();
        }

        public String getFerringName()
        {
            switch (this)
            {
                case IRON:
                    return FerringNames.IRON;
                case STEEL:
                    return FerringNames.STEEL;
                case TIN:
                    return FerringNames.TIN;
                case PEWTER:
                    return FerringNames.PEWTER;
                case ALUMINUM:
                    return FerringNames.ALUMINUM;
                case DURALUMIN:
                    return FerringNames.DURALUMIN;
                case CHROMIUM:
                    return FerringNames.CHROMIUM;
                case NICROSIL:
                    return FerringNames.NICROSIL;
                case ZINC:
                    return FerringNames.ZINC;
                case BRASS:
                    return FerringNames.BRASS;
                case COPPER:
                    return FerringNames.COPPER;
                case BRONZE:
                    return FerringNames.BRONZE;
                case GOLD:
                    return FerringNames.GOLD;
                case ELECTRUM:
                    return FerringNames.ELECTRUM;
                case CADMIUM:
                    return FerringNames.CADMIUM;
                case BENDALLOY:
                    return FerringNames.BENDALLOY;
                case ATIUM:
                    return FerringNames.ATIUM;
                case LERASATIUM:
                    return FerringNames.ALL;
                default:
                case LERASIUM:
                case MALATIUM:
                    return "ferring_" + this.getName();
            }
        }

        public boolean isPushMetal()
        {
            switch (this)
            {
                case STEEL:
                case PEWTER:
                case BRASS:
                case BRONZE:
                case DURALUMIN:
                case NICROSIL:
                case BENDALLOY:
                case ELECTRUM:
                    return true;
                default:
                    return false;
            }
        }

        public boolean isPullMetal()
        {
            switch (this)
            {
                case IRON:
                case TIN:
                case ALUMINUM:
                case CHROMIUM:
                case ZINC:
                case COPPER:
                case GOLD:
                case CADMIUM:
                    return true;
                default:
                    return false;
            }
        }

        public String getMistingName()
        {
            switch (this)
            {
                case IRON:
                    return MistingNames.IRON;

                case STEEL:
                    return MistingNames.STEEL;

                case TIN:
                    return MistingNames.TIN;

                case PEWTER:
                    return MistingNames.PEWTER;

                case ALUMINUM:
                    return MistingNames.ALUMINUM;

                case DURALUMIN:
                    return MistingNames.DURALUMIN;

                case CHROMIUM:
                    return MistingNames.CHROMIUM;

                case NICROSIL:
                    return MistingNames.NICROSIL;

                case ZINC:
                    return MistingNames.ZINC;

                case BRASS:
                    return MistingNames.BRASS;

                case COPPER:
                    return MistingNames.COPPER;

                case BRONZE:
                    return MistingNames.BRONZE;

                case GOLD:
                    return MistingNames.GOLD;

                case ELECTRUM:
                    return MistingNames.ELECTRUM;

                case CADMIUM:
                    return MistingNames.CADMIUM;

                case BENDALLOY:
                    return MistingNames.BENDALLOY;

                case ATIUM:
                    return MistingNames.ATIUM;

                case LERASIUM:
                    return MistingNames.ALL;

                default:
                case MALATIUM:
                case LERASATIUM:
                    return "misting_" + this.getName();

            }
        }

        public Collection<MetalType> getHemalurgyStealWhitelist()
        {
            switch (this)
            {
                case STEEL:
                    //Steals a physical allomantic power
                    //Iron//Steel//Tin//Pewter
                case PEWTER:
                    //Steals a physical feruchemical power
                    //Iron//Steel//Tin//Pewter
                    return Arrays.asList(
                            Metals.MetalType.IRON,
                            Metals.MetalType.STEEL,
                            Metals.MetalType.TIN,
                            Metals.MetalType.PEWTER);
                case BRASS:
                    //Steals a cognitive feruchemical power
                    //Zinc//Brass//Copper//Bronze
                case BRONZE:
                    //Steals a Mental Allomantic power
                    //Zinc//Brass//Copper//Bronze
                    return Arrays.asList(
                            Metals.MetalType.ZINC,
                            Metals.MetalType.BRASS,
                            Metals.MetalType.COPPER,
                            Metals.MetalType.BRONZE);
                case CADMIUM:
                    //Steals a Temporal Allomantic power
                    //Cadmium//Bendalloy//Gold//Electrum
                case GOLD:
                    //Steals a Hybrid Feruchemical power
                    //Cadmium//Bendalloy//Gold//Electrum
                    return Arrays.asList(
                            Metals.MetalType.CADMIUM,
                            Metals.MetalType.BENDALLOY,
                            Metals.MetalType.GOLD,
                            Metals.MetalType.ELECTRUM);
                case BENDALLOY:
                    //Steals a Spiritual Feruchemical power
                    //Chromium//Nicrosil//Aluminum//Duralumin
                case ELECTRUM:
                    //Steals an Enhancement Allomantic power
                    //Chromium//Nicrosil//Aluminum//Duralumin
                    return Arrays.asList(
                            Metals.MetalType.CHROMIUM,
                            Metals.MetalType.NICROSIL,
                            Metals.MetalType.ALUMINUM,
                            Metals.MetalType.DURALUMIN);
                case ATIUM:
                case LERASIUM:
                    return Arrays.stream(MetalType.values()).filter(MetalType::hasAssociatedManifestation).collect(Collectors.toList());
                case MALATIUM:
                case LERASATIUM:
                    break;
            }

            return null;
        }

        public int getAllomancyBurnTimeSeconds()
        {
            //todo decide burn rate of metals
            //todo convert to config item
            int burnTimeInSeconds = 99;
            switch (this)
            {
                case IRON:
                    break;
                case STEEL:
                    break;
                case TIN:
                    break;
                case PEWTER:
                    break;
                case ZINC:
                    break;
                case BRASS:
                    break;
                case COPPER:
                    break;
                case BRONZE:
                    break;
                case ALUMINUM:
                    break;
                case DURALUMIN:
                    break;
                case CHROMIUM:
                    break;
                case NICROSIL:
                    break;
                case CADMIUM:
                    break;
                case BENDALLOY:
                    break;
                case GOLD:
                    break;
                case ELECTRUM:
                    break;
                case ATIUM:
                    break;
                case LERASIUM:
                    break;
                case HARMONIUM:
                    break;
                case MALATIUM:
                    break;
                case LERASATIUM:
                    break;
                case NICKEL:
                    break;
                case LEAD:
                    break;
                case SILVER:
                    break;
            }
            return burnTimeInSeconds;
        }

        public double getEntityAbilityStrength(LivingEntity killedEntity)
        {
            //Steals non-manifestation based abilities. traits inherent to an entity?
            double strengthToAdd = 0;
            switch (this)
            {
                case IRON:
                    //steals physical strength
                    //don't steal modified values, only base value
                    //todo decide how much strength is reasonable to steal and how much goes to waste
                    //currently will try 70%
                    strengthToAdd = killedEntity.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) * 0.7D;

                    break;
                case TIN:
                    //Steals senses

                    //can track you on a huge island, even underground.
                    if (killedEntity instanceof EnderDragonEntity)
                    {
                        strengthToAdd = 0.77;
                    }
                    //Literally hunt players at night
                    else if (killedEntity instanceof PhantomEntity)
                    {
                        strengthToAdd = 0.66;
                    }
                    else if (killedEntity instanceof PlayerEntity)
                    {
                        strengthToAdd = 0.25;
                    }
                    else
                    {
                        final ModifiableAttributeInstance attribute = killedEntity.getAttribute(Attributes.FOLLOW_RANGE);

                        if (attribute != null)
                        {
                            //ghasts have 100 base follow range,
                            //most others have 16-40
                            final int hemalurgicDecay = 150;
                            strengthToAdd = attribute.getBaseValue() / hemalurgicDecay;
                        }
                        else
                        {
                            //at this point, who cares.
                            strengthToAdd =  0.10;
                        }
                    }
                    break;
                case COPPER:
                    //Steals mental fortitude, memory, and intelligence
                    //todo increase base xp gain?
                    break;
                case ZINC:
                    //Steals emotional fortitude
                    //todo figure out what that means
                    break;
                case ALUMINUM:
                    //Removes all powers
                    //... ooops?
                    //maybe its an item you can equip on others that they then have to remove?
                    break;
                case DURALUMIN:
                    //Steals Connection/Identity
                    break;
                case CHROMIUM:
                    //Might steal destiny
                    //so we could add some permanent luck?
                    break;
                case NICROSIL:
                    //Steals Investiture
                    break;
            }
            return strengthToAdd;
        }

        public String getHemalurgicUseString()
        {
            switch (this)
            {
                case IRON:
                    return "Steals physical strength.";
                case STEEL:
                    return "Steals a physical allomantic power.";
                //Iron//Steel//Tin//Pewter
                case TIN:
                    return "Steals senses.";
                case PEWTER:
                    return "Steals a physical feruchemical power.";
                //Iron//Steel//Tin//Pewter
                case ZINC:
                    return "Steals emotional fortitude.";
                case BRASS:
                    return "Steals a cognitive feruchemical power.";
                //Zinc//Brass//Copper//Bronze
                case COPPER:
                    return "Steals mental fortitude, memory, and intelligence.";
                case BRONZE:
                    return "Steals a Mental Allomantic power.";
                //Zinc//Brass//Copper//Bronze
                case ALUMINUM:
                    return "Removes all powers.";
                case DURALUMIN:
                    return "Steals Connection/Identity.";
                case CHROMIUM:
                    return "Might steal destiny...";
                case NICROSIL:
                    return "Steals Investiture.";
                case CADMIUM:
                    return "Steals a Temporal Allomantic power.";
                //Cadmium//Bendalloy//Gold//Electrum
                case GOLD:
                    return "Steals a Hybrid Feruchemical power.";
                //Cadmium//Bendalloy//Gold//Electrum
                case BENDALLOY:
                    return "Steals a Spiritual Feruchemical power.";
                //Chromium//Nicrosil//Aluminum//Duralumin
                case ELECTRUM:
                    return "Steals an Enhancement Allomantic power.";
                //Chromium//Nicrosil//Aluminum//Duralumin
                case ATIUM:
                    return "Steals an Allomantic or Feruchemical power. Must be refined.";
                case LERASIUM:
                    return "Steals all powers...";
            }

            return "Unknown...";
        }

        public String GetAllomancyDescription()
        {
            switch (this)
            {
                case IRON:
                    return "Iron is an Allomantic metal that, when burned, allows the user to Pull on various metal objects around them, pulling any of these objects towards them." ;//+
                            //"The burning of Iron is affected by normal laws of physics, so when the user pulls upon something with more weight, it will take more iron to move this, and if the user pulls upon something with more weight than themselves, they will be pulled through the air towards the aforementioned object. " +
                            //"The burning of Iron is used to fly, manipulate various objects, pull someone towards you (assuming that they have metal on their person), disarm somebody with a metal weapon, change the flight path of flying coins, and for a multitude of other things.";

                case STEEL:
                    return "Steel is an Allomantic metal and an alloy of Iron. When burnt, Steel allows the user to telekinetically push against a metal object." ;//+
                            //"Conservation of momentum still applies here, so if you trying to push against blocks, expect to be thrown back in the opposite direction." +
                            //"Clever Allomancers use steel to allow themselves to fly through the air.";

                case TIN:
                    return "In allomancy, tin heightens the senses to super human levels. In this world, burning tin allows the allomancer to see clearly in the dark and detect where sounds are coming from.";

                case PEWTER:
                    return "Pewter is an allomantic metal that, when burnt, gives the user extreme strength, resistance and durability. Its uses include combat, moving quickly, surviving attacks and healing oneself." ;//+
                            //"The major problem with pewter is that when it runs out, a large portion of the pain and injury that you resisted using the pewter hits you at once, potentially resulting in death. $(#f00)(NYI)$()";

                case ZINC:
                    return "In allomancy, zinc gives the ability to intensify the emotions of others, an ability called \"rioting\"";

                case BRASS:
                    return "In allomancy, brass gives the ability to \"soothe\" the emotions of others.";

                case COPPER:
                    return "Copper is an elemental Allomantic metal that allows one to hide Allomancy. A misting or mistborn burning copper generates a 'coppercloud' which hides Allomancy from being detected by Bronze. " ;//+
                            //"Copperclouds are generally not piercible but those with exceptional strength in Bronze may do so.";

                case BRONZE:
                    return "When burning bronze, either Misting or Mistborn, the user can feel the uses of allomancy near them.";//+
                            //"Copper neutralises the ability for a Seeker to track allomancy by hiding it in a copper cloud, but extremely powerful Seekers or Mistborn, may still be able to pierce said shields.";

                case ALUMINUM:
                    return "Aluminum is an internal enhancement metal that, when burned, clears out all the metals inside the allomancer, including itself.";

                case DURALUMIN:
                    return "An allomancer that burns duralumin causes an amazing burst of power from all currently burning metals, draining all of them rapidly.";

                case CHROMIUM:
                    return "Chromium allows the burner to, with physical contact, deplete the metals in another with an effect similar to that of $(l:cosmere.entry.allomantic_aluminum).";

                case NICROSIL:
                    return "It allows the burner to, with physical contact with another allomancer, have an effect on them as if they were burning $(l:cosmere.entry.allomantic_duralumin) themselves.";

                case CADMIUM:
                    return "Cadmium allows the burner to pull on time in a bubble around them, making time pass slowly within the bubble.";

                case BENDALLOY:
                    return "Bendalloy allows the burner to push on time in a bubble around them, making time pass quickly within the bubble. Expect to see your furnaces finish more quickly or crops grow faster!";

                case GOLD:
                    return "$(#f00)(NYI)$()";

                case ELECTRUM:
                    return "$(#f00)(NYI)$()";

            }
            return "";
        }


        public String getFeruchemyMetalmindUse()
        {
            switch (this)
            {
                case IRON:
                    return "Stores Weight. Less weight slows descent. A Skimmer Ferring using this will decrease the pull of gravity on them in exchange for increasing it later.";
                case STEEL:
                    return "Stores Physical Speed. A Steelrunner Ferring using this will be physically slower now in exchange for being faster later.";
                case TIN:
                    return "Stores Eyesight senses. Tapping will zoom in.";
                case PEWTER:
                    return "Stores Strength. A Brute Ferring using this will lessen the size of his muscles to increase them later.";
                case ZINC:
                    return "Stores Mental Speed. A Sparker Ferring storing zinc will gain experience very slowly in exchange for gaining experience faster later.";
                case BRASS:
                    return "Stores Warmth. Firesoul Ferrings using this will cool themselves in exchange for being able to warm themselves later by tapping the metalmind.";
                case COPPER:
                    return "Stores Experience. An Archivist Ferring using this will be able to store experience inside copper, then withdraw it later from the metal.";
                case BRONZE:
                    return "Stores Wakefulness. A Sentry Ferring using this will sleep or be drowsier now in exchange for staying awake longer later.";
                case ALUMINUM:
                    return "Stores Identity. Trueself Ferrings can store their spiritual sense of self within an aluminum metalmind.";
                case DURALUMIN:
                    return "Stores Connection. A Connector Ferring can store spiritual connection inside a metalmind, reducing outside awareness during active storage. Tapping not yet implemented.";//todo change when tapping works
                case CHROMIUM:
                    return "Stores Fortune. A Spinner Ferring will become unlucky during active storage in exchange for increased fortune later.";
                case NICROSIL:
                    return "Stores Investiture. Little is known about Soulbearer Ferrings. (Not yet implemented)";
                case CADMIUM:
                    return "Stores Breath. A Gasper Ferring may hyperventilate while storing breath in exchange for eliminating or reducing the need to breathe later on.";
                case GOLD:
                    return "Stores Health. A Bloodmaker Ferring using this will feel sick now in exchange for increased regeneration and healing later.";
                case ELECTRUM:
                    return "Stores Determination. A Pinnacle Ferring using this will take more damage in exchange for taking less damage later.";
                case BENDALLOY:
                    return "Stores Energy. A Subsumer Ferring using this can consume large quantities of food and store the calories in the metalmind, in exchange for the ability to forgo eating later.";
                case ATIUM:
                    return "Not yet implemented.";
            }

            return "Unknown...";
        }

        //todo implement item tiers

        @Override
        public int getUses()
        {
            return 0;
        }

        @Override
        public float getSpeed()
        {
            return 0;
        }

        @Override
        public float getAttackDamageBonus()
        {
            return 0;
        }

        @Override
        public int getLevel()
        {
            return 0;
        }

        @Override
        public int getEnchantmentValue()
        {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return Ingredient.of(getMetalIngotTag());
        }

    }

    private static class MetalColor
    {

        //Iron(0.560f,0.579f,0.580f)
        public static final Color IRON = Color.decode("#f2f2f2");
        public static final Color STEEL = Color.decode("#727273");
        public static final Color TIN = Color.decode("#79b7bc");
        public static final Color PEWTER = Color.decode("#a39b90");
        //Aluminium(0.913f,0.921f,0.925f)
        public static final Color ALUMINUM = Color.decode("#d9d9d9");
        public static final Color DURALUMIN = Color.decode("#a6a486");
        //Chromium(0.550f,0.556f,0.554f)
        public static final Color CHROMIUM = Color.decode("#595959");
        public static final Color NICROSIL = Color.decode("#59503e");
        public static final Color ZINC = Color.decode("#d7d9c7");
        public static final Color BRASS = Color.decode("#bc6330");
        //Copper(0.955f,0.637f,0.538f)
        public static final Color COPPER = Color.decode("#fba634");
        public static final Color BRONZE = Color.decode("#8c5b30");
        //Gold(1.000f,0.766f,0.336f)
        public static final Color GOLD = Color.decode("#faf25e");
        public static final Color ELECTRUM = Color.decode("#d9b166");
        public static final Color CADMIUM = Color.decode("#F5926C");
        public static final Color BENDALLOY = Color.decode("#46473e");

        //new Color(0.860f, 0.870f, 0.880f);
        public static final Color LERASIUM = Color.decode("#f2dea0");
        public static final Color ATIUM = Color.decode("#262626");

        //godmetal/metal alloys
        public static final Color MALATIUM = Color.decode("#bfbfbf");


        //god metal only alloys
        public static final Color HARMONIUM = Color.decode("#88b9d9");

        // made up
        public static final Color LERASATIUM = new Color(0.560f, 0.570f, 0.580f);

        //non-special
        public static final Color NICKEL = Color.decode("#eeedcb");
        public static final Color LEAD = Color.decode("#8392c2");
        public static final Color SILVER = Color.decode("#d3e5eb");
    }

    private static class FerringNames
    {
        public static final String ALL = "feruchemist";
        public static final String IRON = "skimmer";
        public static final String STEEL = "steelrunner";
        public static final String TIN = "windwhisperer";
        public static final String PEWTER = "brute";
        public static final String ALUMINUM = "trueself";
        public static final String DURALUMIN = "connector";
        public static final String CHROMIUM = "spinner";
        public static final String NICROSIL = "soulbearer";
        public static final String ZINC = "sparker";
        public static final String BRASS = "firesoul";
        public static final String COPPER = "archivist";
        public static final String BRONZE = "sentry";
        public static final String GOLD = "bloodmaker";
        public static final String ELECTRUM = "pinnacle";
        public static final String CADMIUM = "gasper";
        public static final String BENDALLOY = "subsumer";
        public static final String ATIUM = "elderling";
    }

    private static class MistingNames
    {
        public static final String ALL = "mistborn";
        public static final String IRON = "lurcher";
        public static final String STEEL = "coinshot";
        public static final String TIN = "tineye";
        public static final String PEWTER = "thug";
        public static final String ALUMINUM = "aluminum_gnat";
        public static final String DURALUMIN = "duralumin_gnat";
        public static final String CHROMIUM = "leecher";
        public static final String NICROSIL = "nicroburst";
        public static final String ZINC = "rioter";
        public static final String BRASS = "soother";
        public static final String COPPER = "smoker";
        public static final String BRONZE = "seeker";
        public static final String GOLD = "augur";
        public static final String ELECTRUM = "oracle";
        public static final String CADMIUM = "pulser";
        public static final String BENDALLOY = "slider";
        public static final String ATIUM = "seer";

    }
}
