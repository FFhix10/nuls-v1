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
 *
 */

package io.nuls.network.util;

import io.nuls.core.tools.date.DateUtil;
import io.nuls.core.tools.log.Log;
import io.nuls.kernel.func.TimeService;
import io.nuls.kernel.thread.manager.TaskManager;
import io.nuls.network.constant.NetworkConstant;
import io.nuls.network.manager.NodeManager;

/**
 * @author: Niels Wang
 * @date: 2018/7/10
 */
public class SendNodeInfoThread implements Runnable {

    boolean running = false;

    private static SendNodeInfoThread instance = new SendNodeInfoThread();

    private NodeManager nodeManager = NodeManager.getInstance();

    private SendNodeInfoThread() {

    }

    public static SendNodeInfoThread getInstance() {
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                nodeManager.broadNodeSever();
                Thread.sleep(10 * DateUtil.MINUTE_TIME);
            } catch (Throwable e) {
                Log.error(e);
            }
        }
    }

    public void start() {
        if (!running) {
            running = true;
            TaskManager.createAndRunThread(NetworkConstant.NETWORK_MODULE_ID, "send-node-info", this);
        }
    }

}
