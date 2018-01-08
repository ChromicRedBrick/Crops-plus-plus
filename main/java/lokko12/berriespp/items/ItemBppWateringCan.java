package lokko12.berriespp.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.crop.TileEntityCrop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

public class ItemBppWateringCan extends Item implements IFluidContainerItem {
	private int max_capacity = 1000;
	private int capacity = 0;
	private boolean nutrient = false;
	public ItemBppWateringCan() {
		super();
		this.setUnlocalizedName("WateringCan");
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTab.bpp);
        this.setTextureName("bpp:itemWateringCan");
        this.setMaxDamage(0);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		list.add("Max Capacity:");
		list.add(Integer.toString(max_capacity));
		list.add("Capacity:");
		list.add(Integer.toString(capacity));
		list.add("IsNutrient:");
		list.add(String.valueOf(nutrient));
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFluidTank)
		{
			IFluidTank tank = (IFluidTank) te;
			tank.drain((max_capacity-capacity), true);
			this.fill(null,new FluidStack(tank.getFluid(),tank.getFluidAmount()), true);
			return true;
		}
		
        if (te instanceof TileEntityCrop) {
            TileEntityCrop crop = (TileEntityCrop)te;
            if (crop.getCrop() instanceof ic2.api.crops.CropCard)
            {
            	if (nutrient==false) {
            		crop.humidity++;
            		capacity--;
            		return true;
            	}
            	
            	else {
            		crop.humidity++;
            		crop.nutrients++;
                	capacity--;
                	return true;
            	}
            }
        }
	return false;
	}

	@Override
	public FluidStack getFluid(ItemStack container) {
		if (nutrient==false)
		return new FluidStack(FluidRegistry.WATER,capacity);
		else return FluidRegistry.getFluidStack("fluidNutrientSolution",capacity);
	}


	@Override
	public int getCapacity(ItemStack container) {
		return max_capacity-capacity;
	}


	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		if (doFill == false)
			return 0;
		else {
			if ((resource.isFluidEqual(new FluidStack(FluidRegistry.WATER,max_capacity))||resource.isFluidEqual(FluidRegistry.getFluidStack("fluidNutrientSolution",capacity)))&&(!(capacity>=max_capacity))) {
					int afterfill;
					afterfill = resource.amount-(max_capacity-capacity);
					capacity = resource.amount;
					if (capacity>max_capacity)
						capacity = max_capacity;
					if (resource.isFluidEqual(FluidRegistry.getFluidStack("NutrientSolution",capacity)))
						nutrient = true;
					else
						nutrient = false;
					return afterfill;
				}
			else return 0;
		}
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		return null;
	}

	
}
