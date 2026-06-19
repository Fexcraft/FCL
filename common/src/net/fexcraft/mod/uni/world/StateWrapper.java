package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.mixint.BSWProvider;
import net.fexcraft.mod.fcl.mixint.CWProvider;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.inv.StackWrapper;

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

    public static StateWrapper of(Object state){
        return ((BSWProvider)state).fcl_wrapper();
    }

    public static StateWrapper create(Object state){
        return STATE_WRAPPER.apply(state);
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

    public abstract Object getProperty(String key);

    public abstract <V> V getValue(Object prop);

    public abstract <V> V getValue(String key);

    public abstract <V> V getValue(String key, Class<V> type);

    public abstract IDL getIDL();

    public int get12Meta(){ return 0; }

    @Override
    public boolean equals(Object o){
        return direct().equals(o instanceof StateWrapper ? ((StateWrapper)o).direct() : o);
    }

}
