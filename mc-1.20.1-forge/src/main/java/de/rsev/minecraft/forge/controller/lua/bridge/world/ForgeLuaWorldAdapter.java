// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.world;

import org.luaj.vm2.LuaTable;

public class ForgeLuaWorldAdapter implements LuaWorldAdapter {

    public boolean setBlock(int x,int y,int z,String id){
        return LuaWorldBridgeInternal.setBlock(x,y,z,id);
    }

    public String getBlock(int x,int y,int z){
        return LuaWorldBridgeInternal.getBlock(x,y,z);
    }

    public boolean setSignText(int x,int y,int z,String l1,String l2){
        return LuaWorldBridgeInternal.setSignText(x,y,z,l1,l2);
    }

    public boolean spawnEntity(String id,double x,double y,double z){
        return LuaWorldBridgeInternal.spawnEntity(id,x,y,z);
    }

    public LuaTable getAllEntities(){
        return LuaWorldBridgeInternal.getAllEntities();
    }

    public LuaTable getEntities(String type,double x,double y,double z,double range){
        return LuaWorldBridgeInternal.getEntities(type,x,y,z,range);
    }

    public boolean removeEntity(int id){
        return LuaWorldBridgeInternal.removeEntity(id);
    }

    public int killAllEntities(){
        return LuaWorldBridgeInternal.killAllEntities();
    }

    public int killEntities(LuaTable filter){
        return LuaWorldBridgeInternal.killEntities(filter);
    }

    public LuaTable getPlayers(){
        return LuaWorldBridgeInternal.getPlayers();
    }

    public Object getPlayerById(int id){
        return LuaWorldBridgeInternal.getPlayerById(id);
    }

    public long getWorldTime(){
        return LuaWorldBridgeInternal.getWorldTime();
    }

    public boolean setWorldTime(long t){
        return LuaWorldBridgeInternal.setWorldTime(t);
    }

    public String getWeather(){
        return LuaWorldBridgeInternal.getWeather();
    }

    public boolean setWeather(String type){
        return LuaWorldBridgeInternal.setWeather(type);
    }
}