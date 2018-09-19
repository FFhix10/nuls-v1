package io.nuls.consensus.poc.rpc.cmd;

import io.nuls.core.tools.str.StringUtils;
import io.nuls.kernel.model.CommandResult;
import io.nuls.kernel.model.Na;
import io.nuls.kernel.model.RpcClientResult;
import io.nuls.kernel.processor.CommandProcessor;
import io.nuls.kernel.utils.CommandBuilder;
import io.nuls.kernel.utils.CommandHelper;
import io.nuls.kernel.utils.RestFulUtils;

import java.util.HashMap;
import java.util.Map;

public class CreateMultiAgentByAddrProcessor implements CommandProcessor {
    private RestFulUtils restFul = RestFulUtils.getInstance();
    @Override
    public String getCommand() {
        return "createMultiAgentByAddr";
    }

    @Override
    public String getHelp() {
        return "createagent <agentAddress> <packingAddress> <signAddress> <commissionRate> <deposit> [rewardAddress] --create a agent";
    }

    @Override
    public String getCommandDescription() {
        CommandBuilder bulider = new CommandBuilder();
        bulider.newLine(getCommandDescription())
                .newLine("\t<agentAddress>   agent owner address   -required")
                .newLine("\t<packingAddress>    packing address    -required")
                .newLine("\t<signAddress> \tsign address address - Required")
                .newLine("\t<commissionRate>    commission rate (10~100), you can have up to 2 valid digits after the decimal point  -required")
                .newLine("\t<deposit>   amount you want to deposit, you can have up to 8 valid digits after the decimal point -required")
                .newLine("\t[rewardAddress]  Billing address    -not required");
        return bulider.toString();
    }

    @Override
    public boolean argsValidate(String[] args) {
        int length = args.length;
        if(length != 6 && length != 7){
            return false;
        }
        if (!CommandHelper.checkArgsIsNull(args)) {
            return false;
        }
        if(!StringUtils.validAddressSimple(args[1]) || !StringUtils.validAddressSimple(args[2])|| !StringUtils.validAddressSimple(args[3]) || StringUtils.isBlank(args[4])
                || StringUtils.isBlank(args[5])){
            return false;
        }
        if(!StringUtils.isNumberGtZeroLimitTwo(args[4])){
            return false;
        }
        if(!StringUtils.isNuls(args[5])){
            return false;
        }
        if(length == 7 && !StringUtils.validAddressSimple(args[5])){
            return false;
        }
        return true;
    }

    @Override
    public CommandResult execute(String[] args) {
        String signAddress = args[3];
        RpcClientResult res = CommandHelper.getPassword(signAddress, restFul);
        if(!res.isSuccess()){
            return CommandResult.getFailed(res);
        }
        String password = (String)res.getData();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("agentAddress", args[1]);
        parameters.put("packingAddress", args[2]);
        parameters.put("signAddress", args[3]);
        parameters.put("commissionRate", Double.valueOf(args[4]));
        Long deposit = null;
        try {
            Na na = Na.parseNuls(args[5]);
            deposit = na.getValue();
        } catch (Exception e) {
            return CommandResult.getFailed("Parameter deposit error");
        }
        parameters.put("deposit", deposit);
        parameters.put("password", password);
        if(args.length == 7){
            parameters.put("rewardAddress", args[6]);
        }
        RpcClientResult result = restFul.post("/multiAccount/createMultiAgent",parameters);
        if (result.isFailed()) {
            return CommandResult.getFailed(result);
        }
        return CommandResult.getResult(CommandResult.dataTransformValue(result));
    }
}