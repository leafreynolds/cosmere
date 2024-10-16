/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.metalScanning;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Largely based on Scannable, so we can use their concept of block clusters
 * Obtained at 5 - 4 - 2023
 * https://github.com/MightyPirates/Scannable/blob/d543d2ee6181d5d61010b64a1824096141c81fcd/src/main/java/li/cil/scannable/client/scanning/ScanResultProviderBlock.java
 */
public final class ScanResult
{
	public final List<Vec3> foundEntities = new ArrayList<>();
	public final List<BlockPos> foundBlocks = new ArrayList<>();
	public BlockScanResult targetedCluster = null;
	public boolean hasTargetedCluster = false;

	public final List<BlockScanResult> clusterResults = new ArrayList<>();
	private final Map<BlockPos, BlockScanResult> clusters = new HashMap<>();

	public List<Vec3> clusterCenters = new ArrayList<>();


	public void Clear()
	{
		foundEntities.clear();
		foundBlocks.clear();
		clusterResults.clear();
		clusters.clear();
	}

	public void addBlock(BlockPos blockPos, Vec3 currentClosestMetalObject)
	{
		foundBlocks.add(blockPos);

		//Has a cluster been made nearby already?
		if (!tryAddToCluster(clusters, blockPos, currentClosestMetalObject))
		{
			//if not, make a new cluster
			final BlockScanResult result = new BlockScanResult(blockPos);
			clusters.put(blockPos, result);
			clusterResults.add(result);
		}
	}

	private boolean tryAddToCluster(final Map<BlockPos, BlockScanResult> clusters, final BlockPos pos, Vec3 currentClosestMetalObject)
	{
		BlockScanResult root = null;

		//all the blocks directly touching this one
		final BlockPos east = pos.east();
		root = tryAddToCluster(clusters, pos, east, root);
		final BlockPos west = pos.west();
		root = tryAddToCluster(clusters, pos, west, root);
		final BlockPos north = pos.north();
		root = tryAddToCluster(clusters, pos, north, root);
		final BlockPos south = pos.south();
		root = tryAddToCluster(clusters, pos, south, root);
		root = tryAddToCluster(clusters, pos, pos.above(), root);
		root = tryAddToCluster(clusters, pos, pos.below(), root);

		//center slice
		root = tryAddToCluster(clusters, pos, north.above(), root);
		root = tryAddToCluster(clusters, pos, north.below(), root);
		root = tryAddToCluster(clusters, pos, south.above(), root);
		root = tryAddToCluster(clusters, pos, south.below(), root);

		//west slice
		root = tryAddToCluster(clusters, pos, east.above(), root);
		root = tryAddToCluster(clusters, pos, east.below(), root);
		final BlockPos eastNorth = east.north();
		root = tryAddToCluster(clusters, pos, eastNorth, root);
		root = tryAddToCluster(clusters, pos, eastNorth.above(), root);
		root = tryAddToCluster(clusters, pos, eastNorth.below(), root);
		final BlockPos eastSouth = east.south();
		root = tryAddToCluster(clusters, pos, eastSouth, root);
		root = tryAddToCluster(clusters, pos, eastSouth.above(), root);
		root = tryAddToCluster(clusters, pos, eastSouth.below(), root);

		//east slice
		root = tryAddToCluster(clusters, pos, west.above(), root);
		root = tryAddToCluster(clusters, pos, west.below(), root);
		final BlockPos westNorth = west.north();
		root = tryAddToCluster(clusters, pos, westNorth, root);
		root = tryAddToCluster(clusters, pos, westNorth.above(), root);
		root = tryAddToCluster(clusters, pos, westNorth.below(), root);
		final BlockPos westSouth = east.south();
		root = tryAddToCluster(clusters, pos, westSouth, root);
		root = tryAddToCluster(clusters, pos, westSouth.above(), root);
		root = tryAddToCluster(clusters, pos, westSouth.below(), root);

		if (root != null)
		{
			if (pos.getCenter().equals(currentClosestMetalObject))
			{
				targetedCluster = root;
				hasTargetedCluster = true;
			}
		}

		return root != null;
	}

	@Nullable
	private BlockScanResult tryAddToCluster(final Map<BlockPos, BlockScanResult> clusters, final BlockPos pos, final BlockPos clusterPos, @Nullable BlockScanResult root)
	{
		final BlockScanResult cluster = clusters.get(clusterPos);
		if (cluster == null)
		{
			return root;
		}

		if (root == null)
		{
			root = cluster.getRoot();
			root.add(pos);
			clusters.put(pos, root);
		}
		else
		{
			cluster.getRoot().setRoot(root);
		}

		return root;
	}

	public Vec3 finalizeClusters()
	{
		clusterCenters = clusterResults.stream().map(BlockScanResult::getPosition).toList();

		if (hasTargetedCluster)
		{
			return targetedCluster.getPosition();
		}
		return null;
	}
}
