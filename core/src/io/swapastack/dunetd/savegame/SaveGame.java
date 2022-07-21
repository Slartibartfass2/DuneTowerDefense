package io.swapastack.dunetd.savegame;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class SaveGame {

    @Getter
    private final String name;
    @Getter
    private final long timestamp;

    public SaveGame(@NonNull String name, long timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }
}