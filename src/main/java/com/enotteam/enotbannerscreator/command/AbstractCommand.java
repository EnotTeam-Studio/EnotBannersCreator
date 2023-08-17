package com.enotteam.enotbannerscreator.command;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

public abstract class AbstractCommand implements CommandExecutor, TabExecutor {

    protected final String command;
    protected final String description;
    protected final List<String> alias;
    protected final String usage;
    protected final String permMessage;

    protected static CommandMap cmap;

    public AbstractCommand(String command) {
        this(command, null, null, null, null);
    }

    public AbstractCommand(String command, String usage) {
        this(command, usage, null, null, null);
    }

    public AbstractCommand(String command, String usage, String description) {
        this(command, usage, description, null, null);
    }

    public AbstractCommand(String command, String usage, String description, String permissionMessage) {
        this(command, usage, description, permissionMessage, null);
    }

    public AbstractCommand(String command, String usage, String description, List<String> aliases) {
        this(command, usage, description, null, aliases);
    }

    public AbstractCommand(String command, List<String> aliases) {
        this(command, null, null, aliases);
    }

    public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
        this.command = command.toLowerCase();
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }

    public void register() {
        ReflectCommand cmd = new ReflectCommand(this.command);
        if (this.alias != null) cmd.setAliases(this.alias);
        if (this.description != null) cmd.setDescription(this.description);
        if (this.usage != null) cmd.setUsage(this.usage);
        if (this.permMessage != null) cmd.setPermissionMessage(this.permMessage);
        getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }

    public void unregister() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            Field field2 = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field2.setAccessible(true);
            Map<String, Command> commandMap = (Map<String, Command>) field2.get(map);
            Command cmd = commandMap.get(command);
            commandMap.remove(cmd.getName());
            for(String tmp : cmd.getAliases()) {
                commandMap.remove(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (Exception e) { e.printStackTrace(); }
        } else { return cmap; }
        return getCommandMap();
    }

    private static final class ReflectCommand extends Command {
        private AbstractCommand exe = null;
        private ReflectCommand(String command) { super(command); }
        public void setExecutor(AbstractCommand exe) { this.exe = exe; }
        @Override public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (exe != null) { return exe.onCommand(sender, this, commandLabel, args); }
            return false;
        }

        @Override  public List<String> tabComplete(CommandSender sender, String alais, String[] args) {
            if (exe != null) { return exe.onTabComplete(sender, this, alais, args); }
            return null;
        }
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args);

}
