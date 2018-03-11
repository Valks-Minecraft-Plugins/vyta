package com.vyta.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class WorldGenerator extends org.bukkit.generator.ChunkGenerator {
	List<BlockPopulator> populators = new ArrayList<BlockPopulator>();

	public List<BlockPopulator> getDefaultPopulators(World world) {
		return populators;
	}

	byte getBlock(int x, int y, int z, byte[][] chunk) {	
		// if the Block section the block is in hasn't been used yet, allocate it
		try {
			if (chunk[y >> 4] == null)
				return 0; // block is air as it hasnt been allocated
			if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
				return 0;
			try {
				return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@SuppressWarnings("deprecation")
	/*
	 * Sets a block in the chunk. If the Block section doesn't exist, it allocates
	 * it. [y>>4] the section id (y/16) the math for the second offset confuses me
	 */
	void setBlock(int x, int y, int z, byte[][] chunk, Material material) {
		if (chunk[y >> 4] == null)
			chunk[y >> 4] = new byte[16 * 16 * 16];
		if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
			return; // Out of bounds
		try {
			chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public byte[][] generateBlockSections(World world, Random rand, int ChunkX, int ChunkZ, BiomeGrid biomeGrid) {
		// where we will store our blocks
		byte[][] chunk = new byte[world.getMaxHeight() / 16][];

		

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				// the seas
				for (int y = 0; y < 35; y++) {
					if (getBlock(x, y, z, chunk) == Material.AIR.getId()) {
						setBlock(x, y, z, chunk, Material.STATIONARY_WATER);
					}
				}
				
				// bottom
				for (int y = 0; y < 2; y++) {
					if (getBlock(x, y, z, chunk) == Material.STATIONARY_WATER.getId()) {
						setBlock(x, y, z, chunk, Material.SAND);
					}
				}
				
				setBlock(x, 0, z, chunk, Material.BEDROCK);
			}
		}
		return chunk;
	}
	
	@SuppressWarnings("deprecation")
	public void biomeBlocks(byte[][] chunk, int x, int y, int z, Material material, int depth) {
		for (int d = 0; d < depth; d++) {
			if (getBlock(x, y - d, z, chunk) != Material.AIR.getId())
				setBlock(x, y - d, z, chunk, material);
		}
	}
}