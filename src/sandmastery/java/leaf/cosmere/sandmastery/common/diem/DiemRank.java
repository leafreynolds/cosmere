package leaf.cosmere.sandmastery.common.diem;

import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Locale;

public enum DiemRank
{
	ACOLENT(0xFFFFFF, 0, 1),
	UNDERFEN(0xAAAAAA, 1, 1),
	FEN(0x55FF55, 2, 5),
	DIEMFEN(0x5555FF, 4, 7),
	UNDERLESTRELL(0xAA00AA, 6, 10),
	LESTRELL(0xFF5555, 9, 12),
	UNDERMASTRELL(0xFF8C00, 11, 14),
	MASTRELL(0xFFD700, 15, 20),
	LORD_MASTRELL(0xFFD700, 15, 25);

	public static final int UNTESTED = -1;

	private final int colour;
	private final int minRibbons;
	private final int practiceCap;

	DiemRank(int colour, int minRibbons, int practiceCap)
	{
		this.colour = colour;
		this.minRibbons = minRibbons;
		this.practiceCap = practiceCap;
	}

	public int getMinRibbons()
	{
		return minRibbons;
	}

	public int getPracticeCap()
	{
		return practiceCap;
	}

	public String getName()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	public String getTranslationKey()
	{
		return "diem_rank.sandmastery." + getName();
	}

	public Component getDisplayName()
	{
		return Component.translatable(getTranslationKey()).withStyle(style -> style.withColor(colour));
	}

	@Nullable
	public DiemRank next()
	{
		return ordinal() + 1 < values().length ? values()[ordinal() + 1] : null;
	}

	// UNTESTED or anything out of range is no rank
	@Nullable
	public static DiemRank byId(int id)
	{
		return id >= 0 && id < values().length ? values()[id] : null;
	}

	@Nullable
	public static DiemRank byName(String name)
	{
		for (DiemRank rank : values())
		{
			if (rank.getName().equals(name))
			{
				return rank;
			}
		}
		return null;
	}
}
