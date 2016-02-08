/*
 * This file is part of GriefPrevention, licensed under the MIT License (MIT).
 *
 * Copyright (c) Ryan Hamshire
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.ryanhamshire.GriefPrevention;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;

public class NbtDataHelper {

    public static final String FORGE_DATA = "ForgeData";
    public static final String SPONGE_DATA = "SpongeData";
    public static final String SPONGE_ENTITY_CREATOR = "Creator";

    public static Optional<User> getOwnerOfEntity(net.minecraft.entity.Entity entity) {
        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);
        if (nbt.hasKey(FORGE_DATA)) {
            NBTTagCompound forgeNBT = nbt.getCompoundTag(FORGE_DATA);
            if (forgeNBT.hasKey(SPONGE_DATA) && forgeNBT.getCompoundTag(SPONGE_DATA).hasKey(SPONGE_ENTITY_CREATOR)) {
                NBTTagCompound creatorNBT = forgeNBT.getCompoundTag(SPONGE_DATA).getCompoundTag(SPONGE_ENTITY_CREATOR);
                UUID uuid = new UUID(creatorNBT.getLong("uuid_most"), creatorNBT.getLong("uuid_least"));
                // get player if online
                EntityPlayer player = entity.worldObj.getPlayerEntityByUUID(uuid);
                if (player != null) {
                    return Optional.of((User) player);
                }
                // player is not online, get user from storage if one exists
                return Sponge.getGame().getServiceManager().provide(UserStorageService.class).get().get(uuid);
            }
        }
        return Optional.empty();
    }
}
