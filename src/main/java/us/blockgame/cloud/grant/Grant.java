package us.blockgame.cloud.grant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import us.blockgame.cloud.rank.Rank;

import java.util.UUID;

@AllArgsConstructor
public class Grant {

    @Getter private long time;
    @Getter private long duration;
    @Getter private String reason;
    @Getter private Rank rank;
    @Getter private UUID uuid;
    @Getter private Rank executorRank;

}
