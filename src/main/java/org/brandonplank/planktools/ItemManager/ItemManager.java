package org.brandonplank.planktools.ItemManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack Hammer;
    public static ItemStack DeathStick;

    public static void init(){
        createHammer();
        createDeathStick();
    }

    private static void createHammer(){
        ItemStack item = new ItemStack(Material.STONE_AXE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Ban hammer");
        List<String> Lore = new ArrayList<>();
        Lore.add("Admin ban hammer");
        meta.setLore(Lore);
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        Hammer = item;
    }

    private static void createDeathStick() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Death stick");
        List<String> Lore = new ArrayList<>();
        Lore.add("Admin owo killer");
        meta.setLore(Lore);
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        DeathStick = item;
    }
}
