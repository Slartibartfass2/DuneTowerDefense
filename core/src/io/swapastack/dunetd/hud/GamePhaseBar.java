package io.swapastack.dunetd.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;

import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.game.GameHandler;
import io.swapastack.dunetd.math.PixelsConverter;

import lombok.NonNull;

@SuppressWarnings("squid:S110")
public final class GamePhaseBar extends VisTable {

    // Configuration values
    private static final int GAME_BUILD_PHASE_DURATION_IN_MS = Configuration.getInstance()
            .getIntProperty("GAME_BUILD_PHASE_DURATION_IN_MS");
    private static final int MAX_WAVE_COUNT = Configuration.getInstance().getIntProperty("MAX_WAVE_COUNT");

    private final GameHandler gameHandler;
    private final VisLabel phaseLabel;
    private final VisProgressBar progressBar;

    public GamePhaseBar(@NonNull GameHandler gameHandler) {
        this.gameHandler = gameHandler;

        phaseLabel = new VisLabel();
        progressBar = new VisProgressBar(0f, 100f, 1f, false);

        setFillParent(true);
        align(Align.top);
        padTop(PixelsConverter.getY(0.005f));

        var horizontalOffset = PixelsConverter.getX(0.35f);
        add(phaseLabel).padBottom(PixelsConverter.getY(0.005f)).row();
        add(progressBar).padLeft(horizontalOffset).padRight(horizontalOffset).growX();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        var gamePhase = gameHandler.getGamePhase();
        var progressBarVisibility = true;

        // Set phase label and progress bar
        if (!gameHandler.isGameStarted()) {
            phaseLabel.setText("Build your first tower to start the game");
            progressBarVisibility = false;
        } else {
            switch (gamePhase) {
                case BUILD_PHASE -> {
                    phaseLabel.setText(String.format("Build phase: %2.1f seconds remaining",
                            gameHandler.getRemainingBuildPhaseDurationInMs() / 1000f));
                    progressBar.setRange(0f, GAME_BUILD_PHASE_DURATION_IN_MS);
                    progressBar.setValue(gameHandler.getRemainingBuildPhaseDurationInMs());
                }
                case WAVE_PHASE -> {
                    phaseLabel.setText(String.format("Wave phase %d/%d: %d hostile units remaining",
                            gameHandler.getWaveNumber(), MAX_WAVE_COUNT,
                            gameHandler.getNumberOfRemainingHostileUnits()));
                    progressBar.setRange(0f, gameHandler.getWaveHostileUnitCount());
                    progressBar.setValue(gameHandler.getNumberOfRemainingHostileUnits());
                }
                case GAME_LOST_PHASE -> {
                    phaseLabel.setText("Game over!");
                    progressBarVisibility = false;
                }
                case GAME_WON_PHASE -> {
                    phaseLabel.setText("Game won!");
                    progressBarVisibility = false;
                }
                default -> throw new IllegalStateException("Unexpected value: " + gamePhase);
            }
        }

        progressBar.setVisible(progressBarVisibility);
    }
}
