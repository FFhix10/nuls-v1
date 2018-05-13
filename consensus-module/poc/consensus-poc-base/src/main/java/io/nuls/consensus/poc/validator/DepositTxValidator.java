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

package io.nuls.consensus.poc.validator;/*
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

import io.nuls.consensus.poc.context.PocConsensusContext;
import io.nuls.consensus.poc.protocol.constant.PocConsensusErrorCode;
import io.nuls.consensus.poc.protocol.constant.PocConsensusProtocolConstant;
import io.nuls.consensus.poc.protocol.constant.PunishType;
import io.nuls.consensus.poc.protocol.entity.Deposit;
import io.nuls.consensus.poc.protocol.tx.DepositTransaction;
import io.nuls.consensus.poc.storage.po.AgentPo;
import io.nuls.consensus.poc.storage.po.DepositPo;
import io.nuls.consensus.poc.storage.service.AgentStorageService;
import io.nuls.consensus.poc.storage.service.DepositStorageService;
import io.nuls.consensus.poc.storage.service.PunishLogStorageService;
import io.nuls.core.tools.log.Log;
import io.nuls.kernel.constant.SeverityLevelEnum;
import io.nuls.kernel.lite.annotation.Autowired;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.Na;
import io.nuls.kernel.model.NulsDigestData;
import io.nuls.kernel.validate.ValidateResult;

import java.util.List;

/**
 * @author ln
 * @date 2018/5/10
 */
@Component
public class DepositTxValidator extends BaseConsensusProtocolValidator<DepositTransaction> {

    @Autowired
    private PunishLogStorageService punishLogStorageService;

    @Autowired
    private AgentStorageService agentStorageService;

    @Autowired
    private DepositStorageService depositStorageService;

    @Override
    public ValidateResult validate(DepositTransaction tx) {
        if (null == tx || null == tx.getTxData() || null == tx.getTxData().getAgentHash() || null == tx.getTxData().getDeposit() || null == tx.getTxData().getAddress()) {
            return ValidateResult.getFailedResult(this.getClass().getName(), "the deposit tx is Incomplete!");
        }
        Deposit deposit = tx.getTxData();
        AgentPo agentPo = agentStorageService.get(deposit.getAgentHash());
        if (null == agentPo) {
            return ValidateResult.getFailedResult(this.getClass().getName(), "Agent is not exist!");
        }
        long count = 0;
        try {
            count = this.getCountByType(deposit.getAddress(), PunishType.RED.getCode());
        } catch (Exception e) {
            Log.error(e);
            return ValidateResult.getFailedResult(this.getClass().getName(), e.getMessage());
        }
        if (count > 0) {
            return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.LACK_OF_CREDIT);
        }
        List<DepositPo> poList = this.getDepositListByAgent(deposit.getAgentHash());
        if (null != poList && poList.size() >= PocConsensusProtocolConstant.MAX_ACCEPT_NUM_OF_DEPOSIT) {
            return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.DEPOSIT_OVER_COUNT);
        }
        Na limit = PocConsensusProtocolConstant.ENTRUSTER_DEPOSIT_LOWER_LIMIT;
        Na max = PocConsensusProtocolConstant.SUM_OF_DEPOSIT_OF_AGENT_UPPER_LIMIT;
        Na total = Na.ZERO;
        for (DepositPo cd : poList) {
            total = total.add(cd.getDeposit());
        }
        if (limit.isGreaterThan(deposit.getDeposit())) {
            return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.DEPOSIT_NOT_ENOUGH);
        }
        if (max.isLessThan(total.add(deposit.getDeposit()))) {
            return ValidateResult.getFailedResult(this.getClass().getName(), PocConsensusErrorCode.DEPOSIT_TOO_MUCH);
        }

        if (!isDepositOk(deposit.getDeposit(), tx.getCoinData())) {
            return ValidateResult.getFailedResult(this.getClass().getName(), SeverityLevelEnum.FLAGRANT_FOUL, PocConsensusErrorCode.DEPOSIT_ERROR);
        }
        return ValidateResult.getSuccessResult();
    }

    private List<DepositPo> getDepositListByAgent(NulsDigestData agentHash) {
        // todo auto-generated method stub
        List<Deposit> depositList = PocConsensusContext.getChainManager().getMasterChain().getChain().getDepositList();
        return null;
    }

    private long getCountByType(byte[] address, int code) {
        //todo
        return 0;
    }

}