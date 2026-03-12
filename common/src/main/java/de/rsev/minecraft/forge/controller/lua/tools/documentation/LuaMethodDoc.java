package de.rsev.minecraft.forge.controller.lua.tools.documentation;

public class LuaMethodDoc {

    private final String name;
    private final String signature;
    private final String description;
    private final String example;
    private final String returns;

    public LuaMethodDoc(String name,
                        String signature,
                        String description,
                        String example,
                        String returns) {

        this.name = name;
        this.signature = signature;
        this.description = description;
        this.example = example;
        this.returns = returns;
    }

    public String name() {
        return name;
    }

    public String signature() {
        return signature;
    }

    public String description() {
        return description;
    }

    public String example() {
        return example;
    }

    public String returns() {  
        return returns;
    }
}