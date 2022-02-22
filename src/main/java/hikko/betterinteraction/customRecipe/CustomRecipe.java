package hikko.betterinteraction.customRecipe;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class CustomRecipe implements Listener {
    ItemStack enchantedBook_10lvl;
    ItemStack enchantedBook_7lvl;



    public CustomRecipe() {
        //Craft 10lvl
        enchantedBook_10lvl = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchantedBookMeta_10lvl = (EnchantmentStorageMeta)enchantedBook_10lvl.getItemMeta();
        enchantedBookMeta_10lvl.addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true);
        enchantedBook_10lvl.setItemMeta(enchantedBookMeta_10lvl);

        ItemStack ingredient_7lvl = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta ingredientMeta_7lvl = (EnchantmentStorageMeta)ingredient_7lvl.getItemMeta();
        ingredientMeta_7lvl.addStoredEnchant(Enchantment.DAMAGE_ALL, 7, true);
        ingredient_7lvl.setItemMeta(ingredientMeta_7lvl);

        NamespacedKey key_10lvl = new NamespacedKey(BetterInteraction.getInstance(), "enchant_book_damage_10");
        ShapedRecipe recipe_10lvl = new ShapedRecipe(key_10lvl, enchantedBook_10lvl);

        recipe_10lvl.shape(
                " N ",
                "NSN",
                " N "
        );

        recipe_10lvl.setIngredient('S', ingredient_7lvl);
        recipe_10lvl.setIngredient('N', Material.NETHERITE_INGOT);
        BetterInteraction.getInstance().getServer().addRecipe(recipe_10lvl);

        //Craft 7lvl
        enchantedBook_7lvl = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchantedBookMeta_7lvl = (EnchantmentStorageMeta)enchantedBook_7lvl.getItemMeta();
        enchantedBookMeta_7lvl.addStoredEnchant(Enchantment.DAMAGE_ALL, 7, true);
        enchantedBook_7lvl.setItemMeta(enchantedBookMeta_7lvl);

        ItemStack ingredient_5lvl = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta ingredientMeta_5lvl = (EnchantmentStorageMeta)ingredient_5lvl.getItemMeta();
        ingredientMeta_5lvl.addStoredEnchant(Enchantment.DAMAGE_ALL, 5, false);
        ingredient_5lvl.setItemMeta(ingredientMeta_5lvl);

        NamespacedKey key_7lvl = new NamespacedKey(BetterInteraction.getInstance(), "enchant_book_damage_7");
        ShapedRecipe recipe_7lvl = new ShapedRecipe(key_7lvl, enchantedBook_7lvl);

        recipe_7lvl.shape(
                " N ",
                "NSN",
                " N "
        );
        recipe_7lvl.setIngredient('S', ingredient_5lvl);
        recipe_7lvl.setIngredient('N', Material.NETHERITE_INGOT);
        BetterInteraction.getInstance().getServer().addRecipe(recipe_7lvl);

    }


    @EventHandler
    public void PrepareAnvilEvent(PrepareAnvilEvent e) {
        if (e.getInventory().getFirstItem() == null || e.getInventory().getSecondItem() == null) return;
        if (e.getInventory().getFirstItem().getType() == Material.NETHERITE_SWORD) {
            ItemStack resultItem = e.getInventory().getFirstItem().clone();
            ItemMeta resultItemMeta = resultItem.getItemMeta();

            if (Objects.equals(e.getInventory().getSecondItem(), enchantedBook_10lvl)) {

                resultItemMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                resultItem.setItemMeta(resultItemMeta);
                e.setResult(resultItem);
            } else if (Objects.equals(e.getInventory().getSecondItem(), enchantedBook_7lvl)) {
                resultItemMeta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
                resultItem.setItemMeta(resultItemMeta);
                e.setResult(resultItem);
            }

        } else if (Objects.equals(e.getInventory().getSecondItem(), enchantedBook_10lvl) || Objects.equals(e.getInventory().getSecondItem(), enchantedBook_7lvl)) {
            e.setResult(new ItemStack(Material.AIR));
        }
    }
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) e.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().equals(Material.NETHERITE_SWORD)) {
                ItemMeta itemInHandMeta = itemInHand.getItemMeta();
                if (itemInHandMeta.hasEnchant(Enchantment.DAMAGE_ALL) && !e.getEntity().getType().equals(EntityType.PLAYER)) {
                    int levelEnchant = itemInHandMeta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                    if (levelEnchant == 7) {
                        e.setDamage(e.getDamage()*1.4);
                    }
                    if (levelEnchant == 10) {
                        e.setDamage(e.getDamage()*2);
                    }
                }
            }
        }
    }
}
