/*
 * MIT License
 *
 * Copyright (c) 2017-2019 nuls.io
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

package io.nuls.consensus.poc.tx.validator;

import io.nuls.consensus.poc.protocol.constant.PocConsensusErrorCode;
import io.nuls.consensus.poc.protocol.entity.Agent;
import io.nuls.consensus.poc.protocol.tx.CreateAgentTransaction;
import io.nuls.consensus.poc.storage.po.AgentPo;
import io.nuls.consensus.poc.storage.service.AgentStorageService;
import io.nuls.core.tools.crypto.Hex;
import io.nuls.kernel.cfg.NulsConfig;
import io.nuls.kernel.constant.KernelErrorCode;
import io.nuls.kernel.constant.TransactionErrorCode;
import io.nuls.kernel.exception.NulsRuntimeException;
import io.nuls.kernel.lite.annotation.Autowired;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.validate.NulsDataValidator;
import io.nuls.kernel.validate.ValidateResult;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * date 2018/3/23.
 *
 * @author Facjas
 */
@Component
public class AgentCountValidator implements NulsDataValidator<CreateAgentTransaction> {

    @Autowired
    private AgentStorageService agentStorageService;

    @Override
    public ValidateResult validate(CreateAgentTransaction tx) {
        ValidateResult result = ValidateResult.getSuccessResult();
        Agent agent = tx.getTxData();

        List<AgentPo> caList = agentStorageService.getList();
        if (caList != null) {
            Set<String> set = new HashSet<>();
            for (AgentPo ca : caList) {
                if (ca.getHash().equals(tx.getHash())) {
                    return ValidateResult.getFailedResult(this.getClass().getName(), TransactionErrorCode.TRANSACTION_REPEATED);
                }
                if (ca.getDelHeight() > 0L) {
                    continue;
                }

                set.add(Hex.encode(ca.getAgentAddress()));
                set.add(Hex.encode(ca.getPackingAddress()));
            }
            boolean b = set.add(Hex.encode(agent.getAgentAddress()));
            if (!b) {
                return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.AGENT_EXIST);
            }
            b = set.add(Hex.encode(agent.getPackingAddress()));
            if (!b) {
                return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.AGENT_PACKING_EXIST);
            }
        }
        return result;
    }
}
