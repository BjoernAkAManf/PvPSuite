package tk.manf.pvpsuite.config;

import lombok.Getter;
import tk.manf.serialisation.SerialisationType;
import tk.manf.serialisation.annotations.Property;
import tk.manf.serialisation.annotations.Unit;

@Unit(name = "chat.yml", type = SerialisationType.FLATFILE_YAML)
public class ChatConfig {
    @Property
    @Getter
    private boolean anti7 = true;
}