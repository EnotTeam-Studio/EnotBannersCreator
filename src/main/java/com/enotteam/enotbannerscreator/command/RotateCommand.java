package com.enotteam.enotbannerscreator.command;

import com.enotteam.enotbannerscreator.banner.rotating.BannerRotateManager;
import com.enotteam.enotbannerscreator.common.Messages;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RotateCommand extends AbstractCommand {

    BannerRotateManager bannerRotateManager;
    String permission;

    public RotateCommand(String command, List<String> aliases, BannerRotateManager bannerRotateManager, String permission) {
        super(command, aliases);
        this.bannerRotateManager = bannerRotateManager;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Messages.send(sender, "on-console-command");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(permission)) {
            Messages.send(player, "no-perm");
            return true;
        }

        Block target = player.getTargetBlock(Set.of(Material.AIR), 4);

        if (!(target.getState() instanceof Banner)) {
            Messages.send(player, "rotate-block-is-not-banner");
            return true;
        }

        if (!bannerRotateManager.canCreateNewRotatableBanner(player)) {
            Messages.send(player, "rotate-max-amount");
            return true;
        }

        bannerRotateManager.add(player.getName(), target.getLocation());
        Messages.send(player, "rotate-added");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

}
