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

package io.nuls.protocol.base.service.impl;

import io.nuls.protocol.intf.DownloadService;
import io.nuls.protocol.intf.SystemService;
import io.nuls.protocol.base.thread.ConsensusMeetingRunner;
import io.nuls.core.context.NulsContext;
import io.nuls.core.utils.log.Log;
import io.nuls.network.service.NetworkService;

/**
 * Created by ln on 2018/4/11.
 */
public class SystemServiceImpl implements SystemService {

    /**
     * 重置系统，包括重置网络、同步、共识
     * Reset the system, including resetting the network, synchronization, consensus
     * @param reason
     * @return boolean
     */
    @Override
    public boolean resetSystem(String reason) {

        Log.info("---------------reset start----------------");
        Log.info("Received a reset system request, reason: 【" + reason + "】");

        NetworkService networkService = NulsContext.getServiceBean(NetworkService.class);
        networkService.reset();
//        List<Node> nodeList = networkService.getAvailableNodes();
//        for(Node node:nodeList){
//            networkService.removeNode(node.getId());
//        }

        DownloadService downloadService = NulsContext.getServiceBean(DownloadService.class);
        downloadService.reset();

        ConsensusMeetingRunner consensusMeetingRunner = ConsensusMeetingRunner.getInstance();
        consensusMeetingRunner.resetConsensus();

        Log.info("---------------reset end----------------");

        return true;
    }
}
