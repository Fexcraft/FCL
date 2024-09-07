package net.fexcraft.lib.common.lang;

/**
 * <h1>bit management</h1>
 * stores a list of bits (booleans), and converts them to a single int for efficient networking transfer.
 * Currently has 16 values but can be increased to 32 without any actual problems.
 * @author Eternal Blue Flame
 * @comment I edited the format/spaces a bit also. ~ Ferdinand
 */
public class BitList {
	
    private boolean[] bits = new boolean[16];

    /** get a value in the array, index must be between 0 and 15 */
    public boolean get(int index){
        return bits[index];
    }
    
    /** set a value in the array, index must be between - and 15 */
    public void set(int index, boolean value){
        bits[index] = value;
    }
    
    /** set the value of the array to the last 16 bits of an integer */
    public void set(int value){
        for(int i = 0; i < 16; i++){
            //<< gets the bit from end to start at the position of i, for example 0 is the last bit, but 1 is second to last.
            // & is to get the bits from value
            bits[i] = (value & (1 << i)) != 0;
        }
    }

    /** get's the value for a bit at a specific position in the int */
    public boolean fromInt(int index, int value){
        return (value & (1 << index)) != 0;
    }

    /** return an int with the last 16 bits as the values of the array */
    public int toInt(){
        int output = 0;
        for(int i = 0; i < 16; i++){
            if(bits[i]){ output |= (1 << i); }
            else{ output &= ~(1 << i); }
        }
        return output;
    }
    
    // Fex's Code bellow, may not be useful for anyone, confusion included for free.
    
    public BitList(){}
    
    /** @author Ferdinand (FEX___96) */
    public BitList(boolean[] bools){
    	for(int i = 0; i < bools.length && i < 16; i++){
    		bits[i] = bools[i];
    	}
    }
    
    /** @author Ferdinand (FEX___96) 
     * @param offset */
    public boolean[] shorten(int size, int offset){
    	boolean[] newarr = new boolean[size];
    	for(int i = 0; i < size; i++){
    		if(i + offset >= bits.length){ break; }
    		newarr[i] = bits[i + offset];
    	}
    	return newarr;
    }
    
    /** @author Ferdinand (FEX___96) */
    public void integrate(boolean[] bools, int offset){
    	BitList list = new BitList();
    	list.bits = this.bits;
    	for(int i = 0; i < bools.length && i < 16; i++){
    		if(i + offset >= 16){ break; }
    		list.bits[i + offset] = bools[i];
    	}
    	return;
    }
    
    @Override
    /** @author Ferdinand (FEX___96) */
    public String toString(){
    	String str = "BitList[";
    	for(int i = 0; i < bits.length; i++){
    		str += (bits[i] ? 1 : 0) + (i >= bits.length ? "" : ",");
    	}
    	return str +  "][" + toInt() + "]";
    }

}