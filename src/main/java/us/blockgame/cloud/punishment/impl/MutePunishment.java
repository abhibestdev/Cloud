package us.blockgame.cloud.punishment.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MutePunishment {

    @Getter
    private String reason;
    @Getter private long muteTime;
    @Getter private long duration;
}
