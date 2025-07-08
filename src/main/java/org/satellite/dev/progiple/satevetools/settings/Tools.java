package org.satellite.dev.progiple.satevetools.settings;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.satellite.dev.progiple.satevetools.SateVeTools;

import java.util.List;
import java.util.Random;

@UtilityClass
public class Tools {
    @Getter
    private final Random random = new Random();

    public String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public @NonNull Component getMessage(String message) {
        Settings settings = Settings.getSettings();

        ComponentSerializer<Component, Component, String> serializer = settings.SERIALIZER.getSerializer();
        return serializer == null ? Component.text("") :
                serializer.deserialize(message.replace("[PRFX]", settings.PREFIX));
    }

    @SneakyThrows
    public void registerCommand(CommandSettings settings) {
        Class<?> settingsClass = settings.getClass();
        SateVeTools INSTANCE = SateVeTools.getINSTANCE();

        CommandManager manager = INSTANCE.getServer().getCommandManager();
        if ((boolean) settingsClass.getField("ENABLED").get(settings)) {
            String command = settingsClass.getSimpleName().replace(".class", "").toLowerCase();

            String className = "org.satellite.dev.progiple.satevetools.commands." + Tools.capitalizeFirst(command) + "Command";
            Class<?> clazz = Class.forName(className);

            String aliases = (String) settingsClass.getField("ALIASES").get(settings);
            CommandMeta.Builder builder = manager.metaBuilder(command).plugin(INSTANCE);

            if (aliases != null && !aliases.isEmpty()) builder = builder.aliases(aliases.split(", "));
            manager.register(builder.build(), (Command) clazz.getDeclaredConstructor().newInstance());
        }
    }
}
