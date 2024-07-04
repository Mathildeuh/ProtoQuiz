package fr.mathildeuh.protoquiz.quiz;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizData {
    private final List<String> questions = new ArrayList<>();
    private final Map<String, String> answers = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public QuizData(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "quiz.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("quiz.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadQuestions();
    }

    private void loadQuestions() {
        questions.clear();
        answers.clear();

        List<Map<?, ?>> questionList = config.getMapList("questions");
        for (Map<?, ?> map : questionList) {
            String question = (String) map.get("question");
            String answer = (String) map.get("answer");
            if (question != null && answer != null) {
                questions.add(question);
                answers.put(question, answer);
            }
        }
    }

    public List<String> getQuestions() {
        return new ArrayList<>(questions);
    }

    public String getAnswer(String question) {
        return answers.get(question);
    }

}
