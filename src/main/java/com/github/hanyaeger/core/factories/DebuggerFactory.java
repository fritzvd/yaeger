package com.github.hanyaeger.core.factories;

import com.github.hanyaeger.core.DebuggerStage;
import com.google.inject.Inject;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A {@code DebuggerFactory} can be used to create a specific implementation of a {@link javafx.stage.Stage} designed
 * to become a debugger for Yaeger.
 */
public class DebuggerFactory {

    private static final String STAGE_TITLE = "Yaeger Debugger";

    private BackgroundFactory backgroundFactory;

    /**
     * Create a {@link DebuggerStage} that encapsulates the Yaeger Debugger.
     *
     * @return an instance of {@link DebuggerStage}
     */
    public DebuggerStage create() {
        var stage = new DebuggerStage();
        stage.setTitle(STAGE_TITLE);

        var pane = new Pane();

        pane.setBackground(backgroundFactory.createFillBackground(Color.BLACK));

        var scene = new Scene(pane);
        stage.setScene(scene);

        return stage;
    }

    /**
     * Set the {@link BackgroundFactory} to be used for this {@code BackgroundDelegate}.
     *
     * @param backgroundFactory the {@link BackgroundFactory} to be used
     */
    @Inject
    public void setBackgroundFactory(BackgroundFactory backgroundFactory) {
        this.backgroundFactory = backgroundFactory;
    }
}
