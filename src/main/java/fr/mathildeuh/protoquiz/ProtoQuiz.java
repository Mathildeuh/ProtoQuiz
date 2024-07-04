package fr.mathildeuh.protoquiz;

import fr.mathildeuh.protoquiz.quiz.QuizData;
import fr.mathildeuh.protoquiz.quiz.QuizRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public final class ProtoQuiz extends JavaPlugin implements Listener {
    private QuizData quizData;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        // Initialize QuizData
        quizData = new QuizData(this);
        getLogger().info("Loaded questions: " + quizData.getQuestions().size());

        // Start the quiz task
        new QuizRunnable(quizData).runTaskTimer(this, 0, 36000); // Execute every 30 minutes (36000 ticks)
    }

    public QuizData getQuizData() {
        return quizData;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("quizAnswer")) {
            String correctAnswer = player.getMetadata("quizAnswer").get(0).asString();
            if (event.getMessage().equalsIgnoreCase(correctAnswer)) {
                Bukkit.getScheduler().runTask(this, () -> {
                    player.sendMessage(ChatColor.GREEN + "Bonne réponse! Vous pouvez maintenant jouer.");
                    player.removeMetadata("quizAnswer", this);
                    player.removeMetadata("quizQuestion", this);
                });
            } else {
                Bukkit.getScheduler().runTask(this, () -> {
                    List<String> questions = quizData.getQuestions();
                    Random random = new Random();
                    String question = questions.get(random.nextInt(questions.size()));
                    String answer = quizData.getAnswer(question);
                    player.setMetadata("quizAnswer", new FixedMetadataValue(this, answer));
                    player.setMetadata("quizQuestion", new FixedMetadataValue(this, question));
                    player.kickPlayer(ChatColor.RED + "Mauvaise réponse! La bonne réponse était : " + correctAnswer + ChatColor.GREEN + "\n\nNouvelle question: " + question);
                });
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("quizQuestion")) {
            String question = player.getMetadata("quizQuestion").get(0).asString();
            player.sendMessage(ChatColor.RED + "Vous devez répondre à la question pour pouvoir jouer.");
            player.sendMessage(ChatColor.GREEN + question);
            // Freeze the player
            new FreezePlayerTask(player).runTaskTimer(this, 0L, 20L);
        }
    }

    private static class FreezePlayerTask extends BukkitRunnable {
        private final Player player;
        private final Location freezeLocation;

        public FreezePlayerTask(Player player) {
            this.player = player;
            this.freezeLocation = player.getLocation();
        }

        @Override
        public void run() {
            if (player.hasMetadata("quizAnswer")) {
                player.teleport(freezeLocation); // Prevents player from moving
            } else {
                cancel(); // Stop task if player has answered correctly
            }
        }
    }
}
