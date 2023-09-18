package com.simple.stats.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class playerNBT {

    final static String TagName = "simpleStats";

    static void writeDataToPlayerNBT(EntityPlayer player, String key, int count) {

        if (key == null || key.equals("") || count < 0 || player == null) {
            return;
        }

        NBTTagCompound playerData = player.getEntityData();
        if (!playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }

        NBTTagCompound playerPersistedData = playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        NBTTagCompound customData = new NBTTagCompound();
        customData.setInteger(key, count);
        playerPersistedData.setTag(TagName, customData);

        playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, playerPersistedData);

    }

    static int readDataFromPlayerNBT(EntityPlayer player, String key) {

        if (key == null || key.equals("") || player == null) {
            return 0;
        }

        NBTTagCompound playerData = player.getEntityData();
        if (!playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            return 0;
        }

        NBTTagCompound playerPersistedData = playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (playerPersistedData.hasKey(TagName) || playerPersistedData.getCompoundTag(TagName)
            .hasKey(key)) {
            return playerPersistedData.getCompoundTag(TagName)
                .getInteger(key);
        } else {
            return 0;
        }

    }
}
