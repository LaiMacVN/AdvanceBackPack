package me.lightmax.advancebackpack;

import net.md_5.bungee.api.ChatColor;

public class Utils {

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public int getTime(double time) {
        return (int) (System.currentTimeMillis() - time);
    }
    public boolean isInt(String s) {
        try{
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e) {
            //0ww0
        }
        return false;
    }
}
