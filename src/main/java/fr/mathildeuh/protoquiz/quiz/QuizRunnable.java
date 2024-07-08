package fr.mathildeuh.protoquiz.quiz;

import fr.mathildeuh.protoquiz.ProtoQuiz;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizRunnable extends BukkitRunnable {
    private static QuizData quizData;

    public QuizRunnable(QuizData quizData) {
        QuizRunnable.quizData = quizData;
    }

    public static void startQuiz(Player player) {
        List<String> questions = quizData.getQuestions();
        Random random = new Random();
        String question = questions.get(random.nextInt(questions.size()));
        String answer = quizData.getAnswer(question);

        spawnNPC(player);

        player.sendTitle("§5Protovun:", "§aC'est l'heure du QU.. QU.. QU.. QUIZ !", 10, 20 * 10, 20);

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(ProtoQuiz.class), () -> {
            player.kickPlayer("§5Protovun §6vous a défié, voici un quiz !\n" + ChatColor.GREEN + question + "\n\n§aReconnectez vous pour répondre dans le chat.");
        }, 20 * 10);

        player.setMetadata("quizAnswer", new FixedMetadataValue(JavaPlugin.getPlugin(ProtoQuiz.class), answer));
        player.setMetadata("quizQuestion", new FixedMetadataValue(JavaPlugin.getPlugin(ProtoQuiz.class), question));
    }

    private static void spawnNPC(Player player) {
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();

        // Déplace le point de spawn 5 blocs devant le joueur
        Location spawnLocation = playerLocation.add(playerDirection.multiply(3));
        spawnLocation.setYaw(playerLocation.getYaw() - 180);
        spawnLocation.setPitch(playerLocation.getPitch());

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "§5Protovun");
        npc.spawn(spawnLocation);

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(ProtoQuiz.class), () -> {
            npc.destroy();
        }, 20 * 10);
    }

    @Override
    public void run() {
        List<Player> players = new java.util.ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        if (players.isEmpty()) return;

        Player player = players.get(0);
        startQuiz(player);
    }
}
