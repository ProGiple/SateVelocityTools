package org.satellite.dev.progiple.satevetools.settings;

import lombok.Getter;
import net.elytrium.commons.kyori.serialization.Serializers;
import net.elytrium.serializer.SerializerConfig;
import net.elytrium.serializer.annotations.Comment;
import net.elytrium.serializer.annotations.CommentValue;
import net.elytrium.serializer.annotations.Transient;
import net.elytrium.serializer.language.object.YamlSerializable;

import java.util.List;

public class Settings extends YamlSerializable {
    @Transient
    private static final SerializerConfig serializerConfig = new SerializerConfig.Builder().build();

    @Getter
    @Transient
    private static Settings settings;
    public Settings() {
        super(serializerConfig);
        settings = this;
    }

    public Serializers SERIALIZER = Serializers.LEGACY_AMPERSAND;
    public String PREFIX = "&b[PRFX]&f";

    public Settings.COMMANDS COMMANDS = new COMMANDS();
    public static class COMMANDS {
        public Settings.COMMANDS.HUB HUB = new HUB();
        public static class HUB implements CommandSettings {
            public boolean ENABLED = true;
            public List<String> HUB_SERVERS = List.of("hub");
            public String[] ALIASES = new String[]{"lobby"};
            public String TELEPORT_MESSAGE = "[PRFX] Вы успешно переместились в хаб!";
        }

        @Comment({
                @CommentValue("satevetools.find - permission for use this commands"),
        })
        public Settings.COMMANDS.FIND FIND = new FIND();
        public static class FIND implements CommandSettings {
            public boolean ENABLED = true;
            public String[] ALIASES = new String[]{"search"};
            public List<String> TRANSLATES = List.of("hub-%-Хаб");
            public String SUCCESS_MESSAGE = "[PRFX] Игрок [player] находиться на сервере [server]";
            public String PLAYER_IS_OFFLINE = "[PRFX] Игрок [player] не найден!";
            public String IN_YOUR_SERVER = "[PRFX] Игрок [player] находиться на одном с вами сервере!";
            public String IS_YOU = "[PRFX] Это и так ты, думаю, ты знаешь где находишься :)";
        }

        @Comment({
                @CommentValue("satevetools.online - permission for use this commands"),
        })
        public Settings.COMMANDS.ONLINE ONLINE = new ONLINE();
        public static class ONLINE implements CommandSettings {
            public boolean ENABLED = true;
            public String SUCCESS_MESSAGE = "[PRFX] Игрок [player] онлайн!";
            public String PLAYER_IS_OFFLINE = "[PRFX] Игрок [player] оффлайн!";
            public String IS_YOU = "[PRFX] Это и так ты, и ты в сети :)";
        }
    }
}
