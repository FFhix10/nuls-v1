package io.nuls.contract.vm;

import io.nuls.contract.vm.code.VariableType;

import java.util.Stack;

public class OperandStack extends Stack {

    private final int maxStack;

    public OperandStack(int maxStack) {
        super();
        this.maxStack = maxStack;
    }

    @Override
    public Object push(Object value) {
        return super.push(value);
    }

    public Object push(Object value, VariableType variableType) {
        if (variableType.isPrimitive()) {
            switch (variableType.getType()) {
                case "int":
                    value = pushInt((int) value);
                    break;
                case "long":
                    value = pushLong((long) value);
                    break;
                case "float":
                    value = pushFloat((float) value);
                    break;
                case "double":
                    value = pushDouble((double) value);
                    break;
                case "boolean":
                    value = pushBoolean((boolean) value);
                    break;
                case "byte":
                    value = pushByte((byte) value);
                    break;
                case "char":
                    value = pushChar((char) value);
                    break;
                case "short":
                    value = pushShort((short) value);
                    break;
                default:
                    value = push(value);
                    break;
            }
        } else {
            value = push(value);
        }
        return value;
    }

    @Override
    public synchronized Object pop() {
        return super.pop();
    }

    public int pushInt(int value) {
        push(value);
        return value;
    }

    public int popInt() {
        return (int) pop();
    }

    public long pushLong(long value) {
        push(value);
        push(null);
        return value;
    }

    public long popLong() {
        pop();
        return (long) pop();
    }

    public float pushFloat(float value) {
        push(value);
        return value;
    }

    public float popFloat() {
        return (float) pop();
    }

    public double pushDouble(double value) {
        push(value);
        push(null);
        return value;
    }

    public double popDouble() {
        pop();
        return (double) pop();
    }

    public int pushBoolean(boolean value) {
        return pushInt(value ? 1 : 0);
    }

    public boolean popBoolean() {
        return popInt() == 1 ? true : false;
    }

    public int pushByte(byte value) {
        return pushInt(value);
    }

    public byte popByte() {
        return (byte) popInt();
    }

    public int pushChar(char value) {
        return pushInt(value);
    }

    public char popChar() {
        return (char) popInt();
    }

    public int pushShort(short value) {
        return pushInt(value);
    }

    public short popShort() {
        return (short) popInt();
    }

    public ObjectRef pushRef(ObjectRef ref) {
        push(ref);
        return ref;
    }

    public ObjectRef popRef() {
        return (ObjectRef) pop();
    }

}
