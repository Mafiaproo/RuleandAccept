package fr.vextv.ruleandaccept.Forms;

import com.google.common.io.ByteStreams;
import fr.vextv.ruleandaccept.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class RulesBook {

    public static void OpenRulesBook(UUID playerUUID) throws IOException {
        Player player = Bukkit.getPlayer(playerUUID);
        List<String> chunks = new ArrayList<>();

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        String rawTxt = Main.getRawRules();

        assert bookMeta != null;
        bookMeta.setTitle((String) Main.getInstance().getConfig().get("BookTitle"));
        bookMeta.setAuthor(Bukkit.getServer().getName());

        // Découper le texte en segments, en essayant de ne pas couper les mots
        int maxPageLength = 255;
        String[] paragraphs = rawTxt.split("\n\n"); // Diviser par double saut de ligne pour les paragraphes

        StringBuilder page = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (page.length() + paragraph.length() > maxPageLength) {
                chunks.add(page.toString());
                page = new StringBuilder(paragraph);
            } else {
                if (page.length() != 0) {
                    page.append("\n\n");
                }
                page.append(paragraph);
            }
        }
        if (page.length() > 0) {
            chunks.add(page.toString());
        }

        // Ajouter les pages au livre
        bookMeta.setPages(chunks);

        // Création de la page interactive avec les boutons
        BaseComponent[] formBook =
                new ComponentBuilder(
                        "  ")
                        .append((String) Main.getInstance().getConfig().get("AcceptButton"))
                        .color(ChatColor.GREEN).bold(true).underlined(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptrule"))
                        .append(" | ")
                        .color(ChatColor.BLACK).bold(true).underlined(false)
                        .append((String) Main.getInstance().getConfig().get("DeclineButton"))
                        .color(ChatColor.RED).bold(true).underlined(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/declinerule"))
                        .create();

        bookMeta.spigot().addPage(formBook);
        book.setItemMeta(bookMeta);

        player.openBook(book);
    }


}
