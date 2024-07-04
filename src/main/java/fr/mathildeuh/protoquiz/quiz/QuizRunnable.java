package fr.mathildeuh.protoquiz.quiz;

import fr.mathildeuh.protoquiz.ProtoQuiz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizRunnable extends BukkitRunnable {
    private final QuizData quizData;

    public QuizRunnable(QuizData quizData) {
        this.quizData = quizData;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        if (players.isEmpty()) return;

        Player player = players.get(0);
        startQuiz(player);
    }

    private void startQuiz(Player player) {
        List<String> questions = quizData.getQuestions();
        Random random = new Random();
        String question = questions.get(random.nextInt(questions.size()));
        String answer = quizData.getAnswer(question);


        Bukkit.broadcastMessage("§6" + player.getName() + " a été provoqué par Protovun !");
        Bukkit.broadcastMessage("§6Protovun lui a posé la question suivante:");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.GREEN + question);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("§aD'après vous, sera t'il capable de répondre à la question et de continuer le jeu ?");


        player.kickPlayer("§6Protovun vous a défié, voici un quiz !\n" + ChatColor.GREEN + question);
        player.setMetadata("quizAnswer", new FixedMetadataValue(JavaPlugin.getPlugin(ProtoQuiz.class), answer));
        player.setMetadata("quizQuestion", new FixedMetadataValue(JavaPlugin.getPlugin(ProtoQuiz.class), question));

    }
}
