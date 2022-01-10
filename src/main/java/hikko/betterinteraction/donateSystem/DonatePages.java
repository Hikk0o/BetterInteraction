package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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

        ItemMeta menuGlassMeta = menuGlass.getItemMeta();
        menuGlassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        menuGlassMeta.displayName(Component.text("Пусто..").color(TextColor.color(0x231924)));
        menuGlass.setItemMeta(menuGlassMeta);


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
        ItemStack sponsor = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta menuGlassMeta = menuGlass.getItemMeta();
        menuGlassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        menuGlassMeta.displayName(Component.text("Пусто..").color(TextColor.color(0x231924)));
        menuGlass.setItemMeta(menuGlassMeta);


        ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
        closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
        closeMenu.setItemMeta(closeMenuItemMeta);

        ItemMeta changeNicknameMeta = changeNickname.getItemMeta();
        changeNicknameMeta.displayName(Component.text("Цветной ник").decoration(ITALIC, false).color(TextColor.color(0xDACDFF)));
        List<Component> changeNicknameLore = new ArrayList<>();
        int changeNicknameCost = BetterInteraction.getInstance().getConfig().getInt("prices.coloredNickname.cost");
//        boolean changeNicknameSales = BetterInteraction.getInstance().getConfig().getBoolean("prices.coloredNickname.sales");
//        int changeNicknameSalesCosts = BetterInteraction.getInstance().getConfig().getInt("prices.coloredNickname.salesCosts");
        changeNicknameLore.add(Component.empty());
        changeNicknameLore.add(Component.text("Ваш ник будет выглядеть вот так: ").decoration(ITALIC, false).color(TextColor.color(0xCACACA))
                .append(Component.text(player.getName()).color(TextColor.color(0xAC9BFA))));
        changeNicknameLore.add(Component.text("(изменяется только в локальном и глобальном чате)").decoration(ITALIC, true).color(TextColor.color(0x9C9C9C)));
        changeNicknameLore.add(Component.text("Приобретается на 30 дней").decoration(ITALIC, false).color(TextColor.color(0xCACACA)));
        changeNicknameLore.add(Component.empty());
        changeNicknameLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0x78CB51))
                .append(Component.text(" "+changeNicknameCost+" ").color(TextColor.color(0xFFFFFF)))
                .append(Component.text("донат-коинов").color(TextColor.color(0x78CB51))));
        changeNicknameLore.add(Component.empty());
        changeNicknameLore.add(Component.text("Нажмите ").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text("ЛКМ").color(TextColor.color(0x78CB51)))
                .append(Component.text(", чтобы перейти к странице подтверждения").color(TextColor.color(0xFFFFFF))));

        changeNicknameMeta.lore(changeNicknameLore);
        changeNickname.setItemMeta(changeNicknameMeta);


        ItemMeta testDonate1Meta = testDonate1.getItemMeta();
        testDonate1Meta.displayName(Component.text("testDonate1").decoration(ITALIC, false).color(TextColor.color(0xF6FF3F)));
        testDonate1.setItemMeta(testDonate1Meta);

        ItemMeta sponsorMeta = sponsor.getItemMeta();
        sponsorMeta.displayName(Component.text("Спонсор").decoration(ITALIC, false).color(TextColor.color(0xFAD341)).decorate(TextDecoration.BOLD));
        sponsorMeta.addEnchant(Enchantment.LUCK, 1, false);
        sponsorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        int sponsorCost = BetterInteraction.getInstance().getConfig().getInt("prices.sponsor.cost");
//        boolean sponsorSales = BetterInteraction.getInstance().getConfig().getBoolean("prices.sponsor.sales");
//        int sponsorSalesCosts = BetterInteraction.getInstance().getConfig().getInt("prices.sponsor.salesCosts");

        List<Component> sponsorLore = new ArrayList<>();
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Выгодно, если хотите иметь все преимущества на этом сервере").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.text("Приобретается на 30 дней").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Вы получите: ").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.text("- Смена цвета ника в чате").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text("- Значок \"Спонсор\" в чате").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text("- Что-то ещё").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0x78CB51))
                .append(Component.text(" "+sponsorCost+" ").color(TextColor.color(0xFFFFFF)))
                .append(Component.text("донат-коинов").color(TextColor.color(0x78CB51))));
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Нажмите ").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text("ЛКМ").color(TextColor.color(0x78CB51)))
                .append(Component.text(", чтобы перейти к странице подтверждения").color(TextColor.color(0xFFFFFF))));

        sponsorMeta.lore(sponsorLore);

        sponsor.setItemMeta(sponsorMeta);

        ItemStack[] items = {
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                menuGlass, changeNickname, testDonate1, sponsor, empty, empty, empty, closeMenu, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);
    }

    public void ConfirmPage(Player player, String product) {
        Component menuTitle;
        if (product.equals("sponsor")) {
            menuTitle = Component.text("Товар: Спонсор").decoration(ITALIC, false);
        } else if (product.equals("coloredNickname")) {
            menuTitle = Component.text("Товар: Цветной ник").decoration(ITALIC, false);
        } else {
            player.closeInventory();
            return;
        }

        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);
        ItemStack empty = new ItemStack(Material.AIR);
        ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack confirmButton = new ItemStack(Material.LIME_WOOL);
        ItemStack unconfirmedButton = new ItemStack(Material.RED_WOOL);

        ItemMeta menuGlassMeta = menuGlass.getItemMeta();
        menuGlassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        menuGlassMeta.displayName(Component.text("Пусто..").color(TextColor.color(0x231924)));
        menuGlass.setItemMeta(menuGlassMeta);

        ItemMeta confirmButtonMeta = confirmButton.getItemMeta();
        confirmButtonMeta.displayName(Component.text("Подтвердить покупку").decoration(ITALIC, false).color(TextColor.color(0x4FE64F)));
        confirmButton.setItemMeta(confirmButtonMeta);

        ItemMeta unconfirmedButtonMeta = unconfirmedButton.getItemMeta();
        unconfirmedButtonMeta.displayName(Component.text("Отклонить покупку").decoration(ITALIC, false).color(TextColor.color(0xFF504A)));
        unconfirmedButton.setItemMeta(unconfirmedButtonMeta);


        ItemStack[] items = {
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                menuGlass, empty, empty, confirmButton, empty, unconfirmedButton, empty, empty, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);
    }

}
