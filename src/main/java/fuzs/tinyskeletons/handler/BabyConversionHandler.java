package fuzs.tinyskeletons.handler;

import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;

public class BabyConversionHandler {
    private static final Map<EntityType<? extends Mob>, EntityType<? extends Mob>> BABY_MOB_CONVERSIONS = Maps.newHashMap();

    public static void registerConversion(EntityType<? extends Mob> targetMob, EntityType<? extends Mob> convertsTo) {
        BABY_MOB_CONVERSIONS.put(targetMob, convertsTo);
    }

    public boolean onMobCreate(Level level, Mob mob, MobSpawnType spawnReason) {
        // spawner type shouldn't end up here anyways, but just to make sure
        // would break balancing for baby wither skeletons
        // also exclude summoned by command as this would break forcefully spawning an adult skeleton since there is no baby flag as with zombies which could force that otherwise
        if (spawnReason != MobSpawnType.SPAWNER && spawnReason != MobSpawnType.COMMAND) {
            if (level instanceof ServerLevel serverLevel && Zombie.getSpawnAsBabyOdds(serverLevel.getRandom())) {
                EntityType<? extends Mob> babyType = BABY_MOB_CONVERSIONS.get(mob.getType());
                if (babyType != null) {
                    return makeBabyMob(serverLevel, babyType, mob, spawnReason).isPresent();
                }
            }
        }
        return false;
    }

    public InteractionResult onEntityInteract(Player player, Level world, InteractionHand hand, Entity target) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (target.isAlive() && itemstack.getItem() instanceof SpawnEggItem) {
            EntityType<?> eggType = ((SpawnEggItem) itemstack.getItem()).getType(itemstack.getTag());
            EntityType<? extends Mob> babyType = BABY_MOB_CONVERSIONS.get(eggType);
            if (babyType != null && (target.getType() == babyType || target.getType() == eggType)) {
                if (world instanceof ServerLevel level) {
                    final Optional<Mob> mob = makeBabyMob(level, babyType, target, MobSpawnType.SPAWN_EGG);
                    if (mob.isPresent()) {
                        this.finalizeSpawnEggMob(mob.get(), itemstack, player);
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.PASS;
                } else {
                    return InteractionResult.CONSUME;
                }
            }
        }
        return null;
    }

    private void finalizeSpawnEggMob(Mob mobentity, ItemStack itemstack, Player player) {
        mobentity.playAmbientSound();
        if (itemstack.hasCustomHoverName()) {
            mobentity.setCustomName(itemstack.getHoverName());
        }
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
    }

    private static Optional<Mob> makeBabyMob(ServerLevel level, EntityType<? extends Mob> entityType, Entity parent, MobSpawnType spawnReason) {
        Mob mobentity;
        if (parent instanceof AgeableMob ageableMob) {
            mobentity = ageableMob.getBreedOffspring(level, ageableMob);
        } else {
            mobentity = entityType.create(level);
        }
        if (mobentity == null) {
            return Optional.empty();
        }
        if (!mobentity.isBaby()) {
            throw new RuntimeException("baby mob must be a baby by default");
        }
        mobentity.moveTo(parent.getX(), parent.getY(), parent.getZ(), Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
        level.addFreshEntityWithPassengers(mobentity);
        mobentity.yHeadRot = mobentity.getYRot();
        mobentity.yBodyRot = mobentity.getYRot();
        mobentity.finalizeSpawn(level, level.getCurrentDifficultyAt(mobentity.blockPosition()), spawnReason, null, null);
        return Optional.of(mobentity);
    }
}
