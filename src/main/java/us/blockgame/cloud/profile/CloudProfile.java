package us.blockgame.cloud.profile;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.permissions.PermissionAttachment;
import us.blockgame.cloud.grant.Grant;
import us.blockgame.cloud.punishment.impl.MutePunishment;
import us.blockgame.cloud.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudProfile {

    @Setter @Getter private Rank rank = Rank.DEFAULT;
    @Setter @Getter private PermissionAttachment permissionAttachment;
    @Setter @Getter private List<Grant> grantList = new ArrayList<>();
    @Setter @Getter private List<UUID> ignored = new ArrayList<>();
    @Setter @Getter private long loginTime;
    @Setter @Getter private List<String> ips = new ArrayList<>();
    @Setter @Getter private MutePunishment mutePunishment;
    @Setter @Getter private boolean privateMessaging = true;
    @Setter @Getter private UUID lastMessage;
    @Setter @Getter private boolean sounds = true;
}
