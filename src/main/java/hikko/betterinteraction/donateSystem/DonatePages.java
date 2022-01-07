package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class DonatePages {

    public void InitialDonateMenu(Player player) {
        Component menuTitle = Component.text("Donate Menu").decoration(ITALIC, false).color(TextColor.color(0x212121));
        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);
        ItemStack empty = new ItemStack(Material.AIR);
        ItemStack balance = new ItemStack(Material.RAW_GOLD_BLOCK);
        ItemStack donateList = new ItemStack(Material.NETHER_STAR);
        ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack closeMenu = new ItemStack(Material.RED_CONCRETE);

        ItemMeta balanceItemMeta = balance.getItemMeta();
        balanceItemMeta.displayName(Component.text("Баланс").decoration(ITALIC, false).color(TextColor.color(0x6DFE43)));
        List<Component> balanceLore = new ArrayList<>();
        balanceLore.add(Component.text("Вы имеете ").decoration(ITALIC, false).color(TextColor.color(0xF8F8F8))
                .append(Component.text((int) BetterInteraction.getInstance().getDonateDatabase().getDonate(player.getName())).color(TextColor.color(0xFFDB45)))
                .append(Component.text(" донат-поинтов.")));
        balanceItemMeta.lore(balanceLore);
        balance.setItemMeta(balanceItemMeta);

        ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
        closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
        closeMenu.setItemMeta(closeMenuItemMeta);

        ItemMeta donateListMeta = donateList.getItemMeta();
        donateListMeta.displayName(Component.text("Список услуг").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF)));
        List<Component> donateListLore = new ArrayList<>();
        donateListLore.add(Component.text("Перейти к списку услуг").decoration(ITALIC, false).color(TextColor.color(0x9C9C9C)));
        donateListMeta.lore(donateListLore);
        donateList.setItemMeta(donateListMeta);

        ItemStack[] items = {
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                menuGlass, balance, empty, empty, donateList, empty, empty, closeMenu, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);
    }

    public void DonateListMenu(Player player) {
        Component menuTitle = Component.text("Список услуг").decoration(ITALIC, false).color(TextColor.color(0x212121));

        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);

        ItemStack empty = new ItemStack(Material.AIR);
        ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack closeMenu = new ItemStack(Material.RED_CONCRETE);


        ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
        closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
        closeMenu.setItemMeta(closeMenuItemMeta);

        ItemStack[] items = {
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                menuGlass, empty,     empty,     empty,     empty,     empty,     empty,     closeMenu, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);

    }


    }
