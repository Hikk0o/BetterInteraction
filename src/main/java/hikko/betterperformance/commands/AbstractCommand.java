package hikko.betterperformance.commands;

import hikko.betterperformance.BetterPerformance;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(String command) {
        PluginCommand pluginCommand = BetterPerformance.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }

    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();
        for (String arg : list) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }
        return result;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return filter(complete(sender, args), args);
    }
}
