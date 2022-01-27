package com.github.hanyaeger.core.factories;

import javafx.stage.Stage;

/**
 * A {@code StageFactory} can be used to create instances of {@link javafx.stage.Stage}.
 */
public class StageFactory {


    /**
     * Create a {@link Stage}.
     *
     * @return an instance of {@link Stage}
     */
    public Stage create() {
        return new Stage();
    }
}
