/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import net.minecraftforge.fml.ModContainer;
import org.apache.maven.artifact.versioning.ArtifactVersion;


public class Version
{

	public int major;

	public int minor;

	public int build;

	public Version(int majorNum, int minorNum, int buildNum)
	{
		major = majorNum;
		minor = minorNum;
		build = buildNum;
	}

	public Version(ArtifactVersion artifactVersion)
	{
		this(artifactVersion.getMajorVersion(), artifactVersion.getMinorVersion(), artifactVersion.getIncrementalVersion());
	}

	public Version(ModContainer container)
	{
		this(container.getModInfo().getVersion());
	}

	public static Version get(String s)
	{
		String[] split = s.replace('.', ':').split(":");
		if (split.length != 3)
		{
			return null;
		}
		for (String i : split)
		{
			for (char c : i.toCharArray())
			{
				if (!Character.isDigit(c))
				{
					return null;
				}
			}
		}

		int[] digits = new int[3];
		for (int i = 0; i < 3; i++)
		{
			digits[i] = Integer.parseInt(split[i]);
		}
		return new Version(digits[0], digits[1], digits[2]);
	}

	public void reset()
	{
		major = 0;
		minor = 0;
		build = 0;
	}

	public byte comparedState(Version version)
	{
		if (version.major > major)
		{
			return -1;
		}
		else if (version.major == major)
		{
			if (version.minor > minor)
			{
				return -1;
			}
			else if (version.minor == minor)
			{
				return (byte) Integer.compare(build, version.build);
			}
			return 1;
		}
		return 1;
	}

	@Override
	public String toString()
	{
		if (major == 0 && minor == 0 && build == 0)
		{
			return "";
		}
		return major + "." + minor + "." + build;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + build;
		result = 31 * result + major;
		result = 31 * result + minor;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		Version other = (Version) obj;
		return build == other.build && major == other.major && minor == other.minor;
	}
}