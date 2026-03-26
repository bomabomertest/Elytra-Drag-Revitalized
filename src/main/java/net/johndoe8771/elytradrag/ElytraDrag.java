package net.johndoe8771.elytradrag;

import net.johndoe8771.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ElytraDrag implements ModInitializer {

	public static final String MOD_ID = "elytradrag";

	@Override
	public void onInitialize() {
		ModConfig.LoadConfig();
		ServerTickEvents.END_SERVER_TICK.register(this::ElytraDragTick);
	}
	private void ElytraDragTick(MinecraftServer server) {
		var playerList  = server.getPlayerList().getPlayers();
		if(playerList.isEmpty())
			return;

		for (ServerPlayer spe : playerList)
		{
			if(spe.isFallFlying() && spe.isShiftKeyDown())
			{
				spe.getJumpBoostPower();
				var playerVelocity = spe.getDeltaMovement();
				var playerSpeed = playerVelocity.length() * 20.0f;
				if(playerSpeed > ModConfig.MINIMUM_SPEED)
				{
					var newVelocity = playerVelocity.scale(1.0f - 0.05f * ModConfig.ELYTRA_DRAG);
					spe.setDeltaMovement(newVelocity);
					spe.hurtMarked = true;
				}
				LimitFallDistance(spe);
			}
		}
	}

	/**
	 * Caps the FallDistance when drag is applied and
	 * makes the vertical speed requirement for
	 * fallDistance reset easier to reach
	 */
	private void LimitFallDistance(ServerPlayer player)
	{
		if (player.getDeltaMovement().y() > -1.0f && player.fallDistance > 1.0f)
			player.fallDistance = 1.0f;
		else if(player.fallDistance > ModConfig.MAXIMUM_FALLDISTANCE)
		{
			player.fallDistance = ModConfig.MAXIMUM_FALLDISTANCE;
		}
	}
}