package me.lightmax.advancebackpack.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClickCooldown {

    public HashMap<UUID, Double> cooldowns = new HashMap<>();

    public void setCooldowns(Player player, int seconds) {
        double delay = (System.currentTimeMillis() + (seconds * 1000));
        cooldowns.put(player.getUniqueId(), delay);
    }

    public boolean checkCooldown(Player player) {
        return !cooldowns.containsKey(player.getUniqueId()) || cooldowns.get(player.getUniqueId()) <= System.currentTimeMillis();
    }
}
