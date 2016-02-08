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

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.permission.Subject;

import java.util.Set;
import java.util.UUID;

public class ClaimContextCalculator implements ContextCalculator<Subject> {

    @Override
    public void accumulateContexts(Subject calculable, Set<Context> accumulator) {
        if (calculable.getCommandSource().isPresent() && calculable.getCommandSource().get() instanceof Player) {
            Player player = (Player) calculable.getCommandSource().get();
            Claim sourceClaim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
            if (sourceClaim != null) {
                accumulator.add(new Context("claim", sourceClaim.getID().toString()));
            }
        }

    }

    @Override
    public boolean matches(Context context, Subject subject) {
        if (context.equals("claim")) {
            if (subject.getCommandSource().isPresent() && subject.getCommandSource().get() instanceof Player) {
                Player player = (Player) subject.getCommandSource().get();
                Claim playerClaim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
                if (playerClaim != null && playerClaim.getID().equals(UUID.fromString(context.getValue()))) {
                    return true;
                }
            }
        }

        return false;
    }
}
