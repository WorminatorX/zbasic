package zbasic.compiler;

import static zbasic.Keyword.isStringName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class Module extends Node {
    private final String name;
    private final Context context;
    private final Map<String, Integer> numberVariableValues;
    private final Map<String, String> stringVariableValues;
    private final List<Node.KeywordTree> initialization;

    public Module(String name) {
        if (name == null) { throw new IllegalArgumentException("name"); }
        this.name = name;
        this.context = new Context();
        this.numberVariableValues = new TreeMap<>();
        this.stringVariableValues = new TreeMap<>();
        this.initialization = new ArrayList<>();
    }

    public String getName() { return name; }

    public Context getContext() { return context; }

    public Set<Integer> getNumberValues() { return context.getNumberValues(); }

    public Set<String> getStringValues() { return context.getStringValues(); }

    public Map<String, Integer> getStaticArrays() { return context.getStaticArrays(); }

    public Set<String> getDynamicArrays() { return context.getDynamicArrays(); }

    public Map<String, Integer> getNumberConstants() { return context.getNumberConstants(); }

    public Map<String, Integer> getNumberVariables() {
        final Map<String, Integer> numberVariables = new TreeMap<>();
        for (String numberVariable : context.getNumberVariables()) {
            final Integer value = numberVariableValues.get(numberVariable);
            numberVariables.put(numberVariable, (value != null) ? value : Integer.valueOf(0));
        }
        return numberVariables;
    }

    public Map<String, String> getStringConstants() { return context.getStringConstants(); }

    public Map<String, String> getStringVariables() {
        final Map<String, String> stringVariables = new TreeMap<>();
        for (String stringVariable : context.getStringVariables()) {
            final String value = stringVariableValues.get(stringVariable);
            stringVariables.put(stringVariable, (value != null) ? value : "");
        }
        return stringVariables;
    }

    public Map<String, Function> getFunctions() { return context.getFunctions(); }

    public Map<String, Subroutine> getSubroutines() { return context.getSubroutines(); }

    public List<Node.KeywordTree> getInitialization() { return initialization; }

    public void addStaticArray(String name, int size) {
        if (name != null && !context.isDynamicArray(name) && !context.isStaticArray(name)) {
            if (
                name.isEmpty() ||
                isStringName(name) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            if (size <= 0) {
                throw new IllegalArgumentException("size");
            }
            context.addStaticArray(name, size);
        }
    }

    public void addDynamicArray(String name) {
        if (name != null && !context.isDynamicArray(name) && !context.isStaticArray(name)) {
            if (
                name.isEmpty() ||
                isStringName(name) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addDynamicArray(name);
        }
    }

    public void addNumberConstant(String name, int number) {
        if (name != null && !context.isNumberConstant(name)) {
            if (
                name.isEmpty() ||
                isStringName(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isNumberVariable(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addNumberConstant(name, number);
        }
    }

    public void addNumberVariable(String name, int number) {
        addNumberVariable(name);
        if (number != 0) {
            numberVariableValues.put(name, Integer.valueOf(number));
        }
    }

    public void addNumberVariable(String name) {
        if (name != null && !context.isNumberVariable(name)) {
            if (
                name.isEmpty() ||
                isStringName(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isNumberConstant(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addNumberVariable(name);
        }
    }

    public void addStringConstant(String name, String string) {
        if (string == null) { throw new IllegalArgumentException("string"); }
        if (name != null && !context.isStringConstant(name)) {
            if (
                name.isEmpty() ||
                !isStringName(name) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringVariable(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addStringConstant(name, string);
        }
    }

    public void addStringVariable(String name, String string) {
        if (string == null) { throw new IllegalArgumentException("string"); }
        addStringVariable(name);
        if (!string.isEmpty()) {
            stringVariableValues.put(name, string);
        }
    }

    public void addStringVariable(String name) {
        if (name != null && !context.isStringVariable(name)) {
            if (
                name.isEmpty() ||
                !isStringName(name) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringConstant(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isFunction(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addStringVariable(name);
        }
    }

    public void addFunction(String name, Function function) {
        if (function == null) { throw new IllegalArgumentException("function"); }
        if (name != null && !context.isFunction(name)) {
            if (
                name.isEmpty() ||
                (isStringName(name) != function.isString()) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isSubroutine(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addFunction(name, function);
        }
    }

    public void addSubroutine(String name, Subroutine subroutine) {
        if (subroutine == null) { throw new IllegalArgumentException("subroutine"); }
        if (name != null && !context.isSubroutine(name)) {
            if (
                name.isEmpty() ||
                isStringName(name) ||
                context.isNumberConstant(name) ||
                context.isNumberVariable(name) ||
                context.isStringConstant(name) ||
                context.isStringVariable(name) ||
                context.isStaticArray(name) ||
                context.isDynamicArray(name) ||
                context.isFunction(name)
            ) {
                throw new IllegalArgumentException("name");
            }
            context.addSubroutine(name, subroutine);
        }
    }
}
