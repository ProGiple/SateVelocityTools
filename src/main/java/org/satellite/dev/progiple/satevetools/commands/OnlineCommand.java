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

public class OnlineCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length < 1) return;

        CommandSource source = invocation.source();
        Player player = SateVeTools.getINSTANCE().getServer().getPlayer(args[0]).orElse(null);

        Component errorMessage = Tools.getMessage(Settings.getSettings().COMMANDS.ONLINE.PLAYER_IS_OFFLINE
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
            source.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.ONLINE.IS_YOU));
            return;
        }

        source.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.ONLINE.SUCCESS_MESSAGE
                .replace("[player]", player.getUsername())));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(invocation.arguments().length == 1 ?
                SateVeTools.getINSTANCE().getServer().getAllPlayers()
                        .stream()
                        .map(Player::getUsername)
                        .filter(u -> u.toLowerCase().startsWith(invocation.arguments()[0].toLowerCase()))
                        .toList() : List.of());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("satevetools.online");
    }
}
