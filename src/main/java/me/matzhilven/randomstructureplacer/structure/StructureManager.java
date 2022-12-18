package me.matzhilven.randomstructureplacer.structure;

import me.matzhilven.randomstructureplacer.RandomStructurePlacer;
import me.matzhilven.randomstructureplacer.schematic.Schematic;
import me.matzhilven.randomstructureplacer.utils.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class StructureManager {

    private final RandomStructurePlacer main;

    private final World defaultWorld;

    private final HashMap<String, Structure> structures;
    private final List<ActiveStructure> activeStructures;

    public StructureManager(RandomStructurePlacer main) {
        this.main = main;

        this.structures = new HashMap<>();
        this.activeStructures = new ArrayList<>();

        this.defaultWorld = Bukkit.getWorld(main.getSchematicsConfig().getString("world"));

        loadStructures();
    }

    private void loadStructures() {
        main.getSchematicsConfig().getConfig().getConfigurationSection("structures").getKeys(false).forEach(s -> {
            String schematicName = main.getSchematicsConfig().getString("structures." + s + ".name");
            Schematic schematic = new Schematic(main.getDataFolder().getAbsolutePath() + "/schematics/" + schematicName);
            String spawnType = main.getSchematicsConfig().getString("structures." + s + ".spawn.type");
            Region spawnRegion = new Region(
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.minX"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.minY"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.minZ"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.maxX"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.maxY"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.region.maxZ")
            );
            Location spawnLocation = new Location(defaultWorld,
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.spawn.x"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.spawn.y"),
                    main.getSchematicsConfig().getInt("structures." + s + ".spawn.spawn.z")
            );
            int minFrequency = main.getSchematicsConfig().getInt("structures." + s + ".frequency.min");
            int maxFrequency = main.getSchematicsConfig().getInt("structures." + s + ".frequency.max");
            int activeTime = main.getSchematicsConfig().getInt("structures." + s + ".active-time");
            int maxInstances = main.getSchematicsConfig().getInt("structures." + s + ".max-instances");

            structures.put(s, new Structure(
                    s,
                    schematic,
                    spawnType,
                    spawnRegion,
                    spawnLocation,
                    minFrequency,
                    maxFrequency,
                    activeTime,
                    maxInstances
            ));
        });
    }

    public Optional<Structure> getStructure(String id) {
        return Optional.ofNullable(structures.get(id));
    }

    public void addStructure(Structure structure) {

        if (activeStructures.stream().filter(activeStructure -> activeStructure.getId().equals(structure.getId())).count()
                == structure.getMaxInstances()) return;

        ActiveStructure activeStructure = structure.spawn(defaultWorld);

        activeStructures.add(activeStructure);

        Bukkit.getScheduler().runTaskLater(main, activeStructure::remove, structure.getActiveTime() * 20L);
    }
}
