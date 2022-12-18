package me.matzhilven.randomstructureplacer;

import me.matzhilven.randomstructureplacer.structure.StructureManager;
import me.matzhilven.randomstructureplacer.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RandomStructurePlacer extends JavaPlugin {

    private Config schematicsConfig;
    private StructureManager structureManager;

    @Override
    public void onEnable() {

        File schematicFolder = new File(getDataFolder(), "schematics");
        if (!schematicFolder.exists()) schematicFolder.mkdirs();

        schematicsConfig = new Config(this, "schematics.yml");

        structureManager = new StructureManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Config getSchematicsConfig() {
        return schematicsConfig;
    }

    public StructureManager getStructureManager() {
        return structureManager;
    }
}
