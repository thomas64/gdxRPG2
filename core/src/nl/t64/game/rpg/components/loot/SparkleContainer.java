package nl.t64.game.rpg.components.loot;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;


public class SparkleContainer {

    private static final String SPARKLE_CONFIGS = "configs/loot/";
    private static final String FILE_LIST = SPARKLE_CONFIGS + "_files_sparkles.txt";

    private final Map<String, Loot> sparkles;

    public SparkleContainer() {
        this.sparkles = new HashMap<>();
        try {
            this.loadSparkles();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Loot getSparkle(String sparkleId) {
        return sparkles.get(sparkleId);
    }

    private void loadSparkles() throws IOException {
        var mapper = new ObjectMapper();
        var typeReference = new TypeReference<HashMap<String, Loot>>() {
        };
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        for (String filePath : configFiles) {
            String json = Gdx.files.local(SPARKLE_CONFIGS + filePath).readString();
            sparkles.putAll(mapper.readValue(json, typeReference));
        }
    }

}
