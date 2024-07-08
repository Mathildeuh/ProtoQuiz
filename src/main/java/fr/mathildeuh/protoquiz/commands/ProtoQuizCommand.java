package fr.mathildeuh.protoquiz.commands;

import fr.mathildeuh.protoquiz.ProtoQuiz;
import fr.mathildeuh.protoquiz.quiz.QuizRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ProtoQuizCommand implements CommandExecutor {
    public ProtoQuizCommand(ProtoQuiz protoQuiz) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof CommandBlock) {
            Location commandBlockLocation = ((CommandBlock) sender).getLocation();
            Player nearestPlayer = null;
            double nearestDistance = Double.MAX_VALUE;

            for (Player player : Bukkit.getOnlinePlayers()) {
                double distance = player.getLocation().distance(commandBlockLocation);
                if (distance < nearestDistance) {
                    nearestPlayer = player;
                    nearestDistance = distance;
                }
            }

            if (nearestPlayer != null) {
                // Ici, vous pouvez utiliser nearestPlayer pour ce que vous avez besoin
                // Par exemple, démarrer un quiz pour le joueur le plus proche
                QuizRunnable.startQuiz(nearestPlayer);
            }
            return true;
        }
        List<Player> players = new java.util.ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        if (players.isEmpty()) return false;

        Player player = players.get(0);
        QuizRunnable.startQuiz(player);

        return true;
    }
}
