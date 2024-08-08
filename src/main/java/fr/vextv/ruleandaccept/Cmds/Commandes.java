package fr.vextv.ruleandaccept.Cmds;

import fr.vextv.ruleandaccept.Forms.BedrockForm;
import fr.vextv.ruleandaccept.Forms.RulesBook;
import fr.vextv.ruleandaccept.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.IOException;

public class Commandes implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if(sender instanceof Player)
        {
            Player player = ((Player)sender).getPlayer();
            if(msg.equalsIgnoreCase("rules"))
            {
                assert player != null;
                if(Main.getFloodgateApi().isFloodgateId(player.getUniqueId()))
                {
                    try {
                        BedrockForm.openRulesForm(player.getUniqueId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    //ouvrir les règles
                    player.sendMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + "Ouverture des règles");
                    try {
                        RulesBook.OpenRulesBook(player.getUniqueId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            } else if (msg.equalsIgnoreCase("acceptrule")) {
                Main.getChoices().put(player.getUniqueId(), true);
                player.sendMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + ChatColor.GREEN + "Vous venez d'accepter les règles");
            } else if (msg.equalsIgnoreCase("declinerule")) {
                Main.getChoices().put(player.getUniqueId(), false);
                player.sendMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + ChatColor.GREEN + "Vous venez de refuser les règles");
                Bukkit.getServer().broadcastMessage(Main.getInstance().getConfig().get("PluginPrefix") + " " + player.getName() + " A Décliner les règles");
                player.kickPlayer(ChatColor.RED + "Vous devez accepter les règles pour pouvoir accéder au serveur !");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "La commande doit être executée par un joueur !!!");
        }

        return false;
    }
}
