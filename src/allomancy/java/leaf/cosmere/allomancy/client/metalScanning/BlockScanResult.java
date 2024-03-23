/*
 * File updated ~ 5 - 4 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.metalScanning;


import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Largely based on Scannable, so we can use their concept of block clusters
 * Obtained at 5 - 4 - 2023
 * https://github.com/MightyPirates/Scannable/blob/d543d2ee6181d5d61010b64a1824096141c81fcd/src/main/java/li/cil/scannable/client/scanning/ScanResultProviderBlock.java
 */
public final class BlockScanResult
{
	private AABB bounds;
	@Nullable
	private BlockScanResult parent;
	private final Set<BlockPos> blocks;

	public BlockScanResult(final BlockPos pos)
	{
		bounds = new AABB(pos);
		blocks = new HashSet<>();
		blocks.add(pos);
	}

	boolean isRoot()
	{
		return parent == null;
	}

	public BlockScanResult getRoot()
	{
		if (parent != null)
		{
			return parent.getRoot();
		}
		return this;
	}

	public void setRoot(final BlockScanResult root)
	{
		if (root == this)
		{
			return;
		}

		assert parent == null;

		root.bounds = root.bounds.minmax(bounds);
		root.blocks.addAll(blocks);
		blocks.clear();
		parent = root;
	}

	public void add(final BlockPos pos)
	{
		assert parent == null : "Trying to add to non-root node.";
		bounds = bounds.minmax(new AABB(pos));
		blocks.add(pos);
	}

	@Nullable
	public AABB getRenderBounds()
	{
		return bounds;
	}

	public Vec3 getPosition()
	{
		return bounds.getCenter();
	}

	public ArrayList<BlockPos> getBlocks()
	{
		return new ArrayList<>(blocks);
	}

}