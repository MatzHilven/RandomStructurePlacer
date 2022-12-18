package me.matzhilven.randomstructureplacer.schematic.blockentity.impl;

import me.matzhilven.randomstructureplacer.schematic.blockentity.BlockEntity;
import me.matzhilven.randomstructureplacer.schematic.blockentity.BlockEntityType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class BlockEntitySign extends BlockEntity {


    /* The lines of this sign */
    private String[] lines;


    /**
     * Construct a new Block Sign.
     *
     * @param nbtData - the nbt data that represents this block entity
     */
    public BlockEntitySign(NBTTagCompound nbtData) {
        super(BlockEntityType.SIGN, nbtData);
    }

    @Override
    public void parseData(NBTTagCompound nbtData) {
        lines = new String[4];

        // Parse the lines of the sign
        for (int i = 0; i < 4; i++) {
            lines[i] = ChatColor.stripColor(ComponentSerializer.parse(nbtData.getString("Text" + (i + 1)))[0].toLegacyText());
        }
    }

    @Override
    public void applyData(Location baseLocation) {

        if (lines == null) return;

        Block block = baseLocation.clone().add(getPosition().getBlockX(), getPosition().getBlockY(), getPosition().getBlockZ()).getBlock();
        Sign sign = (Sign) block.getState();

        for (int i = 0; i < 4; i++) {
            sign.setLine(i, lines[i]);
        }

        sign.update();
    }

    /**
     * Get the lines of this sign.
     *
     * @return - a String array that contains all the lines of this sign.
     */
    public String[] getLines() {
        return lines;
    }
}
