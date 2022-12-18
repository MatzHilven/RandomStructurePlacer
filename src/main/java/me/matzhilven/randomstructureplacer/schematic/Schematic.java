package me.matzhilven.randomstructureplacer.schematic;

import me.matzhilven.randomstructureplacer.schematic.blockentity.BlockEntity;
import me.matzhilven.randomstructureplacer.schematic.blockentity.BlockEntityType;
import me.matzhilven.randomstructureplacer.utils.Region;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to parse schematics
 */
public class Schematic implements Cloneable {

    protected static final String FORMAT = ".schem";

    /* The absolute path to the schematic */
    protected final String absolutePath;
    /* The block data palette of the schematic */
    protected final Map<Integer, BlockData> blockPalette;
    /* The block entity map of the schematic */
    protected final Map<Integer, BlockEntity> blockEntityMap;
    /* The dimensions of the schematic */
    protected short width, height, length;
    /* The block data array of the schematic */
    protected byte[] blockDataArray;

    /**
     * Construct a new {@link Schematic}.
     * <p>
     * Note: This does not parse the schematic yet, to parse the schematic use {@link Schematic#parse()}.
     * </p>
     *
     * @param absolutePath - the absolute path to the schematic
     */
    public Schematic(String absolutePath) {
        // Make sure the path ends with the file format!
        if (!absolutePath.endsWith(FORMAT))
            throw new UnsupportedOperationException("Schematic format not supported! Please make sure the file is a schematic!");

        this.absolutePath = absolutePath;
        this.blockPalette = new HashMap<>();
        this.blockEntityMap = new HashMap<>();
    }

    /**
     * Parse the schematic.
     *
     * @throws IOException thrown if the schematic file doesn't exist or if it's not in the proper nbt format!
     */
    public void parse() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // Create input stream using absolute path
        InputStream inputStream = new FileInputStream(absolutePath);

        // Parse the nbt data using minecraft's NBT library
        NBTTagCompound nbtData = NBTCompressedStreamTools.a(inputStream);

        // Get the dimensions of the schematic
        width = nbtData.getShort("Width");
        height = nbtData.getShort("Height");
        length = nbtData.getShort("Length");

        // Get the block data as byte array
        blockDataArray = nbtData.getByteArray("BlockData");

        // Get the block palette, this contains all the possible block states in this schematic
        // These block states are stored as a string
        // Example: minecraft:oak_fence[east=false,north=true,south=false,waterlogged=false,west=false]
        NBTTagCompound palette = nbtData.getCompound("Palette");

        // Get all the tile entities in this schematic
        NBTTagList blockEntities = (NBTTagList) nbtData.get("BlockEntities");

        // Make sure the schematic contains block entities
        if (blockEntities != null) {

            // Loop through all block entities of the schematic
            for (NBTBase nbtBase : blockEntities) {

                // Get the block entity nbt data
                NBTTagCompound blockEntityNBTData = (NBTTagCompound) nbtBase;

                // Get the block entity type of the block entity
                BlockEntityType blockEntityType = BlockEntityType.fromId(blockEntityNBTData.getString("Id"));

                // Make sure the block entity type is implemented
                // If not, skip the entity
                if (blockEntityType == null) continue;

                // Get the block entity constructor and create a new block entity
                Constructor<? extends BlockEntity> constructor = blockEntityType.getType().getConstructor(NBTTagCompound.class);
                BlockEntity blockEntity = constructor.newInstance(blockEntityNBTData);

                // The index of the block at X, Y, Z is (Y * length + Z) * width + X.
                int index = (blockEntity.getPosition().getBlockY() * length + blockEntity.getPosition().getBlockZ()) * width + blockEntity.getPosition().getBlockX();

                // Add the block entity to the block entity list
                blockEntityMap.put(index, blockEntity);
            }
        }

        // Loop through all the block states in the palette
        palette.getKeys().forEach(state -> {

            // Get the index of the block state
            int index = palette.getInt(state);

            // Construct the block data based of the block state and add it to the block data map
            BlockData blockData = Bukkit.createBlockData(state);
            blockPalette.put(index, blockData);
        });

        // Close the input stream
        inputStream.close();
    }

    /**
     * Paste the schematic at the target location.
     *
     * @param targetLocation  - the target location
     * @param ignoreAirBlocks - whether to ignore air blocks or not
     */
    public Region paste(Location targetLocation, boolean ignoreAirBlocks) {

        Location pasteLocation = null;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {

                    // The index of the block at X, Y, Z is (Y * length + Z) * width + X.
                    int index = (y * length + z) * width + x;

                    // Get the block data from the palette
                    BlockData blockData = blockPalette.get((int) blockDataArray[index]);

                    // Make sure block data isn't null
                    if (blockData == null) continue;

                    // Ignore air blocks
                    if (ignoreAirBlocks && blockData.getMaterial() == Material.AIR) continue;

                    pasteLocation = targetLocation.clone().add(x, y, z);

                    // Set the block data using the block's state
                    BlockState blockState = pasteLocation.getBlock().getState();
                    blockState.setBlockData(blockData);

                    // Force the update but don't update physics to make sure the blocks get pasted properly.
                    // If applyPhysics is set to true, portal blocks won't get placed and sand blocks will fall etc.
                    blockState.update(true, false);

                    // Apply block entity data
                    if (blockEntityMap.get(index) != null) {
                        blockEntityMap.get(index).applyData(targetLocation);
                    }
                }
            }
        }

        return new Region(
                targetLocation.getBlockX(),
                targetLocation.getBlockY(),
                targetLocation.getBlockZ(),
                pasteLocation.getBlockX(),
                pasteLocation.getBlockY(),
                pasteLocation.getBlockZ()
        );
    }

    /**
     * Get the width of this schematic.
     *
     * @return - the width of this schematic
     */
    public short getWidth() {
        return width;
    }

    /**
     * Get the height of this schematic.
     *
     * @return - the height of this schematic
     */
    public short getHeight() {
        return height;
    }

    /**
     * Get the length of this schematic.
     *
     * @return - the length of this schematic
     */
    public short getLength() {
        return length;
    }

    /**
     * Get the size of the the schematic.
     * <p>
     * Note: Size = width * height * length
     * </p>
     *
     * @return - the size of this schematic
     */
    public int getSize() {
        return width * height * length;
    }


    @Override
    public Schematic clone() {
        try {
            return (Schematic) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}