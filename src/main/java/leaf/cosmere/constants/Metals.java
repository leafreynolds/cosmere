/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.items.MetalIngotItem;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.curio.BraceletMetalmind;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.items.curio.NecklaceMetalmind;
import leaf.cosmere.items.curio.RingMetalmind;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.block.OreBlock;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/*
*   All hope abandon ye who enter here.
*            ,,
*          W ()
*          |-><  v
*          | )(\/
*   You've found the monolith metal script!
*   It ain't pretty, but I also don't really care :D
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
                case SILVER:
                case LEAD:
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
                case CHROMIUM:
                    //case IRON:
                case NICKEL:
                case COPPER:
                case ZINC:
                case SILVER:
                case TIN:
                    //case GOLD:
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

        public boolean hasMaterialItem()
        {
            switch (this)
            {
                case IRON:
                case GOLD:
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

        public NecklaceMetalmind getNecklaceItem()
        {
            return (NecklaceMetalmind) ItemsRegistry.METAL_NECKLACES.get(this).get();
        }

        public RingMetalmind getRingItem()
        {
            return (RingMetalmind) ItemsRegistry.METAL_RINGS.get(this).get();
        }

        public BraceletMetalmind getBraceletItem()
        {
            return (BraceletMetalmind) ItemsRegistry.METAL_BRACELETS.get(this).get();
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

        public MetalBlock getBlock()
        {
            return (MetalBlock) BlocksRegistry.METAL_BLOCKS.get(this).get();
        }

        public OreBlock getOreBlock()
        {
            return BlocksRegistry.METAL_ORE.get(this).get();
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
            Color color;

            switch (this)
            {
                default:
                case IRON:
                    color = MetalColor.IRON;
                    break;
                case STEEL:
                    color = MetalColor.STEEL;
                    break;
                case TIN:
                    color = MetalColor.TIN;
                    break;
                case PEWTER:
                    color = MetalColor.PEWTER;
                    break;
                case ALUMINUM:
                    color = MetalColor.ALUMINUM;
                    break;
                case DURALUMIN:
                    color = MetalColor.DURALUMIN;
                    break;
                case CHROMIUM:
                    color = MetalColor.CHROMIUM;
                    break;
                case NICROSIL:
                    color = MetalColor.NICROSIL;
                    break;
                case ZINC:
                    color = MetalColor.ZINC;
                    break;
                case BRASS:
                    color = MetalColor.BRASS;
                    break;
                case COPPER:
                    color = MetalColor.COPPER;
                    break;
                case BRONZE:
                    color = MetalColor.BRONZE;
                    break;
                case GOLD:
                    color = MetalColor.GOLD;
                    break;
                case ELECTRUM:
                    color = MetalColor.ELECTRUM;
                    break;
                case CADMIUM:
                    color = MetalColor.CADMIUM;
                    break;
                case BENDALLOY:
                    color = MetalColor.BENDALLOY;
                    break;
                case LERASIUM:
                    color = MetalColor.LERASIUM;
                    break;
                case ATIUM:
                    color = MetalColor.ATIUM;
                    break;
                case MALATIUM:
                    color = MetalColor.MALATIUM;
                    break;
                case LERASATIUM:
                    color = MetalColor.LERASATIUM;
                    break;
                case HARMONIUM:
                    color = MetalColor.HARMONIUM;
                    break;
                case NICKEL:
                    color = MetalColor.NICKEL;
                    break;
                case LEAD:
                    color = MetalColor.LEAD;
                    break;
                case SILVER:
                    color = MetalColor.SILVER;
                    break;
            }

            return color;
        }

        public int getColorValue()
        {
            return getColor().getRGB();
        }

        public String getFerringName()
        {
            String name;

            switch (this)
            {
                case IRON:
                    name = FerringNames.IRON;
                    break;
                case STEEL:
                    name = FerringNames.STEEL;
                    break;
                case TIN:
                    name = FerringNames.TIN;
                    break;
                case PEWTER:
                    name = FerringNames.PEWTER;
                    break;
                case ALUMINUM:
                    name = FerringNames.ALUMINUM;
                    break;
                case DURALUMIN:
                    name = FerringNames.DURALUMIN;
                    break;
                case CHROMIUM:
                    name = FerringNames.CHROMIUM;
                    break;
                case NICROSIL:
                    name = FerringNames.NICROSIL;
                    break;
                case ZINC:
                    name = FerringNames.ZINC;
                    break;
                case BRASS:
                    name = FerringNames.BRASS;
                    break;
                case COPPER:
                    name = FerringNames.COPPER;
                    break;
                case BRONZE:
                    name = FerringNames.BRONZE;
                    break;
                case GOLD:
                    name = FerringNames.GOLD;
                    break;
                case ELECTRUM:
                    name = FerringNames.ELECTRUM;
                    break;
                case CADMIUM:
                    name = FerringNames.CADMIUM;
                    break;
                case BENDALLOY:
                    name = FerringNames.BENDALLOY;
                    break;
                case ATIUM:
                    name = FerringNames.ATIUM;
                    break;
                case LERASATIUM:
                    name = FerringNames.ALL;
                    break;
                default:
                case LERASIUM:
                case MALATIUM:
                    name = "ferring_" + this.name().toLowerCase();
                    break;
            }


            return name;
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
            String name;

            switch (this)
            {
                case IRON:
                    name = MistingNames.IRON;
                    break;
                case STEEL:
                    name = MistingNames.STEEL;
                    break;
                case TIN:
                    name = MistingNames.TIN;
                    break;
                case PEWTER:
                    name = MistingNames.PEWTER;
                    break;
                case ALUMINUM:
                    name = MistingNames.ALUMINUM;
                    break;
                case DURALUMIN:
                    name = MistingNames.DURALUMIN;
                    break;
                case CHROMIUM:
                    name = MistingNames.CHROMIUM;
                    break;
                case NICROSIL:
                    name = MistingNames.NICROSIL;
                    break;
                case ZINC:
                    name = MistingNames.ZINC;
                    break;
                case BRASS:
                    name = MistingNames.BRASS;
                    break;
                case COPPER:
                    name = MistingNames.COPPER;
                    break;
                case BRONZE:
                    name = MistingNames.BRONZE;
                    break;
                case GOLD:
                    name = MistingNames.GOLD;
                    break;
                case ELECTRUM:
                    name = MistingNames.ELECTRUM;
                    break;
                case CADMIUM:
                    name = MistingNames.CADMIUM;
                    break;
                case BENDALLOY:
                    name = MistingNames.BENDALLOY;
                    break;
                case ATIUM:
                    name = MistingNames.ATIUM;
                    break;
                case LERASIUM:
                    name = MistingNames.ALL;
                    break;
                default:
                case MALATIUM:
                case LERASATIUM:
                    name = "misting_" + this.name().toLowerCase();
                    break;
            }

            return name;
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
                    return Arrays.asList(Metals.MetalType.values());
                case LERASIUM:
                case MALATIUM:
                case LERASATIUM:
                    break;
            }

            return null;
        }

        //todo implement item tiers

        @Override
        public int getMaxUses()
        {
            return 0;
        }

        @Override
        public float getEfficiency()
        {
            return 0;
        }

        @Override
        public float getAttackDamage()
        {
            return 0;
        }

        @Override
        public int getHarvestLevel()
        {
            return 0;
        }

        @Override
        public int getEnchantability()
        {
            return 0;
        }

        @Override
        public Ingredient getRepairMaterial()
        {
            return null;
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
        public static final String TIN = "windwhisper";
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
