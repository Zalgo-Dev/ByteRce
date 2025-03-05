package zalgo.dev.mixin.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zalgo.dev.Byterce;

import java.util.ArrayList;

@Mixin(InGameHud.class)
public abstract class HUDMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At("TAIL"))
    public void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client == null || this.client.player == null) {
            return;
        }

        // Liste des textes à afficher
        ArrayList<String> textList = new ArrayList<>();
        textList.add("ByteRce v" + Byterce.getModVersion());
        textList.add("FPS: " + client.getCurrentFps());
        textList.add("Ping: " + client.getNetworkHandler().getPlayerList().stream()
                .filter(entry -> entry.getProfile().getId().equals(client.player.getUuid()))
                .findFirst()
                .map(entry -> entry.getLatency() + "ms")
                .orElse("N/A"));

        // Position initiale
        int x = 5;
        int y = 5;

        // Affichage de chaque ligne
        for (String text : textList) {
            context.drawTextWithShadow(client.textRenderer, Text.of(text), x, y, 0xFFFFFF);
            y += client.textRenderer.fontHeight + 2; // Décalage vertical
        }
    }
}
