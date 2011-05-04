package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.util.Random;

public class BlockRedstoneInverter extends Block
{
	protected BlockRedstoneInverter(int i, boolean flag)
	{
		super(i, Material.circuits);
		
		inverterIsPowered = flag;
		
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		if(!world.isBlockOpaqueCube(i, j - 1, k))
			return false;
		
		return super.canPlaceBlockAt(world, i, j, k);
	}

	public boolean canBlockStay(World world, int i, int j, int k)
	{
		if(!world.isBlockOpaqueCube(i, j - 1, k))
			return false;
		
		return super.canBlockStay(world, i, j, k);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		if(i == 0)
			return inverterIsPowered ? 115 : 99; // torch on : off

		if(i == 1)
		{
			int j1 = (j & 0xc) >> 2;
			switch(field_22023_b[j1])
			{
			case 1: return (!inverterIsPowered ? 131 : 147); // top off : on
			case 2: return (!inverterIsPowered ? mod_RedstoneInverter.redstoneInverterTerrain[1] : mod_RedstoneInverter.redstoneInverterTerrain[0]);
			case 3: return (!inverterIsPowered ? mod_RedstoneInverter.redstoneInverterTerrain[3] : mod_RedstoneInverter.redstoneInverterTerrain[2]);
			case 4: return (!inverterIsPowered ? 147 : 131); // top on : off
			}
		}
		
		return 5; // bottom: stone slab
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		return l != 0 && l != 1;
	}

	public int getRenderType()
	{
		return mod_RedstoneInverter.redstoneInverterModelID;
	}

	public int getBlockTextureFromSide(int i)
	{
		return getBlockTextureFromSideAndMetadata(i, 0);
	}

	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int l = world.getBlockMetadata(i, j, k);
		boolean flag = isBlockGettingPowerFromNOR(world, i, j, k, l);
		
		if(inverterIsPowered && !flag)
		{
			world.setBlockAndMetadataWithNotify(i, j, k, mod_RedstoneInverter.redstoneInverter.blockID, l);
		} else
		if(!inverterIsPowered)
		{
			world.setBlockAndMetadataWithNotify(i, j, k, mod_RedstoneInverter.redstoneInverterA.blockID, l);
			if(!flag)
			{
				int i1 = (l & 0xc) >> 2;
				world.scheduleBlockUpdate(i, j, k, mod_RedstoneInverter.redstoneInverterA.blockID, field_22023_b[i1] * 2);
			}
		}
	}

	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
	{
		return isPoweringTo(world, i, j, k, l);
	}

	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		if(inverterIsPowered)
			return false;
			
		int i1 = iblockaccess.getBlockMetadata(i, j, k) & 3;
		
		if(l == 1)
			return true;
		
		if(i1 == 0 && l == 3)
			return true;
		if(i1 == 1 && l == 4)
			return true;
		if(i1 == 2 && l == 2)
			return true;

		return i1 == 3 && l == 5;
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if(!canBlockStay(world, i, j, k))
		{
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
			world.setBlockWithNotify(i, j, k, 0);
			return;
		}
		
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = isBlockGettingPowerFromNOR(world, i, j, k, i1);

		int j1 = (i1 & 0xc) >> 2;

		if(inverterIsPowered && !flag)
			world.scheduleBlockUpdate(i, j, k, blockID, field_22023_b[j1] * 2);
		else if(!inverterIsPowered && flag)
			world.scheduleBlockUpdate(i, j, k, blockID, field_22023_b[j1] * 2);
	}

	private boolean isBlockGettingPowerFromNOR(World world, int i, int j, int k, int l)
	{
		boolean flag = isBlockGettingPowerFrom(world, i, j, k, l);
		
		// nor gate
		switch(l)
		{
		case 0: // '\0'   von hinten & li, re
		case 2: // '\002' von vorne  & li, re
			flag = flag || world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5) ||
			               world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4);
			break;

		case 1: // '\001' von li & vorne, hinten
		case 3: // '\003' von re & vorne, hinten
			flag = flag || world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3) ||
			               world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2);
			break;
		}
		
		return flag;
	}


	private boolean isBlockGettingPowerFrom(World world, int i, int j, int k, int l)
	{
		int l1 = l & 3;

		switch(l1)
		{
		case 0: // '\0'
			return world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 1) || world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3);

		case 2: // '\002'
			return world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 1) || world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2);

		case 3: // '\003'
			return world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 1) || world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5);

		case 1: // '\001'
			return world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 1) || world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4);
		}
		
		return false;
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int l = world.getBlockMetadata(i, j, k);
		int i1 = (l & 0xc) >> 2;
		i1 = i1 + 1 << 2 & 0xc;
		world.setBlockMetadataWithNotify(i, j, k, i1 | l & 3);
		return true;
	}

	public boolean canProvidePower()
	{
		return inverterIsPowered;
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
	{
		int l = ((MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;
		world.setBlockMetadataWithNotify(i, j, k, l);
		boolean flag = isBlockGettingPowerFromNOR(world, i, j, k, l);
		if(flag)
			world.scheduleBlockUpdate(i, j, k, blockID, 1);
	}
	
	public void onBlockAdded(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public int idDropped(int i, Random random)
	{
		return mod_RedstoneInverter.redstoneInverterItem.shiftedIndex;
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if(inverterIsPowered)
			return;

		int l = world.getBlockMetadata(i, j, k);
		double d = (double)((float)i + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d1 = (double)((float)j + 0.4F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d2 = (double)((float)k + 0.5F) + (double)(random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		if(random.nextInt(2) == 0)
		{
			int i1 = (l & 0xc) >> 2;
			switch(l & 3)
			{
			case 0: // '\0'
				d4 = field_22024_a[i1];
				break;

			case 2: // '\002'
				d4 = -field_22024_a[i1];
				break;

			case 3: // '\003'
				d3 = field_22024_a[i1];
				break;

			case 1: // '\001'
				d3 = -field_22024_a[i1];
				break;
			}
		}
		world.spawnParticle("reddust", d + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
	}

	public static final double field_22024_a[] = {
		-0.0625D, 0.0625D, 0.1875D, 0.3125D
	};
	private static final int field_22023_b[] = {
		1, 2, 3, 4
	};
	private final boolean inverterIsPowered;

}
