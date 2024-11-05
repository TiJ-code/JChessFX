package dk.tij.jchessfx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import dk.tij.jchessfx.model.BoardModel;
import dk.tij.jchessfx.struct.KeyInfo;
import dk.tij.jchessfx.views.BoardView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.HashMap;
import java.util.Map;

public class Game extends GameApplication {
    private BoardModel boardModel;
    private BoardView boardView;
    private Map<KeyCode, KeyInfo> keys;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("JChessFX");
        gameSettings.setVersion("0.1");
        gameSettings.setHeight(Configuration.height);
        gameSettings.setWidth(Configuration.width);
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setTicksPerSecond(60);
        keys = new HashMap<KeyCode, KeyInfo>();
        keys.put(KeyCode.LEFT, new KeyInfo("LEFT", () -> boardModel.goLeft()));
        keys.put(KeyCode.RIGHT, new KeyInfo("RIGHT", () -> boardModel.goRight()));
        keys.put(KeyCode.UP, new KeyInfo("UP", () -> boardModel.goUp()));
        keys.put(KeyCode.DOWN, new KeyInfo("DOWN", () -> boardModel.goDown()));
        keys.put(KeyCode.SPACE, new KeyInfo("SPACE", () -> boardModel.quitSelection()));
        keys.put(KeyCode.ENTER, new KeyInfo("ENTER", () -> boardModel.select()));
        keys.put(KeyCode.BACK_SPACE, new KeyInfo("BACK_SPACE", () -> boardModel.back()));
        keys.put(KeyCode.TAB, new KeyInfo("TAB", () -> boardModel.back()));
    }

    @Override
    protected void initGame() {
        boardModel = new BoardModel();
        boardView = new BoardView(boardModel.getTileModels());
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        keys.forEach((code, info) -> {
            input.addAction(new UserAction(info.getName()) {
                @Override
                protected void onAction() {
                    if (info.getCooldown() <= 0) {
                        info.getAction().run();
                        info.setCooldown(Configuration.cooldown);
                    }
                }
            }, code);
        });
        input.addAction(new UserAction("PRIMARY") {
            @Override
            protected void onActionBegin() {
                Vec2 mousePosition = new Vec2(input.getMousePositionWorld().getX(), input.getMousePositionWorld().getY());
                boardModel.mouseSelect(mousePosition);
            }

            @Override
            protected void onActionEnd() {
                Vec2 mousePosition = new Vec2(input.getMousePositionWorld().getX(), input.getMousePositionWorld().getY());
                boardModel.mouseSelect(mousePosition, false);
            }
        }, MouseButton.PRIMARY);
        input.addAction(new UserAction("SECONDARY") {
            @Override
            protected void onActionBegin() {
                boardModel.quitSelection();
            }
        }, MouseButton.SECONDARY);
    }

    @Override
    protected void onUpdate(double tpf) {
        keys.forEach((code, info) -> {
            if (info.getCooldown() > 0)
                info.setCooldown(info.getCooldown() - tpf);
        });
        boardModel.onUpdate();
    }
}
