package com.rtm516.mcxboxbroadcast.core.nethernet;

import com.rtm516.mcxboxbroadcast.core.Logger;
import com.rtm516.mcxboxbroadcast.core.SessionInfo;
import com.rtm516.mcxboxbroadcast.core.SessionManagerCore;
import com.rtm516.mcxboxbroadcast.core.nethernet.initializer.NetherNetBedrockChannelInitializer;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;

import java.util.concurrent.TimeUnit;

public class BroadcasterChannelInitializer extends NetherNetBedrockChannelInitializer<BedrockServerSession> {

    private final SessionInfo sessionInfo;
    private final SessionManagerCore sessionManager;
    private final Logger logger;

    public BroadcasterChannelInitializer(SessionInfo sessionInfo, SessionManagerCore sessionManager, Logger logger) {
        this.sessionInfo = sessionInfo;
        this.sessionManager = sessionManager;
        this.logger = logger;
    }

    @Override
    protected BedrockServerSession createSession0(BedrockPeer peer, int subClientId) {
        return new BedrockServerSession(peer, subClientId);
    }

    @Override
    protected void initSession(BedrockServerSession session) {
        session.setLogging(true);
        RedirectPacketHandler handler = new RedirectPacketHandler(session, sessionInfo, sessionManager, logger);
        session.setPacketHandler(handler);
        sessionManager.scheduledThread().schedule(() -> {
            if (!handler.hasCompletedTransfer()) {
                logger.warn("NetherNet join did not complete; last handshake stage: " + handler.handshakeStage());
                sessionManager.recoverStalledJoin();
            }
        }, 32, TimeUnit.SECONDS);
    }
}
