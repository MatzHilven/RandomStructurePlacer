package me.matzhilven.randomstructureplacer.schematic.blockentity;


import me.matzhilven.randomstructureplacer.schematic.blockentity.impl.BlockEntitySign;

public enum BlockEntityType {

    SIGN("minecraft:sign", BlockEntitySign.class);

    /* The id of the block entity type */
    private final String id;
    /* The class type of the block entity type */
    private final Class<? extends BlockEntity> type;

    /**
     * Construct a new BlockEntityType.
     *
     * @param id   - the id of the block entity
     * @param type - the implementation of the block entity
     */
    BlockEntityType(String id, Class<? extends BlockEntity> type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Get the {@link BlockEntityType} by id.
     *
     * @param id - the id of the block entity type
     * @return - the block entity type or else {@code null}
     */
    public static BlockEntityType fromId(String id) {
        for (BlockEntityType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) {
                return type;
            }
        }

        return null;
    }

    /**
     * Get the id of this {@link BlockEntityType}.
     *
     * @return - the id of this block entity type
     */
    public String getId() {
        return id;
    }

    /**
     * Get the implementation of this {@link BlockEntityType}.
     *
     * @return - the implementation of this block entity type
     */
    public Class<? extends BlockEntity> getType() {
        return type;
    }
}
