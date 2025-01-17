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
package io.nuls.contract.vm.instructions.references;

import io.nuls.contract.vm.Frame;
import io.nuls.contract.vm.ObjectRef;
import io.nuls.contract.vm.code.VariableType;
import io.nuls.contract.vm.util.Constants;
import io.nuls.contract.vm.util.Log;
import org.objectweb.asm.tree.TypeInsnNode;

public class Anewarray {

    public static void anewarray(Frame frame) {
        TypeInsnNode typeInsnNode = frame.typeInsnNode();
        String className = typeInsnNode.desc;
        int length = frame.operandStack.popInt();
        if (length < 0) {
            frame.throwNegativeArraySizeException();
            return;
        } else {
            ObjectRef arrayRef;
            if (className.contains(Constants.ARRAY_START)) {
                VariableType type = VariableType.valueOf(className);
                int[] dimensions = new int[type.getDimensions() + 1];
                dimensions[0] = length;
                VariableType variableType = VariableType.valueOf(Constants.ARRAY_START + className);
                arrayRef = frame.heap.newArray(variableType, dimensions);
            } else {
                VariableType variableType = VariableType.valueOf(Constants.ARRAY_PREFIX + className + Constants.ARRAY_SUFFIX);
                arrayRef = frame.heap.newArray(variableType, length);
            }
            frame.operandStack.pushRef(arrayRef);

            //Log.opcode(frame.getCurrentOpCode(), arrayRef);
        }
    }

}
