package ex5.analyzer;

import ex5.Constants.Constants;

import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, VariableType> globalSymbolTable;
    private final HashMap<String, VariableType> localSymbolTable;

    public SymbolTable() {
        this.globalSymbolTable = new HashMap<>();
        this.localSymbolTable = new HashMap<>();
    }

    public void startMethod() {
        this.localSymbolTable.clear();
    }

    public void addToTable(String name, String scope, VariableType variableType) {
        if (scope.equals(Constants.GLOBAL_SCOPE)) {
            globalSymbolTable.put(name, variableType);
        } else {
            localSymbolTable.put(name, variableType);
        }
    }

    public String scopeOf(String name){
        if (localSymbolTable.containsKey(name)) {
            return Constants.LOCAL_SCOPE;
        } else if(globalSymbolTable.containsKey(name)) {
            return Constants.GLOBAL_SCOPE;
        }
        else {
            return null;
        }
    }

    public VariableType typeOf(String name){
        if (localSymbolTable.containsKey(name)) {
            return localSymbolTable.get(name);
        } else return globalSymbolTable.getOrDefault(name, null);
    }

}
