package zbasic.generator.zvm;

import zbasic.compiler.Module;

import java.util.ArrayList;
import java.util.List;

public final class Generator {
    public static final String SIGNATURE  = "ZVM";
    public static final int    VERSION    = 0;

    private static final int BYTE_LIMIT   = 256;
    private static final int HALF_BYTE    = 128;
    private static final int MIN_SIG_BYTE = -128;
    private static final int MAX_UNS_BYTE = 255;

    public Generator() {}

    public byte[] generate(Module module) {
        if (module == null) { throw new IllegalArgumentException("module"); }
        final List<Integer> buffer = new ArrayList<>();
        writeHeader(buffer, module);
        writeStringValues(buffer, module);
        writeNumberValues(buffer, module);
        writeImport(buffer, module);
        writeData(buffer, module);
        writeCode(buffer, module);
        final byte[] result = new byte[buffer.size()];
        int index = 0;
        for (Integer b : buffer) {
            final int value = b.intValue();
            result[index++] = (value < HALF_BYTE) ? (byte)value : (byte)(value - BYTE_LIMIT);
        }
        return result;
    }

    private void writeHeader(List<Integer> buffer, Module module) {
        writeSignature(buffer);
        buffer.add(Integer.valueOf(VERSION));
        if (module.getInitialization().size() > 0) {
            buffer.add(module.getSubroutines().size());
        } else {
            buffer.add(0);
        }
        buffer.add(0);
        buffer.add(module.getStringValues().size());
        int bigNumberCounter = 0;
        for (Integer numberValue : module.getNumberValues()) {
            final int number = numberValue.intValue();
            if (number < MIN_SIG_BYTE || number > MAX_UNS_BYTE) {
                bigNumberCounter++;
            }
        }
        buffer.add(bigNumberCounter);
        buffer.add(module.getNumberConstants().size());
        buffer.add(module.getStringConstants().size());
        buffer.add(module.getNumberVariables().size());
        buffer.add(module.getStringVariables().size());
        //buffer.add(module.getArrays().size());
        buffer.add(0);
        buffer.add(module.getFunctions().size());
        buffer.add(module.getFunctions().size());
        buffer.add(module.getSubroutines().size());
    }

    private void writeSignature(List<Integer> buffer) {
        for (char c : SIGNATURE.toCharArray()) {
            buffer.add(Integer.valueOf((int)c));
        }
    }

    private void writeStringValues(List<Integer> buffer, Module module) {
        for (String stringValue : module.getStringValues()) {
            final int length =
                (stringValue.length() <= MAX_UNS_BYTE) ? stringValue.length() : MAX_UNS_BYTE;
            buffer.add(length);
            final char[] chars = stringValue.toCharArray();
            for (int i = 0; i < length; i++) {
                if (chars[i] >= (char)0 && chars[i] <= (char)0x7F) {
                    buffer.add((int)chars[i]);
                }
            }
        }
    }

    private void writeNumberValues(List<Integer> buffer, Module module) {}

    private void writeImport(List<Integer> buffer, Module module) {}

    private void writeData(List<Integer> buffer, Module module) {
    }

    private void writeCode(List<Integer> buffer, Module module) {
    }

    private void addDoubleByte(List<Integer> buffer, int number) {
        buffer.add((number & 0xFF00) >>> 8);
        buffer.add(number & 0xFF);
    }
}
