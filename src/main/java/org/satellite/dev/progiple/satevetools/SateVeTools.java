package org.satellite.dev.progiple.satevetools;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.satellite.dev.progiple.satevetools.settings.Settings;
import org.satellite.dev.progiple.satevetools.settings.Tools;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "satevetools",
        name = "SateVeTools",
        version = "1.0.1",
        authors = "ProGiple"
) @Getter
public class SateVeTools {
    @Getter private static SateVeTools INSTANCE;

    @Inject
    private Logger logger;
    private final ProxyServer server;
    private final Path dataDirectory;

    @Inject
    public SateVeTools(Logger logger, ProxyServer server, @DataDirectory Path dataDirectory) {
        INSTANCE = this;
        this.logger = logger;
        this.server = server;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Settings settings = new Settings();
        settings.reload(Path.of("plugins/SateVeTools/config.yml"));

        Tools.registerCommand(settings.COMMANDS.HUB);
        Tools.registerCommand(settings.COMMANDS.FIND);
        Tools.registerCommand(settings.COMMANDS.ONLINE);
    }
}
