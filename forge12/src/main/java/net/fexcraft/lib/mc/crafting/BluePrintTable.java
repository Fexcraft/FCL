package net.fexcraft.lib.mc.crafting;

import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.api.registry.fBlock;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

@fBlock(modid = "fcl", name = "blueprinttable")
public class BluePrintTable extends Block {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
    public BluePrintTable(){
    	super(Material.GLASS);
    	this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    	this.setHarvestLevel("axe", 1); this.setHardness(1.0F); this.setResistance(32.0F);
    	this.setCreativeTab(CreativeTabs.TOOLS);
    	//FCLRegistry.getAutoRegisterer("fcl").addBlock("blueprinttable", this, null, 0, null);
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
		if(!world.isRemote){
			if(!player.isSneaking()){
				UniEntity.getEntity(player).openUI(UniFCL.SELECT_RECIPE_CATEGORY, pos.getX(), pos.getY(), pos.getZ());
				player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
				return true;
			}
		}
		return false;
	}
	
	@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if(enumfacing.getAxis() == EnumFacing.Axis.Y){ enumfacing = EnumFacing.NORTH; }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
	
	@Override
    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
	
	@Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
	
}