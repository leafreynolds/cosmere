/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Roshar
{
	public enum GemSize
	{
		BROAM,
		MARK,
		CHIP;

		public float getChargeModifier()
		{
			return switch (this)
			{
				case BROAM -> 1f;
				case MARK -> 0.5f;
				case CHIP -> 0.1f;
			};
		}
	}

	public enum Surges
	{
		ADHESION(0),
		GRAVITATION(1),
		DIVISION(2),
		ABRASION(3),
		PROGRESSION(4),
		ILLUMINATION(5),
		TRANSFORMATION(6),
		TRANSPORTATION(7),
		COHESION(8),
		TENSION(9);

		private final int id;

		Surges(int id)
		{
			this.id = id;
		}

		public static Optional<Surges> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(surge -> surge.id == value)
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
	}

	//https://coppermind.net/wiki/Gemstone
	public enum Gemstone implements Tier
	{
		SAPPHIRE(0),
		SMOKESTONE(1),
		RUBY(2),
		DIAMOND(3),
		EMERALD(4),
		GARNET(5),
		ZIRCON(6),
		AMETHYST(7),
		TOPAZ(8),
		HELIODOR(9);


		private final int id;

		Gemstone(int id)
		{
			this.id = id;
		}

		public static Optional<Gemstone> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(gemType -> gemType.id == value)
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


		public RadiantOrder getAssociatedOrder()
		{
			return switch (this)
			{
				case SAPPHIRE -> RadiantOrder.WINDRUNNER;
				case SMOKESTONE -> RadiantOrder.SKYBREAKER;
				case RUBY -> RadiantOrder.DUSTBRINGER;
				case DIAMOND -> RadiantOrder.EDGEDANCER;
				case EMERALD -> RadiantOrder.TRUTHWATCHER;
				case GARNET -> RadiantOrder.LIGHTWEAVER;
				case ZIRCON -> RadiantOrder.ELSECALLER;
				case AMETHYST -> RadiantOrder.WILLSHAPER;
				case TOPAZ -> RadiantOrder.STONEWARD;
				case HELIODOR -> RadiantOrder.BONDSMITH;
			};
		}

		public int getColorValue()
		{
			return getColor().getRGB();
		}

		public Color getColor()
		{
			return switch (this)
			{
				case SAPPHIRE -> GemColours.SAPPHIRE;
				case SMOKESTONE -> GemColours.SMOKESTONE;
				case RUBY -> GemColours.RUBY;
				case DIAMOND -> GemColours.DIAMOND;
				case EMERALD -> GemColours.EMERALD;
				case GARNET -> GemColours.GARNET;
				case ZIRCON -> GemColours.ZIRCON;
				case AMETHYST -> GemColours.AMETHYST;
				case TOPAZ -> GemColours.TOPAZ;
				case HELIODOR -> GemColours.HELIODOR;
			};
		}


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
			return null;
		}

	}

	private static class GemColours
	{
		public static final Color SAPPHIRE = Color.decode("#3991f3");
		public static final Color SMOKESTONE = Color.decode("#c0a6b3");
		public static final Color RUBY = Color.decode("#b40502");
		public static final Color DIAMOND = Color.decode("#f5faf3");
		public static final Color EMERALD = Color.decode("#31E840");
		public static final Color GARNET = Color.decode("#f03c72");
		public static final Color ZIRCON = Color.decode("#3ab7bb");
		public static final Color AMETHYST = Color.decode("#c975e4");
		public static final Color TOPAZ = Color.decode("#e3681a");
		public static final Color HELIODOR = Color.decode("#f3dd25");
	}


	public enum RadiantOrder
	{
		WINDRUNNER(Gemstone.SAPPHIRE),
		SKYBREAKER(Gemstone.SMOKESTONE),
		DUSTBRINGER(Gemstone.RUBY),
		EDGEDANCER(Gemstone.DIAMOND),
		TRUTHWATCHER(Gemstone.EMERALD),
		LIGHTWEAVER(Gemstone.GARNET),
		ELSECALLER(Gemstone.ZIRCON),
		WILLSHAPER(Gemstone.AMETHYST),
		STONEWARD(Gemstone.TOPAZ),
		BONDSMITH(Gemstone.HELIODOR);


		private final Gemstone gemstone;

		RadiantOrder(Gemstone gemstone)
		{
			this.gemstone = gemstone;
		}

		public static Optional<RadiantOrder> valueOf(int value)
		{
			return Arrays.stream(EnumUtils.RADIANT_ORDERS)
					.filter(order -> order.getID() == value)
					.findFirst();
		}

		public int getID()
		{
			return gemstone.getID();
		}

		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
		}

		public Surges getFirstSurge()
		{
			switch (this)
			{
				default:
				case WINDRUNNER://Adhesion & Gravitation
					return Surges.ADHESION;
				case SKYBREAKER://Gravitation & Division
					return Surges.GRAVITATION;
				case DUSTBRINGER://Division & Abrasion
					return Surges.DIVISION;
				case EDGEDANCER://Abrasion & Progression
					return Surges.ABRASION;
				case TRUTHWATCHER://Progression & Illumination
					return Surges.PROGRESSION;
				case LIGHTWEAVER://Illumination & Transformation
					return Surges.ILLUMINATION;
				case ELSECALLER://	Transformation & Transportation
					return Surges.TRANSFORMATION;
				case WILLSHAPER://Transportation & Cohesion
					return Surges.TRANSPORTATION;
				case STONEWARD://Cohesion & Tension
					return Surges.COHESION;
				case BONDSMITH://Tension & Adhesion
					return Surges.TENSION;
			}
		}

		public Surges getSecondSurge()
		{
			switch (this)
			{
				default:
				case WINDRUNNER://Adhesion & Gravitation
					return Surges.GRAVITATION;
				case SKYBREAKER://Gravitation & Division
					return Surges.DIVISION;
				case DUSTBRINGER://Division & Abrasion
					return Surges.ABRASION;
				case EDGEDANCER://Abrasion & Progression
					return Surges.PROGRESSION;
				case TRUTHWATCHER://Progression & Illumination
					return Surges.ILLUMINATION;
				case LIGHTWEAVER://Illumination & Transformation
					return Surges.TRANSFORMATION;
				case ELSECALLER://Transformation & Transportation
					return Surges.TRANSPORTATION;
				case WILLSHAPER://Transportation & Cohesion
					return Surges.COHESION;
				case STONEWARD://Cohesion & Tension
					return Surges.TENSION;
				case BONDSMITH://Tension & Adhesion
					return Surges.ADHESION;
			}
		}

		public int getColorValue()
		{
			return getColor().getRGB();
		}

		public Color getColor()
		{
			return gemstone.getColor();
		}

		public Gemstone getGemstone()
		{
			return gemstone;
		}

		public boolean hasBlade()
		{
			switch (this)
			{
				case BONDSMITH:
					return false;
				default:
					return true;
			}
		}

		public Color getPlateColor()
		{
			return switch (this)
			{
				case WINDRUNNER -> PlateColors.WINDRUNNER;
				case SKYBREAKER -> PlateColors.SKYBREAKER;
				case DUSTBRINGER -> PlateColors.DUSTBRINGER;
				case EDGEDANCER -> PlateColors.EDGEDANCER;
				case TRUTHWATCHER -> PlateColors.TRUTHWATCHER;
				case LIGHTWEAVER -> PlateColors.LIGHTWEAVER;
				case ELSECALLER -> PlateColors.ELSECALLER;
				case WILLSHAPER -> PlateColors.WILLSHAPER;
				case STONEWARD -> PlateColors.STONEWARD;
				case BONDSMITH -> PlateColors.BONDSMITH;
			};
		}

		public String getNahelSpren()
		{
			return switch (this)
			{
				case WINDRUNNER -> "honorspren";
				case SKYBREAKER -> "highspren";
				case DUSTBRINGER -> "ashspren";
				case EDGEDANCER -> "cultivationspren";
				case TRUTHWATCHER -> "mistspren";
				case LIGHTWEAVER -> "cryptic";
				case ELSECALLER -> "inkspren";
				case WILLSHAPER -> "lightspren";
				case STONEWARD -> "peakspren";
				case BONDSMITH -> null;

			};
		}
		public String getSprenOrBondsmith(int spren)
		{
			return this.getSprenOrBondsmith(spren, true);
		}

		public String getSprenOrBondsmith(int spren, boolean newSpren)
		{
			if(this.equals(RadiantOrder.BONDSMITH)  || (spren >= 1 && spren <= 3))
			{
				if (newSpren)
				{
					return switch (spren)
					{
						case 1 -> "stormfather";
						case 2 -> "sibling";
						case 3 -> "nightwatcher";
						default -> getNahelSpren();
					};
				}
				else
				{
					return switch (spren)
					{
						case 1 -> "wind";
						case 2 -> "stone";
						case 3 -> "night";
						default -> getNahelSpren();
					};
				}

			}
			return getNahelSpren();
		}

		public String getLesserSpren()
		{
			return switch (this)
			{
				case WINDRUNNER -> "windspren";
				case SKYBREAKER -> "gravitationspren";
				case DUSTBRINGER -> "flamespren";
				case EDGEDANCER -> "lifespren";
				case TRUTHWATCHER -> "concentrationspren";
				case LIGHTWEAVER -> "creationspren";
				case ELSECALLER -> "logicspren";
				case WILLSHAPER -> "joyspren";
				case STONEWARD -> "stonespren";
				case BONDSMITH -> "gloryspren";
			};
		}
	}

	private static class PlateColors
	{
		public static final Color WINDRUNNER = Color.decode("#17008a");
		public static final Color SKYBREAKER = Color.decode("#c2c2c2");
		public static final Color DUSTBRINGER = Color.decode("#574e4b");
		public static final Color EDGEDANCER = Color.decode("#c6cfd8");
		public static final Color TRUTHWATCHER = Color.decode("#6d8f5b");
		public static final Color LIGHTWEAVER = Color.decode("#8a343e");
		public static final Color ELSECALLER = Color.decode("#1e6c82");
		public static final Color WILLSHAPER = Color.decode("#d4d4d4");
		public static final Color STONEWARD = Color.decode("#b09335");
		public static final Color BONDSMITH = Color.decode("#f3dd25");
		public static final Color DEADPLATE = Color.decode("#757575");
	}

	public static Color getDeadplate()
	{
		return PlateColors.DEADPLATE;
	}
}