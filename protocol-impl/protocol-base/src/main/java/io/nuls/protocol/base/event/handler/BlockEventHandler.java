/*
 * MIT License
 *
 * Copyright (c) 2017-2018 nuls.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.protocol.base.event.handler;

import io.nuls.poc.service.intf.ConsensusService;
import io.nuls.protocol.base.manager.BlockManager;
import io.nuls.protocol.base.download.DownloadCacheHandler;
import io.nuls.protocol.constant.DownloadStatus;
import io.nuls.protocol.entity.Consensus;
import io.nuls.protocol.event.BlockEvent;
import io.nuls.core.chain.entity.Block;
import io.nuls.core.context.NulsContext;
import io.nuls.core.utils.log.Log;
import io.nuls.event.bus.handler.AbstractEventHandler;
import io.nuls.network.service.NetworkService;
import io.nuls.protocol.intf.DownloadService;

/**
 * @author facjas
 * @date 2017/11/16
 */
public class BlockEventHandler extends AbstractEventHandler<BlockEvent> {

    private BlockManager blockCacheManager = BlockManager.getInstance();
    private NetworkService networkService = NulsContext.getServiceBean(NetworkService.class);
    private ConsensusService consensusService = NulsContext.getServiceBean(ConsensusService.class);
    private DownloadService downloadService = NulsContext.getServiceBean(DownloadService.class);

    @Override
    public void onEvent(BlockEvent event, String fromId) {
        Block block = event.getEventBody();
        if (null == block) {
            Log.warn("recieved a null blockEvent form " + fromId);
            return;
        }
        //BlockLog.debug("download("+fromId+") block height:" + block.getHeader().getHeight() + ", preHash:" + block.getHeader().getPreHash() + " , hash:" + block.getHeader().getHash() + ", address:" + block.getHeader().getPackingAddress());
//        if (BlockBatchDownloadUtils.getInstance().downloadedBlock(fromId, block)) {
//            return;
//        }

        //blockCacheManager.addBlock(block, true, fromId);

        if(downloadService.getStatus() != DownloadStatus.SUCCESS) {
            DownloadCacheHandler.receiveBlock(block);
        } else {
            consensusService.addBlock(block);
        }

    }
}
