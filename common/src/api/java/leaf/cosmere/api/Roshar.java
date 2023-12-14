/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

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


		public String getAssociatedOrder()
		{
			return switch (this)
					{
						case SAPPHIRE -> "windrunner";
						case SMOKESTONE -> "skybreaker";
						case RUBY -> "dustbringer";
						case DIAMOND -> "edgedancer";
						case EMERALD -> "truthwatcher";
						case GARNET -> "lightweaver";
						case ZIRCON -> "elsecaller";
						case AMETHYST -> "willshaper";
						case TOPAZ -> "stoneward";
						case HELIODOR -> "bondsmith";
					};
		}


		public Surges getFirstSurge()
		{
			switch (this)
			{
				default:
				case SAPPHIRE://Adhesion & Gravitation
					return Surges.ADHESION;
				case SMOKESTONE://Gravitation & Division
					return Surges.GRAVITATION;
				case RUBY://Division & Abrasion
					return Surges.DIVISION;
				case DIAMOND://Abrasion & Progression
					return Surges.ABRASION;
				case EMERALD://Progression & Illumination
					return Surges.PROGRESSION;
				case GARNET://Illumination & Transformation
					return Surges.ILLUMINATION;
				case ZIRCON://	Transformation & Transportation
					return Surges.TRANSFORMATION;
				case AMETHYST://Transportation & Cohesion
					return Surges.TRANSPORTATION;
				case TOPAZ://Cohesion & Tension
					return Surges.COHESION;
				case HELIODOR://Tension & Adhesion
					return Surges.TENSION;
			}
		}

		public Surges getSecondSurge()
		{
			switch (this)
			{
				default:
				case SAPPHIRE://Adhesion & Gravitation
					return Surges.GRAVITATION;
				case SMOKESTONE://Gravitation & Division
					return Surges.DIVISION;
				case RUBY://Division & Abrasion
					return Surges.ABRASION;
				case DIAMOND://Abrasion & Progression
					return Surges.PROGRESSION;
				case EMERALD://Progression & Illumination
					return Surges.ILLUMINATION;
				case GARNET://Illumination & Transformation
					return Surges.TRANSFORMATION;
				case ZIRCON://Transformation & Transportation
					return Surges.TRANSPORTATION;
				case AMETHYST://Transportation & Cohesion
					return Surges.COHESION;
				case TOPAZ://Cohesion & Tension
					return Surges.TENSION;
				case HELIODOR://Tension & Adhesion
					return Surges.ADHESION;
			}
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
		public static final Color EMERALD = Color.decode("#52b984");
		public static final Color GARNET = Color.decode("#e03935");
		public static final Color ZIRCON = Color.decode("#3ab7bb");
		public static final Color AMETHYST = Color.decode("#c975e4");
		public static final Color TOPAZ = Color.decode("#e3681a");
		public static final Color HELIODOR = Color.decode("#f3dd25");
	}
}