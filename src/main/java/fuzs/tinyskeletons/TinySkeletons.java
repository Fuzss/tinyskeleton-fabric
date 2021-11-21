package fuzs.tinyskeletons;

import fuzs.tinyskeletons.api.event.player.EntityInteractCallback;
import fuzs.tinyskeletons.handler.BabyConversionHandler;
import fuzs.tinyskeletons.registry.ModRegistry;
import fuzs.tinyskeletons.world.entity.monster.BabyStray;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TinySkeletons implements ModInitializer {
    public static final String MOD_ID = "tinyskeletons";
    public static final String MOD_NAME = "Tiny Skeletons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        onConstructMod();
        onCommonSetup();
        onEntityAttributeCreation();
    }

    public static void onConstructMod() {
        final BabyConversionHandler handler = new BabyConversionHandler();
        EntityInteractCallback.EVENT.register(handler::onEntityInteract);
        ModRegistry.touch();
    }

    public static void onCommonSetup() {
        SpawnRestrictionAccessor.callRegister(ModRegistry.BABY_SKELETON_ENTITY_TYPE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnRestrictionAccessor.callRegister(ModRegistry.BABY_WITHER_SKELETON_ENTITY_TYPE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnRestrictionAccessor.callRegister(ModRegistry.BABY_STRAY_ENTITY_TYPE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BabyStray::checkBabyStraySpawnRules);
        BabyConversionHandler.registerConversion(EntityType.SKELETON, ModRegistry.BABY_SKELETON_ENTITY_TYPE);
        BabyConversionHandler.registerConversion(EntityType.WITHER_SKELETON, ModRegistry.BABY_WITHER_SKELETON_ENTITY_TYPE);
        BabyConversionHandler.registerConversion(EntityType.STRAY, ModRegistry.BABY_STRAY_ENTITY_TYPE);
    }

    public static void onEntityAttributeCreation() {
        FabricDefaultAttributeRegistry.register(ModRegistry.BABY_SKELETON_ENTITY_TYPE, Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.375));
        FabricDefaultAttributeRegistry.register(ModRegistry.BABY_WITHER_SKELETON_ENTITY_TYPE, Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.375));
        FabricDefaultAttributeRegistry.register(ModRegistry.BABY_STRAY_ENTITY_TYPE, Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.375));
    }
}
