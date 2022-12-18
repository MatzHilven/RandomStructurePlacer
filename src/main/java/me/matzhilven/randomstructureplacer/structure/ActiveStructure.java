package me.matzhilven.randomstructureplacer.structure;

import me.matzhilven.randomstructureplacer.utils.Region;
import org.bukkit.Material;
import org.bukkit.World;

public class ActiveStructure extends Structure {

    private final World spawnWorld;
    private final Region structureRegion;

    public ActiveStructure(Structure structure, World spawnWorld, Region structureRegion) {
        super(structure.getId(), structure.getSchematic(), structure.getSpawnType(), structure.getSpawnRegion(),
                structure.getSpawnLocation(), structure.getMinFrequency(), structure.getMaxFrequency(),
                structure.getActiveTime(), structure.getMaxInstances());
        this.spawnWorld = spawnWorld;
        this.structureRegion = structureRegion;
    }

    public void remove() {
        for (int x = structureRegion.getMinX(); x < structureRegion.getMaxX(); x++) {
            for (int y = structureRegion.getMinY(); y < structureRegion.getMaxY(); y++) {
                for (int z = structureRegion.getMinZ(); z < structureRegion.getMaxZ(); z++) {
                    spawnWorld.getBlockAt(x,y,z).setType(Material.AIR);
                }
            }
        }
    }


    public World getSpawnWorld() {
        return spawnWorld;
    }

    public Region getStructureRegion() {
        return structureRegion;
    }
}
