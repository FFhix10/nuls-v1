/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2017-2019 nuls.io
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package io.nuls.consensus.poc.module;

import io.nuls.consensus.poc.BaseTest;
import io.nuls.consensus.poc.module.impl.PocConsensusModuleBootstrap;
import io.nuls.kernel.MicroKernelBootstrap;
import org.junit.Test;

/**
 * Created by ln on 2018/5/7.
 */
public class PocConsensusModuleBootstrapTest extends BaseTest {

    private PocConsensusModuleBootstrap bootstrap = new PocConsensusModuleBootstrap();

    @Test
    public void testStartModule() throws Exception {

        MicroKernelBootstrap mk = MicroKernelBootstrap.getInstance();
        mk.init();
        mk.start();

        bootstrap.init();
        bootstrap.start();
    }
}