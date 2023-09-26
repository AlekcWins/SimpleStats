package com.simple.stats.datastorage;

import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// todo need generic for mb refactor
public class PlayerNBTStorage implements IStorageData<Long, EntityPlayer> {

    private static final Logger log = LogManager.getLogger("PlayerNBTStorage");

    private EntityPlayer player;

    @Override
    public Optional<Long> readData(String key) {
        if (validateKeyAndPlayer(key)) {
            NBTTagCompound playerData = player.getEntityData();
            NBTTagCompound playerPersistedData = playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

            if (playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG) || playerPersistedData.hasKey(TAG_NAME)
                || playerPersistedData.getCompoundTag(TAG_NAME)
                    .hasKey(key)) {
                return Optional.of(
                    playerPersistedData.getCompoundTag(TAG_NAME)
                        .getLong(key));
            }
        }
        return Optional.of(0L);
    }

    @Override
    public void saveData(Long data, String key) {
        if (!validateKeyAndPlayer(key)) {
            return;
        }
        NBTTagCompound entityData = player.getEntityData();
        NBTTagCompound playerPersisted = player.getEntityData()
            .getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        // todo need optional for generic mb refactor
        Optional.of(data)
            .ifPresent(v -> {
                NBTTagCompound statData = playerPersisted.getCompoundTag(TAG_NAME);
                statData.setLong(key, v);
                playerPersisted.setTag(TAG_NAME, statData);
                entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, playerPersisted);
            });
    }

    @Override
    public void setContextObject(EntityPlayer obj) {
        player = obj;
    }

    @Override
    public EntityPlayer getContextObject() {
        return player;
    }

    private boolean validateKeyAndPlayer(String key) {
        return key != null && !key.isEmpty() && player != null;
    }
}
