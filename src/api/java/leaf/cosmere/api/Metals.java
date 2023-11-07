/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.api;

import leaf.cosmere.api.helpers.TimeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

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

	public enum MetalType implements Tier
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

		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
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
				//case ATIUM:
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
					//case COPPER: // covered by minecraft
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

		public TagKey<Item> getMetalRawTag()
		{
			return CosmereTags.Items.METAL_RAW_TAGS.get(this);
		}

		public TagKey<Item> getMetalBlendTag()
		{
			return CosmereTags.Items.METAL_DUST_TAGS.get(this);
		}

		public TagKey<Item> getMetalIngotTag()
		{
			return CosmereTags.Items.METAL_INGOT_TAGS.get(this);
		}

		public TagKey<Item> getMetalNuggetTag()
		{
			return CosmereTags.Items.METAL_NUGGET_TAGS.get(this);
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
							MetalType.IRON,
							MetalType.STEEL,
							MetalType.TIN,
							MetalType.PEWTER);
				case BRASS:
					//Steals a cognitive feruchemical power
					//Zinc//Brass//Copper//Bronze
				case BRONZE:
					//Steals a Mental Allomantic power
					//Zinc//Brass//Copper//Bronze
					return Arrays.asList(
							MetalType.ZINC,
							MetalType.BRASS,
							MetalType.COPPER,
							MetalType.BRONZE);
				case CADMIUM:
					//Steals a Temporal Allomantic power
					//Cadmium//Bendalloy//Gold//Electrum
				case GOLD:
					//Steals a Hybrid Feruchemical power
					//Cadmium//Bendalloy//Gold//Electrum
					return Arrays.asList(
							MetalType.CADMIUM,
							MetalType.BENDALLOY,
							MetalType.GOLD,
							MetalType.ELECTRUM);
				case BENDALLOY:
					//Steals a Spiritual Feruchemical power
					//Chromium//Nicrosil//Aluminum//Duralumin
				case ELECTRUM:
					//Steals an Enhancement Allomantic power
					//Chromium//Nicrosil//Aluminum//Duralumin
					return Arrays.asList(
							MetalType.CHROMIUM,
							MetalType.NICROSIL,
							MetalType.ALUMINUM,
							MetalType.DURALUMIN);
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
			//todo convert to config item
			double burnTimeInSeconds = switch (this)
					{
						case IRON, STEEL -> TimeHelper.MinutesToSeconds(5);
						case TIN -> TimeHelper.MinutesToSeconds(15);
						case PEWTER -> TimeHelper.MinutesToSeconds(2.5);
						case ZINC, BRASS -> TimeHelper.MinutesToSeconds(5);
						case COPPER -> TimeHelper.MinutesToSeconds(10);
						case BRONZE -> TimeHelper.MinutesToSeconds(7.5);
						case ALUMINUM, DURALUMIN, CHROMIUM, NICROSIL ->
								TimeHelper.MinutesToSeconds(0.5);//these have special rules that make it burn out fast
						case CADMIUM -> TimeHelper.MinutesToSeconds(7.5);
						case BENDALLOY -> TimeHelper.MinutesToSeconds(2.5);
						case GOLD, ELECTRUM -> TimeHelper.MinutesToSeconds(2.5);
						case ATIUM -> TimeHelper.MinutesToSeconds(0.5f);
						case MALATIUM -> TimeHelper.MinutesToSeconds(1);
						default -> 99;
					};
			return Mth.floor(burnTimeInSeconds);
		}

		public double getEntityAbilityStrength(LivingEntity killedEntity, Player playerEntity)
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
					final AttributeMap attributes = killedEntity.getAttributes();
					if (attributes.hasAttribute(Attributes.ATTACK_DAMAGE))
					{
						strengthToAdd = attributes.getBaseValue(Attributes.ATTACK_DAMAGE) * 0.7D;
					}
					break;
				case TIN:
					//Steals senses

					//can track you on a huge island, even underground.
					if (killedEntity instanceof EnderDragon)
					{
						strengthToAdd = 0.77;
					}
					//Literally hunt players at night
					else if (killedEntity instanceof Phantom)
					{
						strengthToAdd = 0.66;
					}
					else if (killedEntity instanceof Player)
					{
						strengthToAdd = 0.25;
					}
					else if (killedEntity instanceof Cat)
					{
						strengthToAdd = 0.5;
					}
					else
					{
						final AttributeInstance attribute = killedEntity.getAttribute(Attributes.FOLLOW_RANGE);

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
							strengthToAdd = 0.10;
						}
					}
					break;
				case COPPER:
					//Steals mental fortitude, memory, and intelligence
					//increase base xp gain rate
					final float potentialRewardRate = killedEntity.getExperienceReward() / 150f;

					if (killedEntity instanceof Player)
					{
						//todo do better
						final Attribute xpAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(CosmereAPI.COSMERE_MODID, Metals.MetalType.COPPER.getName()));
						if (xpAttribute != null)
						{
							final AttributeInstance attribute = killedEntity.getAttribute(xpAttribute);
							if (attribute != null)
							{
								//70% strength to spike
								strengthToAdd = attribute.getValue() * 0.70;
								//25% remaining on player
								final double newBaseValue = attribute.getValue() * 0.25;
								attribute.setBaseValue(((int) (newBaseValue * 100)) / 100f);
							}
							else
							{
								strengthToAdd = potentialRewardRate;
							}
						}
					}
					else if (killedEntity instanceof EnderDragon dragonEntity)
					{
						//dragon doesn't reward xp in a typical way
						strengthToAdd =
								dragonEntity.getDragonFight() != null && !dragonEntity.getDragonFight().hasPreviouslyKilledDragon()
								? 1 //give first person to kill dragon a full rate increase spike
								: 0.33;//else similar to wither rate.
					}
					else
					{
						strengthToAdd = potentialRewardRate;
					}
					break;
				case ZINC:
					//Steals emotional fortitude
					//todo figure out what that means
					break;
				case ALUMINUM:
					//done else where.
					break;
				case DURALUMIN://todo hemalurgic connection/identity
					//Steals Connection/Identity
					break;
				case CHROMIUM:
					//Might steal destiny
					//so we could add some permanent luck?

					if (killedEntity instanceof Rabbit)
					{
						strengthToAdd = 0.25;
					}
					else if (killedEntity instanceof WitherSkeleton)
					{
						strengthToAdd = -1;
					}
					else if (killedEntity instanceof WitherBoss)
					{
						strengthToAdd = -3;
					}
					else if (killedEntity instanceof Pufferfish)
					{
						strengthToAdd = -0.5;
					}
					else if (killedEntity instanceof Cat cat)
					{
						final CatVariant catType = cat.getCatVariant();
						if (catType == CatVariant.BLACK)//all black
						{
							strengthToAdd = -5;
						}
						else if (catType == CatVariant.WHITE)//white
						{
							strengthToAdd = 1;
						}
					}
					break;
				case NICROSIL://todo hemalurgic investiture
					//Steals Investiture
					break;
			}
			return strengthToAdd;
		}

		public String getShortHemalurgicUseString()
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
					return "Stores Height. Atium ferrings become smaller during active storage, and can tap it later to be come larger.";
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

		public boolean hasAttribute()
		{
			switch (this)
			{
				case TIN, COPPER, ATIUM ->
				{
					return true;
				}
			}
			return false;
		}

		//todo do something better than sticking this in enum api
		public Item getNugget()
		{
			if (!hasMaterialItem())
			{
				switch (this)
				{
					case IRON:
						return Items.IRON_NUGGET;
					case GOLD:
						return Items.GOLD_NUGGET;
				}
			}

			var resourceLoc = new ResourceLocation(CosmereAPI.COSMERE_MODID, getName() + Constants.RegNameStubs.NUGGET);
			return ForgeRegistries.ITEMS.getValue(resourceLoc);
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
		public static final Color LERASATIUM = Color.decode("#8e9194");

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
