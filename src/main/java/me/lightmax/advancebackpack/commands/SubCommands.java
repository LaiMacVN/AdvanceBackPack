package me.lightmax.advancebackpack.commands;

import org.bukkit.entity.Player;

public abstract class SubCommands {
    public abstract String getName();

    public abstract String getDesc();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract void perform(Player player, String args[]);
}
