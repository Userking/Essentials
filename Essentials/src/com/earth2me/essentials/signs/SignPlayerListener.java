package com.earth2me.essentials.signs;

import com.earth2me.essentials.IEssentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class SignPlayerListener implements Listener
{
	private final transient IEssentials ess;

	public SignPlayerListener(final IEssentials ess)
	{
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if (event.isCancelled() || ess.getSettings().areSignsDisabled())
		{
			return;
		}

		final Block block = event.getClickedBlock();
		if (block == null)
		{
			return;
		}
		final int mat = block.getTypeId();
		if (mat == Material.SIGN_POST.getId() || mat == Material.WALL_SIGN.getId())
		{
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			{
				return;
			}
			final Sign csign = (Sign)block.getState();
			for (Signs signs : Signs.values())
			{
				final EssentialsSign sign = signs.getSign();
				if (csign.getLine(0).equalsIgnoreCase(sign.getSuccessName()))
				{
					sign.onSignInteract(block, event.getPlayer(), ess);
					event.setCancelled(true);
					return;
				}
			}
		}
		else
		{
			for (Signs signs : Signs.values())
			{
				final EssentialsSign sign = signs.getSign();
				if (sign.getBlocks().contains(block.getType())
					&& !sign.onBlockInteract(block, event.getPlayer(), ess))
				{
					event.setCancelled(true);
					return;

				}
			}
		}
	}
}
