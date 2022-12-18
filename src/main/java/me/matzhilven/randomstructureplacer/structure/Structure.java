package me.matzhilven.randomstructureplacer.structure;

import me.matzhilven.randomstructureplacer.schematic.Schematic;
import me.matzhilven.randomstructureplacer.utils.Region;
import org.bukkit.Location;
import org.bukkit.Warning;
import org.bukkit.World;

public class Structure {

    private final String id;
    private final Schematic schematic;
    private final String spawnType;
    private final Region spawnRegion;
    private final Location spawnLocation;
    private final int minFrequency;
    private final int maxFrequency;
    private final int activeTime;
    private final int maxInstances;

    public Structure(String id, Schematic schematic, String spawnType, Region spawnRegion, Location spawnLocation,
                     int minFrequency, int maxFrequency, int activeTime, int maxInstances) {
        this.id = id;
        this.schematic = schematic;
        this.spawnType = spawnType;
        this.spawnRegion = spawnRegion;
        this.spawnLocation = spawnLocation;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.activeTime = activeTime;
        this.maxInstances = maxInstances;
    }

    public ActiveStructure spawn(World world) {

        Location spawnLocation;

        if (spawnType.equals("region")) {
            spawnLocation = spawnRegion.getRandomLocation(world);
        } else {
            spawnLocation = this.spawnLocation;
        }

        Region pasteRegion = schematic.paste(spawnLocation, true);

        return new ActiveStructure(this, world, pasteRegion);
    }

    public String getId() {
        return id;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public String getSpawnType() {
        return spawnType;
    }

    public Region getSpawnRegion() {
        return spawnRegion;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getMinFrequency() {
        return minFrequency;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }

    public int getActiveTime() {
        return activeTime;
    }

    public int getMaxInstances() {
        return maxInstances;
    }
}
