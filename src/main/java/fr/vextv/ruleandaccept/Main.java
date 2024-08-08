package fr.vextv.ruleandaccept;

import fr.vextv.ruleandaccept.Cmds.Commandes;
import fr.vextv.ruleandaccept.Events.Events;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private static FloodgateApi floodgateApi;

    private static HashMap<UUID, Boolean> choices = new HashMap<>();


    @Override
    public void onEnable() {
        getLogger().info("Le plugin Démare");

        instance = this;
        floodgateApi = FloodgateApi.getInstance();

        loadChoiceInHashMap();

        getServer().getPluginManager().registerEvents(new Events(), this);
        getConfig().options().copyDefaults(true);
        saveConfig();

        saveResource("rules.txt", false);

        getCommand("rules").setExecutor(new Commandes());
        getCommand("declinerule").setExecutor(new Commandes());
        getCommand("acceptrule").setExecutor(new Commandes());


    }
    @Override
    public void onDisable() {
        getLogger().info("Le plugin D'arrête");

        saveChoicesInConfig();
    }

    public static Main getInstance()
    {
        return instance;
    }

    public static FloodgateApi getFloodgateApi()
    {
        return floodgateApi;
    }

    public static HashMap<UUID, Boolean> getChoices()
    {
        return choices;
    }

    public static void saveChoicesInConfig()
    {
        List<String> list = new ArrayList<>();

        choices.forEach((key, value) -> {
            list.add(key.toString() + ":" + value.toString());
        });
        /*choices.forEach((key, value) -> {
            getConfig().set("PlayersChoices.", key.toString() + ":" + value);
        });*/
        instance.getConfig().set("PlayersChoices", list);
        instance.saveConfig();
    }

    public static void loadChoiceInHashMap()
    {
        instance.getConfig().getStringList("PlayersChoices").forEach((value) -> {
            String pUUID;
            String pBool;


            String[] playerChoice = value.split(":");

            pUUID = playerChoice[0];
            pBool = playerChoice[1];
            choices.put(UUID.fromString(pUUID), Boolean.valueOf(pBool));
        });
    }

    public static String getRawRules() throws IOException {
        // Lecture du fichier rules.txt
        String folderPath = instance.getDataFolder().getAbsolutePath();
        File rulesFile = new File(folderPath, "rules.txt");

        if (!rulesFile.exists()) {
            throw new IOException("Le fichier rules.txt n'existe pas dans le dossier du plugin !");
        }

        Path path = rulesFile.toPath();
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        // Combiner toutes les lignes en une seule chaîne de caractères
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line).append("\n");
        }

        // Nettoyage des retours chariot
        String rawTxt = content.toString().replace("\r\n", "\n").replace("\r", "\n");

        return rawTxt;
    }
}