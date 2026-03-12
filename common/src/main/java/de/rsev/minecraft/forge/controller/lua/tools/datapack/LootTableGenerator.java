package de.rsev.minecraft.forge.controller.lua.tools.datapack;

public class LootTableGenerator {

    public static String generateSimpleChestLoot(String itemId, int count) {
        return """
        {
          "type": "minecraft:chest",
          "pools": [
            {
              "rolls": 1,
              "entries": [
                {
                  "type": "minecraft:item",
                  "name": "%s",
                  "functions": [
                    {
                      "function": "minecraft:set_count",
                      "count": %d
                    }
                  ]
                }
              ]
            }
          ]
        }
        """.formatted(itemId, count);
    }

    private LootTableGenerator() {}
}
