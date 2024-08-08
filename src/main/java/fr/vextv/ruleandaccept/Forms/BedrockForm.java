package fr.vextv.ruleandaccept.Forms;

import fr.vextv.ruleandaccept.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.cumulus.response.ModalFormResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class BedrockForm {

    public static void openRulesForm(UUID uuid) throws IOException {
        FormBuilder<ModalForm.Builder, ModalForm, ModalFormResponse> form = ModalForm.builder()
                .title((String) Objects.requireNonNull(Main.getInstance().getConfig().get("FormTitle")))
                .content(Main.getRawRules())
                .button1((String) Objects.requireNonNull(Main.getInstance().getConfig().get("FormAcceptButton")))
                .button2((String) Objects.requireNonNull(Main.getInstance().getConfig().get("FormDeclineButton")))
                .closedOrInvalidResultHandler(() -> Objects.requireNonNull(Bukkit.getPlayer(uuid)).performCommand("declinerule"))
                .validResultHandler(response -> BedrockForm.checkChoice(response.clickedButtonId(), uuid));
                ;
        Main.getFloodgateApi().sendForm(uuid, form);
    }

    private static void checkChoice(int btnId, UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
        if(btnId == 0)
        {

            Main.getChoices().put(uuid, true);
            assert player != null;
            player.sendMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + ChatColor.GREEN + "Vous venez d'accepter les règles");
        }
        else
        {

            assert player != null;
            Main.getChoices().put(player.getUniqueId(), false);
            player.sendMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + ChatColor.GREEN + "Vous venez de refuser les règles");
            Bukkit.getServer().broadcastMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + player.getName() + " A Décliner les règles");
            player.kickPlayer(ChatColor.RED + "Vous devez accepter les règles pour pouvoir accéder au serveur !");
        }
    }
}
