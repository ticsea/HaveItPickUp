package me.afk.haveitpickup;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HaveItPickup.MODID)
public class HaveItPickup
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "haveitpickup";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public HaveItPickup(FMLJavaModLoadingContext context)
    {
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
