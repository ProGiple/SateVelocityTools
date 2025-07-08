package org.satellite.dev.progiple.satevetools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import org.satellite.dev.progiple.satevetools.SateVeTools;
import org.satellite.dev.progiple.satevetools.settings.Settings;
import org.satellite.dev.progiple.satevetools.settings.Tools;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FindCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length < 1) return;

        CommandSource source = invocation.source();
        Player player = SateVeTools.getINSTANCE().getServer().getPlayer(args[0]).orElse(null);

        Component errorMessage = Tools.getMessage(Settings.getSettings().COMMANDS.FIND.PLAYER_IS_OFFLINE
                .replace("[player]", player != null ? player.getUsername() : args[0]));
        if (player == null) {
            source.sendMessage(errorMessage);
            return;
        }

        ServerConnection connection = player.getCurrentServer().orElse(null);
        if (connection == null) {
            source.sendMessage(errorMessage);
            return;
        }

        if (source.equals(player)) {
            source.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.FIND.IS_YOU));
            return;
        }

        RegisteredServer server = connection.getServer();
        if (source instanceof Player usedPlayer) {
            ServerConnection usedConnection = usedPlayer.getCurrentServer().orElse(null);
            if (usedConnection != null && usedConnection.getServer().equals(server)) {
                usedPlayer.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.FIND.IN_YOUR_SERVER
                        .replace("[player]", player.getUsername())));
                return;
            }
        }

        String defaultName = server.getServerInfo().getName();
        String translate = Settings.getSettings().COMMANDS.FIND.TRANSLATES
                .stream()
                .map(t -> t.split("-%-"))
                .filter(s -> s.length == 2 && s[0].equals(defaultName))
                .map(s -> s[1])
                .findAny()
                .orElse(defaultName);
        if (translate == null || translate.isEmpty()) translate = defaultName;

        source.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.FIND.SUCCESS_MESSAGE
                .replace("[player]", player.getUsername())
                .replace("[server]", translate)));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(
                SateVeTools.getINSTANCE().getServer().getAllPlayers()
                        .stream()
                        .map(Player::getUsername)
                        .filter(u -> invocation.arguments().length == 0 ||
                                u.toLowerCase().startsWith(invocation.arguments()[0].toLowerCase()))
                        .toList());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("satevetools.find");
    }
}
