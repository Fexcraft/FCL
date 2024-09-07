package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class StateWrapperI extends StateWrapper {

    private BlockState state;

    public StateWrapperI(BlockState state){
        this.state = state;
    }

    @Override
    public Object getBlock(){
        return state.getBlock();
    }

    @Override
    public <S> S local(){
        return (S)state;
    }

    @Override
    public Object direct(){
        return state;
    }

    @Override
    public <V> V getValue(Object prop){
        return (V)state.getValue((Property<?>)prop);
    }

    @Override
    public IDL getIDL(){
        return IDLManager.getIDL(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
    }

    @Override
    public int get12Meta(){
        return 0;
    }

}
