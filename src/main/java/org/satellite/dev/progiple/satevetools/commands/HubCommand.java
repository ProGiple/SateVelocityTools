package org.satellite.dev.progiple.satevetools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.satellite.dev.progiple.satevetools.SateVeTools;
import org.satellite.dev.progiple.satevetools.settings.Settings;
import org.satellite.dev.progiple.satevetools.settings.Tools;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HubCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        List<String> hubs = Settings.getSettings().COMMANDS.HUB.HUB_SERVERS;
        if (hubs == null || hubs.isEmpty()) return;

        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) return;

        String[] args = invocation.arguments();
        String stringHub = args.length >= 1 ? args[0] : hubs.get(Tools.getRandom().nextInt(hubs.size()));

        if (!hubs.contains(stringHub)) stringHub = hubs.get(Tools.getRandom().nextInt(hubs.size()));
        SateVeTools.getINSTANCE().getServer().getServer(stringHub).ifPresent(s -> {
            player.createConnectionRequest(s).connect().thenAccept(r -> {
                if (r.isSuccessful()) player.sendMessage(Tools.getMessage(Settings.getSettings().COMMANDS.HUB.TELEPORT_MESSAGE));
            });
        });
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(invocation.arguments().length == 1 ?
                Settings.getSettings().COMMANDS.HUB.HUB_SERVERS
                        .stream()
                        .filter(s -> s.toLowerCase().startsWith(invocation.arguments()[0].toLowerCase()))
                        .toList() : List.of());
    }
}
