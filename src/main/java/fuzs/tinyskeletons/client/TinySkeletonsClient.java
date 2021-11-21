package fuzs.tinyskeletons.client;

import fuzs.tinyskeletons.client.registry.ModClientRegistry;
import fuzs.tinyskeletons.client.renderer.entity.BabySkeletonRenderer;
import fuzs.tinyskeletons.client.renderer.entity.BabyWitherSkeletonRenderer;
import fuzs.tinyskeletons.registry.ModRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.StrayRenderer;

public class TinySkeletonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        onRegisterRenderers();
        onRegisterLayerDefinitions();
    }

    public static void onRegisterRenderers() {
        EntityRendererRegistry.register(ModRegistry.BABY_SKELETON_ENTITY_TYPE, BabySkeletonRenderer::new);
        EntityRendererRegistry.register(ModRegistry.BABY_WITHER_SKELETON_ENTITY_TYPE, BabyWitherSkeletonRenderer::new);
        EntityRendererRegistry.register(ModRegistry.BABY_STRAY_ENTITY_TYPE, StrayRenderer::new);
    }

    public static void onRegisterLayerDefinitions() {
        EntityModelLayerRegistry.TexturedModelDataProvider skeletonLayer = SkeletonModel::createBodyLayer;
        EntityModelLayerRegistry.TexturedModelDataProvider innerArmorLayer = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5f), 0.0F), 64, 32);
        EntityModelLayerRegistry.TexturedModelDataProvider outerArmorLayer = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0f), 0.0F), 64, 32);
        EntityModelLayerRegistry.TexturedModelDataProvider strayOuterLayer = () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F), 64, 32);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_SKELETON, skeletonLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_SKELETON_INNER_ARMOR, innerArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_SKELETON_OUTER_ARMOR, outerArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_STRAY, skeletonLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_STRAY_INNER_ARMOR, innerArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_STRAY_OUTER_ARMOR, outerArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_STRAY_OUTER_LAYER, strayOuterLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_WITHER_SKELETON, skeletonLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_WITHER_SKELETON_INNER_ARMOR, innerArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(ModClientRegistry.BABY_WITHER_SKELETON_OUTER_ARMOR, outerArmorLayer);
    }
}
