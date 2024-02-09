package zbasic.compiler;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

public final class Context {
    private final boolean functionContext;
    private final boolean stringResult;
    private final Set<Integer> numberValues;
    private final Set<String> stringValues;
    private final Map<String, Integer> numberConstants;
    private final Map<String, String> stringConstants;
    private final Set<String> numberVariables;
    private final Set<String> stringVariables;
    private final Map<String, Integer> staticArrays;
    private final Set<String> dynamicArrays;
    private final Map<String, Function> functions;
    private final Map<String, Subroutine> subroutines;

    public Context(boolean functionContext, boolean stringResult) {
        this.functionContext = functionContext;
        this.stringResult    = stringResult;
        this.numberValues    = new TreeSet<>();
        this.stringValues    = new TreeSet<>();
        this.numberConstants = new TreeMap<>();
        this.stringConstants = new TreeMap<>();
        this.numberVariables = new TreeSet<>();
        this.stringVariables = new TreeSet<>();
        this.staticArrays    = new TreeMap<>();
        this.dynamicArrays   = new TreeSet<>();
        this.functions       = new TreeMap<>();
        this.subroutines     = new TreeMap<>();
    }

    public Context(boolean functionContext) {
        this(functionContext, false);
    }

    public Context() {
        this(false, false);
    }

    public Context(Context ownerContext, boolean functionContext, boolean stringResult) {
        this(functionContext, stringResult);
        if (ownerContext == null) { throw new IllegalArgumentException("context"); }
        this.numberConstants.putAll(ownerContext.numberConstants);
        this.stringConstants.putAll(ownerContext.stringConstants);
        this.numberVariables.addAll(ownerContext.numberVariables);
        this.stringVariables.addAll(ownerContext.stringVariables);
        this.staticArrays.putAll(ownerContext.staticArrays);
        this.dynamicArrays.addAll(ownerContext.dynamicArrays);
    }

    public Context(Context ownerContext, boolean functionContext) {
        this(ownerContext, functionContext, false);
    }

    public Context(Context ownerContext) {
        this(ownerContext, false, false);
    }

    public boolean isFunctionContext() { return functionContext; }

    public boolean isStringResult() { return stringResult; }

    public Set<Integer> getNumberValues() { return numberValues; }

    public Set<String> getStringValues() { return stringValues; }

    public Map<String, Integer> getNumberConstants() { return numberConstants; }

    public Map<String, String> getStringConstants() { return stringConstants; }

    public Set<String> getNumberVariables() { return numberVariables; }

    public Set<String> getStringVariables() { return stringVariables; }

    public Map<String, Integer> getStaticArrays() { return staticArrays; }

    public Set<String> getDynamicArrays() { return dynamicArrays; }

    public Map<String, Function> getFunctions() { return functions; }

    public Map<String, Subroutine> getSubroutines() { return subroutines; }

    public Integer getNumberConstantValue(String name) { return numberConstants.get(name); }

    public String getStringConstantValue(String name) { return stringConstants.get(name); }

    public Integer getStaticArraySize(String name) { return staticArrays.get(name); }

    public boolean isNumberConstant(String name) { return numberConstants.get(name) != null; } 
    
    public boolean isStringConstant(String name) { return stringConstants.get(name) != null; }

    public boolean isNumberVariable(String name) { return numberVariables.contains(name); }

    public boolean isStringVariable(String name) { return stringVariables.contains(name); }

    public boolean isStaticArray(String name) { return staticArrays.get(name) != null; }

    public boolean isDynamicArray(String name) { return dynamicArrays.contains(name); }

    public boolean isFunction(String name) { return functions.get(name) != null; }

    public boolean isSubroutine(String name) { return subroutines.get(name) != null; }

    public void addNumberValue(int value) {
        if (value != 0) {
            numberValues.add(Integer.valueOf(value));
        }
    }

    public void addStringValue(String value) {
        if (value != null && !value.isEmpty()) {
            stringValues.add(value);
        }
    }

    public void addNumberConstant(String name, int value) {
        if (name != null && !name.isEmpty()) {
            numberConstants.put(name, Integer.valueOf(value));
        }
    }

    public void addStringConstant(String name, String value) {
        if (name != null && !name.isEmpty()) {
            if (value != null && !value.isEmpty()) {
                stringConstants.put(name, value);
            }
        }
    }

    public void addNumberVariable(String name) {
        if (name != null && !name.isEmpty()) {
            numberVariables.add(name);
        }
    }

    public void addStringVariable(String name) {
        if (name != null && !name.isEmpty()) {
            stringVariables.add(name);
        }
    }

    public void addStaticArray(String name, int size) {
        if (name != null && !name.isEmpty() && size > 0) {
            staticArrays.put(name, Integer.valueOf(size));
        }
    }

    public void addDynamicArray(String name) {
        if (name != null && !name.isEmpty()) {
            dynamicArrays.add(name);
        }
    }

    public void addFunction(String name, Function function) {
        if (name != null && !name.isEmpty()) {
            if (function != null) {
                functions.put(name, function);
            }
        }
    }

    public void addSubroutine(String name, Subroutine subroutine) {
        if (name != null && !name.isEmpty()) {
            if (subroutine != null) {
                subroutines.put(name, subroutine);
            }
        }
    }        
}
