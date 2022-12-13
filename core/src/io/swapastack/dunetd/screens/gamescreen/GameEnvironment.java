package io.swapastack.dunetd.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.math.Vector3;

import lombok.Getter;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public final class GameEnvironment {

    public static final float AMBIENT_LIGHT = 1f;

    private static final int ENVIRONMENT_MAP_BASE_SIZE = 1024;
    private static final int IRRADIANCE_MAP_BASE_SIZE = 256;
    private static final int RADIANCE_MIP_MAP_LEVELS = 10;
    private static final Vector3 LIGHT_DIRECTION = new Vector3(1, -3, 1);

    @Getter
    private final Attribute[] attributes;

    @Getter
    private final DirectionalLightEx light;

    @Getter
    private final SceneSkybox skybox;

    private final Cubemap environmentCubemap;

    private final Cubemap diffuseCubemap;

    private final Cubemap specularCubemap;

    private final Texture brdfLUT;

    public GameEnvironment() {
        // GDX GLTF - Light
        light = new DirectionalLightEx();
        light.direction.set(LIGHT_DIRECTION).nor();
        light.color.set(Color.WHITE);

        // GDX GLTF - Image Based Lighting
        var iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(ENVIRONMENT_MAP_BASE_SIZE);
        diffuseCubemap = iblBuilder.buildIrradianceMap(IRRADIANCE_MAP_BASE_SIZE);
        specularCubemap = iblBuilder.buildRadianceMap(RADIANCE_MIP_MAP_LEVELS);
        iblBuilder.dispose();

        // GDX GLTF - This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        // GDX GLTF - Cubemaps
        attributes = new Attribute[] {
            new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT),
            PBRCubemapAttribute.createSpecularEnv(specularCubemap),
            PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap),
        };

        // GDX GLTF - Skybox
        skybox = new SceneSkybox(environmentCubemap);
    }

    public void dispose() {
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }
}
