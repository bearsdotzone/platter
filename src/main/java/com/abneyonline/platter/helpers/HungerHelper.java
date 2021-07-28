package com.abneyonline.platter.helpers;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class HungerHelper
{
	protected static final Field foodExhaustion;

	static
	{
		try
		{
			foodExhaustion = FoodData.class.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "exhaustionLevel"));
			foodExhaustion.setAccessible(true);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static float getMaxExhaustion(Player player)
	{
		return 4.0f;
	}

	public static float getExhaustion(Player player)
	{
		try
		{
			return foodExhaustion.getFloat(player.getFoodData());
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void setExhaustion(Player player, float exhaustion)
	{
		try
		{
			foodExhaustion.setFloat(player.getFoodData(), exhaustion);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
