package fr.vextv.ruleandaccept.Events;

import fr.vextv.ruleandaccept.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class Events implements Listener {

    private Main main = Main.getInstance();



    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        UUID pUUID = event.getPlayer().getUniqueId();

        if (!player.hasPlayedBefore())
        {
            player.sendMessage(ChatColor.RED + (String) Main.getInstance().getConfig().get("AcceptRulesWarning"));
            player.performCommand("rules");
        }


    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        UUID pUUID = event.getPlayer().getUniqueId();


        if (Main.getChoices().get(pUUID) == null || !Main.getChoices().get(pUUID))
        {


            Location from = event.getFrom();

            player.sendMessage(ChatColor.RED + (String) Main.getInstance().getConfig().get("AcceptRulesWarning"));
            player.teleport(from);
        }
    }
}
