package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.item.StackWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class StateWrapper {

    public static StateWrapper DEFAULT = null;
    public static Function<Object, StateWrapper> STATE_WRAPPER = null;
    public static BiFunction<Object, String, StateWrapper> COMMAND_WRAPPER = null;
    public static BiFunction<StackWrapper, PlacingContext, StateWrapper> STACK_WRAPPER = null;
    public static ConcurrentHashMap<Object, StateWrapper> WRAPPERS = new ConcurrentHashMap<>();

    public static StateWrapper of(Object state){
        StateWrapper wrapper = WRAPPERS.get(state);
        if(wrapper == null) WRAPPERS.put(state, wrapper = STATE_WRAPPER.apply(state));
        return wrapper;
    }
    public static StateWrapper from(StackWrapper stack, PlacingContext ctx){
        return STACK_WRAPPER.apply(stack, ctx);
    }

    /** Command type state format. */
	public static StateWrapper from(Object block, String state_arg){
        return COMMAND_WRAPPER.apply(block, state_arg);
	}

	public abstract String getStateString();

	public static class PlacingContext {

        public final CubeSide side;
        public final EntityW placer;
        public final boolean main;
        public final WorldW world;
        public final V3I pos;
        public final V3D off;

        public PlacingContext(WorldW world, V3I pos, V3D off, CubeSide side, EntityW placer, boolean main){
            this.placer = placer;
            this.world = world;
            this.side = side;
            this.main = main;
            this.pos = pos;
            this.off = off;
        }

    }

    public abstract Object getBlock();

    public abstract <S> S local();

    public abstract Object direct();

    public abstract <V> V getValue(Object prop);

    public abstract IDL getIDL();

    public abstract int get12Meta();

    @Override
    public boolean equals(Object o){
        return direct().equals(o instanceof StateWrapper ? ((StateWrapper)o).direct() : o);
    }

}
