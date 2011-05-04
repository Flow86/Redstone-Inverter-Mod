package net.minecraft.src;

import java.util.Random;

public class mod_RedstoneInverter extends BaseMod
{
	public static Item redstoneInverterItem;
	public static Block redstoneInverter;
	public static int redstoneInverterModelID;
	public static int redstoneInverterID = 120;
	public static int redstoneInverterItemID = 1200;
	
	public static int[] redstoneInverterTerrain = new int[4];
	
	private RenderBlocks renderBlocks;
	private IBlockAccess blockAccess;
	
	public mod_RedstoneInverter()
	{
		redstoneInverter = new BlockRedstoneInverter(redstoneInverterID).setBlockName("not gate");

		redstoneInverterTerrain[0] = ModLoader.addOverride("/terrain.png", "/redstoneinverter_1a.png");
		redstoneInverterTerrain[1] = ModLoader.addOverride("/terrain.png", "/redstoneinverter_2a.png");
		redstoneInverterTerrain[2] = ModLoader.addOverride("/terrain.png", "/redstoneinverter_1b.png");
		redstoneInverterTerrain[3] = ModLoader.addOverride("/terrain.png", "/redstoneinverter_2b.png");

		int id = ModLoader.addOverride("/gui/items.png", "/gui/redstoneinverter.png");
		redstoneInverterItem = new ItemReed(redstoneInverterItemID, redstoneInverter).setIconIndex(id).setItemName("Redstone Inverter Item");

		ModLoader.AddName(redstoneInverter,   "Redstone Inverter");
		ModLoader.AddName(redstoneInverterItem,   "Redstone Inverter");

		ModLoader.RegisterBlock(redstoneInverter);

		ModLoader.AddRecipe(new ItemStack(redstoneInverterItem, 1), new Object[] {
			"#X#", 
			"III", 
			Character.valueOf('#'), Item.redstone,
			Character.valueOf('X'), Block.torchRedstoneActive,
			Character.valueOf('I'), Block.stone
		});
		
		redstoneInverterModelID = ModLoader.getUniqueBlockModelID(this, false);
	}

	public boolean renderBlockRedstoneInverter(Block block, int i, int j, int k)
	{
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 3;
		int j1 = (l & 0xc) >> 2;
		renderBlocks.renderStandardBlock(block, i, j, k);
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(blockAccess, i, j, k);
		if(Block.lightValue[block.blockID] > 0)
		{
			f = (f + 1.0F) * 0.5F;
		}
		tessellator.setColorOpaque_F(f, f, f);
		double d = -0.1875D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		switch(i1)
		{
		case 0: // '\0'
			d4 = -0.3125D;
			d2 = BlockRedstoneInverter.field_22024_a[j1];
			break;

		case 2: // '\002'
			d4 = 0.3125D;
			d2 = -BlockRedstoneInverter.field_22024_a[j1];
			break;

		case 3: // '\003'
			d3 = -0.3125D;
			d1 = BlockRedstoneInverter.field_22024_a[j1];
			break;

		case 1: // '\001'
			d3 = 0.3125D;
			d1 = -BlockRedstoneInverter.field_22024_a[j1];
			break;
		}
		renderBlocks.renderTorchAtAngle(block, (double)i + d1, (double)j + d, (double)k + d2, 0.0D, 0.0D);
		//renderBlocks.renderTorchAtAngle(block, (double)i + d3, (double)j + d, (double)k + d4, 0.0D, 0.0D);
		int k1 = block.getBlockTextureFromSideAndMetadata(1, l);
		int l1 = (k1 & 0xf) << 4;
		int i2 = k1 & 0xf0;
		double d5 = (float)l1 / 256F;
		double d6 = ((float)l1 + 15.99F) / 256F;
		double d7 = (float)i2 / 256F;
		double d8 = ((float)i2 + 15.99F) / 256F;
		float f1 = 0.125F;
		float f2 = i + 1;
		float f3 = i + 1;
		float f4 = i + 0;
		float f5 = i + 0;
		float f6 = k + 0;
		float f7 = k + 1;
		float f8 = k + 1;
		float f9 = k + 0;
		float f10 = (float)j + f1;
		if(i1 == 2)
		{
			f2 = f3 = i + 0;
			f4 = f5 = i + 1;
			f6 = f9 = k + 1;
			f7 = f8 = k + 0;
		} else
		if(i1 == 3)
		{
			f2 = f5 = i + 0;
			f3 = f4 = i + 1;
			f6 = f7 = k + 0;
			f8 = f9 = k + 1;
		} else
		if(i1 == 1)
		{
			f2 = f5 = i + 1;
			f3 = f4 = i + 0;
			f6 = f7 = k + 1;
			f8 = f9 = k + 0;
		}
		tessellator.addVertexWithUV(f5, f10, f9, d5, d7);
		tessellator.addVertexWithUV(f4, f10, f8, d5, d8);
		tessellator.addVertexWithUV(f3, f10, f7, d6, d8);
		tessellator.addVertexWithUV(f2, f10, f6, d6, d7);
		return true;
	}

	public boolean RenderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l)
	{
		renderBlocks = renderblocks;
		blockAccess = iblockaccess;
		
		if(l == redstoneInverterModelID)
			return  renderBlockRedstoneInverter(block, i, j, k);

		return false;
	}

	public String Version()
	{
		return "1.5_01";
	}
}
