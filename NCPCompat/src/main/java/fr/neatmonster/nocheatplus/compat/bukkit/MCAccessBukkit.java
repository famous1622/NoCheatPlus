package fr.neatmonster.nocheatplus.compat.bukkit;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import fr.neatmonster.nocheatplus.compat.AlmostBoolean;
import fr.neatmonster.nocheatplus.compat.MCAccess;
import fr.neatmonster.nocheatplus.utilities.BlockCache;
import fr.neatmonster.nocheatplus.utilities.BlockProperties;
import fr.neatmonster.nocheatplus.utilities.PotionUtil;

public class MCAccessBukkit implements MCAccess{
	
	/**
	 * Constructor to let it fail.
	 */
	public MCAccessBukkit(){
		// TODO: ...
	}

	@Override
	public String getMCVersion() {
		// Bukkit API.
		// TODO: maybe output something else.
		return "?";
	}

	@Override
	public String getServerVersionTag() {
		return "Bukkit-API";
	}

	@Override
	public CommandMap getCommandMap() {
		return null;
	}

	@Override
	public BlockCache getBlockCache(final World world) {
		return new BlockCacheBukkit(world);
	}

	@Override
	public double getHeight(final Entity entity) {
		final double entityHeight = 1.0;
		if (entity instanceof LivingEntity) {
			return Math.max(((LivingEntity) entity).getEyeHeight(), entityHeight);
		} else return entityHeight;
	}

	@Override
	public AlmostBoolean isBlockSolid(final int id) {
		final Material mat = Material.getMaterial(id); 
		if (mat == null) return AlmostBoolean.MAYBE;
		else return AlmostBoolean.match(mat.isSolid());
	}

	@Override
	public AlmostBoolean isBlockLiquid(final int id) {
		final Material mat = Material.getMaterial(id); 
		if (mat == null) return AlmostBoolean.MAYBE;
		switch (mat) {
		case STATIONARY_LAVA:
		case STATIONARY_WATER:
		case WATER:
		case LAVA:
			return AlmostBoolean.YES;
		}
		return AlmostBoolean.NO;
	}

	@Override
	public boolean Block_i(final int id) {
		// TODO: This is inaccurate (would be something like "can suffocate"), however it is used for piling upwards and might about do.
		return BlockProperties.isGround(id) || BlockProperties.isSolid(id);
	}

	@Override
	public double getWidth(final Entity entity) {
		// TODO
		return 0.6;
	}

	@Override
	public AlmostBoolean isIllegalBounds(final Player player) {
		if (player.isDead()) return AlmostBoolean.NO;
		if (!player.isSleeping()){ // TODO: ignored sleeping ?
			// TODO: This can test like ... nothing !
			// (Might not be necessary.)
		}
		return AlmostBoolean.MAYBE;
	}

	@Override
	public double getJumpAmplifier(final Player player) {
		return PotionUtil.getPotionEffectAmplifier(player, PotionEffectType.JUMP);
	}

	@Override
	public double getFasterMovementAmplifier(final Player player) {
		return PotionUtil.getPotionEffectAmplifier(player, PotionEffectType.SPEED);
	}

	@Override
	public int getInvulnerableTicks(final Player player) {
		// TODO: Ahhh...
		return player.getNoDamageTicks();
	}

	@Override
	public void setInvulnerableTicks(final Player player, final int ticks) {
		// TODO: Ahhh...
		player.setLastDamageCause(new EntityDamageEvent(player, DamageCause.CUSTOM, 500));
		player.setNoDamageTicks(ticks);
	}

	@Override
	public void dealFallDamage(final Player player, final int damage) {
		// TODO: account for armor, other.
		player.damage(damage);
	}

	@Override
	public boolean isComplexPart(final Entity entity) {
		return entity instanceof ComplexEntityPart || entity instanceof ComplexLivingEntity;
	}

	@Override
	public boolean shouldBeZombie(final Player player) {
		return player.getHealth() <= 0 || player.isDead();
	}

	@Override
	public void setDead(final Player player, final int deathTicks) {
		// TODO: Test / kick ? ...
		player.setHealth(0);
		player.damage(1);
	}
	
}
