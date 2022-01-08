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
        ItemStack changeNickname = new ItemStack(Material.PAPER);
        ItemStack testDonate1 = new ItemStack(Material.PAPER);
        ItemStack testDonate2 = new ItemStack(Material.PAPER);


        ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
        closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
        closeMenu.setItemMeta(closeMenuItemMeta);

        ItemMeta changeNicknameMeta = changeNickname.getItemMeta();
        changeNicknameMeta.displayName(Component.text("Сменить цвет ника в чате").decoration(ITALIC, false).color(TextColor.color(0x34FF30)));
        List<Component> changeNicknameLore = new ArrayList<>();
        changeNicknameLore.add(Component.empty());
        changeNicknameLore.add(Component.text("Выбери цвет для ника на 30 дней! ").decoration(ITALIC, false).color(TextColor.color(0x9C9C9C)));
        changeNicknameLore.add(Component.text("(изменяется только в локальном и глобальном чате)").decoration(ITALIC, true).color(TextColor.color(0x9C9C9C)));
        changeNicknameLore.add(Component.empty());
        changeNicknameLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text(" 100 ").color(TextColor.color(0xFFDB45)))
                .append(Component.text("донат-коинов.")));


        changeNicknameMeta.lore(changeNicknameLore);
        changeNickname.setItemMeta(changeNicknameMeta);

        ItemMeta testDonate1Meta = testDonate1.getItemMeta();
        testDonate1Meta.displayName(Component.text("testDonate1").decoration(ITALIC, false).color(TextColor.color(0xF6FF3F)));
        testDonate1.setItemMeta(testDonate1Meta);

        ItemMeta testDonate2Meta = testDonate2.getItemMeta();
        testDonate2Meta.displayName(Component.text("testDonate2").decoration(ITALIC, false).color(TextColor.color(0xF6FF3F)));
        testDonate2.setItemMeta(testDonate2Meta);


        ItemStack[] items = {
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                menuGlass, changeNickname, testDonate1, testDonate2, empty, empty, empty, closeMenu, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);

    }


    }
