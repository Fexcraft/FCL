package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.mc.api.registry.fBlock;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fBlock(modid = "fcl", name = "crafting")
public class CraftingTable extends Block {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
    public CraftingTable(){
    	super(Material.GLASS);
    	setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    	setHarvestLevel("axe", 1);
		setHardness(1.0F);
		setResistance(32.0F);
    	setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state){
        return false;
    }
	
	@Override
	public boolean isFullCube(IBlockState state){
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
        return false;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote && !player.isSneaking()){
			UniEntity.getEntity(player).openUI(UniFCL.SELECT_RECIPE_CATEGORY, pos.getX(), pos.getY(), pos.getZ());
			player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
			return true;
		}
		return false;
	}
	
	@Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

	@Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta){
        EnumFacing facing = EnumFacing.byIndex(meta);
        if(facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(FACING, facing);
    }
	
	@Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(FACING).getIndex();
    }
	
	@Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING);
    }
	
}