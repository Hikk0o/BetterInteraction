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
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class DonatePages {

    public void InitialDonateMenu(Player player) {
        Component menuTitle = Component.text("Donate Menu").decoration(ITALIC, false).color(TextColor.color(0x212121));
        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);
        ItemStack empty = new ItemStack(Material.AIR);
        ItemStack balance = new ItemStack(Material.EMERALD);
        ItemStack donateList = new ItemStack(Material.NETHER_STAR);
        ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack closeMenu = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack about = new ItemStack(Material.BOOK);

        ItemMeta aboutMeta = about.getItemMeta();
        aboutMeta.displayName(Component.text("Подробнее про пожертования").decoration(ITALIC, false).color(TextColor.color(0xF8F8F8)));
        about.setItemMeta(aboutMeta);

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
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, closeMenu,
                menuGlass, balance, empty, empty, donateList, empty, empty, about, menuGlass,
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
        ItemStack closeMenu = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack coloredNickname = new ItemStack(Material.PAPER);
        ItemStack addPower = new ItemStack(Material.GRASS_BLOCK);
        ItemStack particleHats = new ItemStack(Material.ENCHANTED_BOOK);
        ItemStack sponsor = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta menuGlassMeta = menuGlass.getItemMeta();
        menuGlassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        menuGlassMeta.displayName(Component.text("Пусто..").color(TextColor.color(0x231924)));
        menuGlass.setItemMeta(menuGlassMeta);


        ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
        closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
        closeMenu.setItemMeta(closeMenuItemMeta);

        // Colored nickname
        ItemMeta coloredNicknameMeta = coloredNickname.getItemMeta();
        coloredNicknameMeta.displayName(Component.text("Цветной ник").decoration(ITALIC, false).color(TextColor.color(0xDACDFF)));
        List<Component> coloredNicknameLore = new ArrayList<>();

        int coloredNicknameCost = BetterInteraction.getInstance().getConfig().getInt("prices.coloredNickname.cost");

        coloredNicknameLore.add(Component.empty());
        coloredNicknameLore.add(Component.text("Ваш ник будет выглядеть вот так: ").decoration(ITALIC, false).color(TextColor.color(0xCACACA))
                .append(Component.text(player.getName()).color(TextColor.color(0x9CA2F0))));
        coloredNicknameLore.add(Component.text("(изменяется только в локальном и глобальном чате)").decoration(ITALIC, true).color(TextColor.color(0x9C9C9C)));
        coloredNicknameLore.add(Component.text("Приобретается на 30 дней").decoration(ITALIC, false).color(TextColor.color(0xCACACA)));
        coloredNicknameLore.add(Component.empty());
        coloredNicknameLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0x78CB51))
                .append(Component.text(" "+coloredNicknameCost+" ").color(TextColor.color(0xFFFFFF)))
                .append(Component.text("донат-коинов").color(TextColor.color(0x78CB51))));
        coloredNicknameLore.add(Component.empty());
        coloredNicknameLore.add(Component.text("Нажмите ").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text("ЛКМ").color(TextColor.color(0x78CB51)))
                .append(Component.text(", чтобы перейти к странице подтверждения").color(TextColor.color(0xFFFFFF))));

        coloredNicknameMeta.lore(coloredNicknameLore);
        coloredNickname.setItemMeta(coloredNicknameMeta);

        // Add power
        int addPowerCost = BetterInteraction.getInstance().getConfig().getInt("prices.addPower.cost");

        ItemMeta addPowerMeta = addPower.getItemMeta();
        addPowerMeta.displayName(Component.text("+50 силы").decoration(ITALIC, false).color(TextColor.color(0x3EDFC4)));
        List<Component> addPowerLore = new ArrayList<>();
        addPowerLore.add(Component.empty());
        addPowerLore.add(Component.text("Покупая данный товар, вы прибавляете +50 силы для СЕБЯ").decoration(ITALIC, false).color(TextColor.color(0xA1FAE6)));
        addPowerLore.add(Component.text("Сила позволяет захватывать больше земель").decoration(ITALIC, false).color(TextColor.color(0xA1FAE6)));
        addPowerLore.add(Component.text("(Можно сразу использовать)").decoration(ITALIC, true).color(TextColor.color(0xA1FAE6)));
        addPowerLore.add(Component.empty());
        addPowerLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0x78CB51))
                .append(Component.text(" "+addPowerCost+" ").color(TextColor.color(0xFFFFFF)))
                .append(Component.text("донат-коинов").color(TextColor.color(0x78CB51))));
        addPowerLore.add(Component.empty());
        addPowerLore.add(Component.text("Нажмите ").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text("ЛКМ").color(TextColor.color(0x78CB51)))
                .append(Component.text(", чтобы перейти к странице подтверждения").color(TextColor.color(0xFFFFFF))));
        addPowerMeta.lore(addPowerLore);

        addPower.setItemMeta(addPowerMeta);

        // Particle Menu
        int particleHatsCost = BetterInteraction.getInstance().getConfig().getInt("prices.particleHats.cost");

        ItemMeta particleHatsMeta = particleHats.getItemMeta();
        particleHatsMeta.displayName(Component.text("Меню эффектов").decoration(ITALIC, false).color(TextColor.color(0xAA66F4)));
        List<Component> particleHatsLore = new ArrayList<>();
        particleHatsLore.add(Component.empty());
        particleHatsLore.add(Component.text("Покупая данный товар, вы получаете доступ к меню эффектов").decoration(ITALIC, false).color(TextColor.color(0xAB95D8)));
        particleHatsLore.add(Component.text("(Команда /h)").decoration(ITALIC, false).color(TextColor.color(0xAB95D8)));
        particleHatsLore.add(Component.text("Приобретается на 30 дней").decoration(ITALIC, false).color(TextColor.color(0xAB95D8)));
        particleHatsLore.add(Component.empty());
        particleHatsLore.add(Component.text("Меню содержит в себе 4 эффекта:").decoration(ITALIC, false).color(TextColor.color(0xAB95D8)));
        particleHatsLore.add(Component.text(" - Эффект \"Разноцветный ореол из редстоуна\"").decoration(ITALIC, false).color(TextColor.color(0xBEB8D8)));
        particleHatsLore.add(Component.text(" - Эффект \"Сверкающий плащ\"").decoration(ITALIC, false).color(TextColor.color(0xBEB8D8)));
        particleHatsLore.add(Component.text(" - Эффект \"Пламбоб\"").decoration(ITALIC, false).color(TextColor.color(0xBEB8D8)));
        particleHatsLore.add(Component.text(" - Эффект \"Магическая аура\"").decoration(ITALIC, false).color(TextColor.color(0xBEB8D8)));
        particleHatsLore.add(Component.empty());
        particleHatsLore.add(Component.text("Стоимость:").decoration(ITALIC, false).color(TextColor.color(0x78CB51))
                .append(Component.text(" "+particleHatsCost+" ").color(TextColor.color(0xFFFFFF)))
                .append(Component.text("донат-коинов").color(TextColor.color(0x78CB51))));
        particleHatsLore.add(Component.empty());
        particleHatsLore.add(Component.text("Нажмите ").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF))
                .append(Component.text("ЛКМ").color(TextColor.color(0x78CB51)))
                .append(Component.text(", чтобы перейти к странице подтверждения").color(TextColor.color(0xFFFFFF))));
        particleHatsMeta.lore(particleHatsLore);

        particleHats.setItemMeta(particleHatsMeta);

        // Sponsor
        ItemMeta sponsorMeta = sponsor.getItemMeta();
        sponsorMeta.displayName(Component.text("Спонсор").decoration(ITALIC, false).color(TextColor.color(0xFAD341)).decorate(TextDecoration.BOLD));
        sponsorMeta.addEnchant(Enchantment.LUCK, 1, false);
        sponsorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        int sponsorCost = BetterInteraction.getInstance().getConfig().getInt("prices.sponsor.cost");

        List<Component> sponsorLore = new ArrayList<>();
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Полный комплект плюшек из магазина").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.text("Приобретается на 30 дней").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.empty());
        sponsorLore.add(Component.text("Вы получите: ").decoration(ITALIC, false).color(TextColor.color(0xFAEB67)));
        sponsorLore.add(Component.text(" - Цветной ник в чате").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text(" - Значок \"Спонсор\" в чате").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text(" - Доступ к меню эффектов").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text(" - +50 силы").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text(" - Уважение от Hikko").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7)));
        sponsorLore.add(Component.text("Ваш ник будет выглядеть вот так: ").decoration(ITALIC, false).color(TextColor.color(0xD7D7D7))
                .append(Component.text("✦ ").color(TextColor.color(0xC580FF)))
                .append(Component.text(player.getName()).color(TextColor.color(0xAC8BFF))));
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
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, closeMenu,
                menuGlass, coloredNickname, addPower, particleHats, sponsor, empty, empty, empty, menuGlass,
                menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
        };
        inv.setContents(items);

        player.openInventory(inv);
    }

    public void ConfirmPage(Player player, String product) {
        Component menuTitle;
        switch (product) {
            case "sponsor":
                menuTitle = Component.text("Товар: Спонсор").decoration(ITALIC, false);
                break;
            case "coloredNickname":
                menuTitle = Component.text("Товар: Цветной ник").decoration(ITALIC, false);
                break;
            case "addPower":
                menuTitle = Component.text("Товар: +50 силы").decoration(ITALIC, false);
                break;
            case "particleMenu":
                menuTitle = Component.text("Товар: Меню эффектов").decoration(ITALIC, false);
                break;
            default:
                player.closeInventory();
                return;
        }

        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);
        ItemStack empty = new ItemStack(Material.AIR);
        ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack confirmButton = new ItemStack(Material.LIME_CONCRETE);
        ItemStack unconfirmedButton = new ItemStack(Material.RED_CONCRETE);

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

    public void aboutDonate(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK); //Create book ItemStack
        BookMeta meta = (BookMeta)book.getItemMeta(); //Get BookMeta
        meta.setTitle("Litwein SMP");
        meta.addPages(net.kyori.adventure.text.Component.text(
                "Пожертвования существуют исключительно для тех, кто желает поддержать сервер и получить что-то взамен.\n" +
                        "Внеся копеечку, Вы не получите большое преимущество перед другими игроками, но и не останетесь без плюшек.\n")); //Add a page
        meta.addPages(net.kyori.adventure.text.Component.text(
                "Донат - вынужденная мера для того, чтобы сервер жил и развивался.\n" +
                        "Надеюсь на ваше понимание ❤")); //Add a page
        meta = meta.author(Component.text("Litwein SMP"));
        book.setItemMeta(meta); //Set meta

        player.openBook(book);
    }
}
