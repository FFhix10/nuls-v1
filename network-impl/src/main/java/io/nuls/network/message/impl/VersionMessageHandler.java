package io.nuls.network.message.impl;

import io.nuls.core.chain.entity.Block;
import io.nuls.core.constant.ErrorCode;
import io.nuls.core.context.NulsContext;
import io.nuls.network.entity.Peer;
import io.nuls.network.exception.NetworkMessageException;
import io.nuls.network.message.AbstractNetworkMessage;
import io.nuls.network.message.NetworkMessageResult;
import io.nuls.network.message.entity.GetVersionMessage;
import io.nuls.network.message.entity.VersionMessage;
import io.nuls.network.message.messageHandler.NetWorkMessageHandler;

import java.io.IOException;

/**
 * @author vivi
 * @date 2017/11/21
 */
public class VersionMessageHandler implements NetWorkMessageHandler {

    private static final VersionMessageHandler INSTANCE = new VersionMessageHandler();

    private VersionMessageHandler() {

    }

    public static VersionMessageHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public NetworkMessageResult process(AbstractNetworkMessage message, Peer peer) {
        VersionMessage versionMessage = (VersionMessage) message;
        if (versionMessage.getBestBlockHeight() < 0) {
            throw new NetworkMessageException(ErrorCode.NET_MESSAGE_ERROR);
        }
        peer.setVersionMessage(versionMessage);
        peer.setStatus(Peer.HANDSHAKE);
        return null;
    }
}
