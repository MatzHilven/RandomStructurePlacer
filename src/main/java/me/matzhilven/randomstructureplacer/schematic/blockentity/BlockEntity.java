package me.matzhilven.randomstructureplacer.schematic.blockentity;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagIntArray;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class BlockEntity {

    /* The type of the tile entity */
    private final BlockEntityType type;
    /* The relative position of the tile entity */
    private Vector position;

    /**
     * Construct a new Block Entity.
     *
     * @param type    - the type of the block entity
     * @param nbtData - the nbt data that represents this block entity
     */
    public BlockEntity(BlockEntityType type, NBTTagCompound nbtData) {
        this.type = type;

        // Parse the nbt data
        parsePosition(nbtData);
        parseData(nbtData);
    }

    /**
     * Parse the NBT data specific to this {@link BlockEntity} type.
     *
     * @param nbtData - the nbt data that represents this block entity
     */
    public abstract void parseData(NBTTagCompound nbtData);

    /**
     * Apply the block entity data to a block in the World.
     *
     * @param baseLocation - the base location of the schematic
     */
    public abstract void applyData(Location baseLocation);

    /**
     * Parse the position from the NBT data.
     *
     * @param nbtData - the nbt data that represents this block entity
     */
    private void parsePosition(NBTTagCompound nbtData) {

        // Get the position from the nbt data
        NBTTagIntArray coordinates = (NBTTagIntArray) nbtData.get("Pos");
        if (coordinates == null || coordinates.size() != 3)
            throw new IllegalArgumentException("Invalid position format!");

        // Set the position
        setPosition(new Vector(coordinates.get(0).asInt(), coordinates.get(1).asInt(), coordinates.get(2).asInt()));
    }

    /**
     * Get the type of this {@link BlockEntity}
     *
     * @return - the type of this block entity
     */
    public BlockEntityType getType() {
        return type;
    }

    /**
     * Get the position of this {@link BlockEntity} relative to (0, 0, 0) of the schematic.
     *
     * @return - the relative position of this block entity
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * Set the position of this {@link BlockEntity} relative to (0, 0, 0) of the schematic.
     *
     * @param position - the new relative position of this block entity
     */
    public void setPosition(Vector position) {
        this.position = position;
    }
}
