package com.enotteam.enotbannerscreator.command;

import com.enotteam.enotbannerscreator.common.Messages;
import com.enotteam.enotbannerscreator.menu.StaticMenus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainCommand extends AbstractCommand {

    boolean needPermission;
    String permission;

    public MainCommand(String command, List<String> aliases, boolean needPermission, String permission) {
        super(command, aliases);
        this.needPermission = needPermission;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Messages.send(sender, "on-console-command");
            return true;
        }

        Player player = (Player) sender;
        if (needPermission && !player.hasPermission(permission)) {
            Messages.send(sender, "no-perm");
            return true;
        }

        StaticMenus.getMainMenu().open(player);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

}
