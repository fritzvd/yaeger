package nl.meron.yaeger.engine.entities.entity.sprites;

import com.google.inject.Injector;
import javafx.geometry.BoundingBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.meron.yaeger.engine.entities.entity.Point;
import nl.meron.yaeger.engine.entities.entity.sprites.delegates.SpriteAnimationDelegate;
import nl.meron.yaeger.engine.entities.entity.sprites.movement.MovementVector;
import nl.meron.yaeger.engine.media.repositories.ImageRepository;
import nl.meron.yaeger.javafx.image.ImageViewFactory;
import nl.meron.yaeger.module.factories.SpriteAnimationDelegateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdatableSpriteEntityTest {

    private final static String DEFAULT_RESOURCE = "images/bubble.png";
    private final static int X_POSITION = 37;
    private final static int Y_POSITION = 37;
    private final static Point DEFAULT_POINT = new Point(X_POSITION, Y_POSITION);
    private final static int WIDTH = 39;
    private final static int HEIGHT = 41;
    private final static Size DEFAULT_SIZE = new Size(WIDTH, HEIGHT);
    private final static Double DIRECTION = MovementVector.Direction.UP;
    private final static int SPEED = 1;

    private final static MovementVector MOVEMENT_VECTOR = new MovementVector(DIRECTION, SPEED);

    private SpriteAnimationDelegateFactory spriteAnimationDelegateFactory;
    private ImageViewFactory imageViewFactory;
    private SpriteAnimationDelegate spriteAnimationDelegate;
    private ImageRepository imageRepository;
    private Injector injector;

    @BeforeEach
    void setup() {
        imageViewFactory = mock(ImageViewFactory.class);
        spriteAnimationDelegate = mock(SpriteAnimationDelegate.class);
        spriteAnimationDelegateFactory = mock(SpriteAnimationDelegateFactory.class);

        imageRepository = mock(ImageRepository.class);
        injector = mock(Injector.class);
    }

    @Test
    void instantiatingAnUpdatableSpriteEntityWithOneFrameGivesNoSideEffects() {
        // Setup

        // Test
        var updatableSpriteEntity = new TestUpdatableSpriteEntity(DEFAULT_RESOURCE, DEFAULT_POINT, DEFAULT_SIZE);

        // Verify
        Assertions.assertNotNull(updatableSpriteEntity);
    }

    @Test
    void setAutocycleDoesNotBreakWhithOnlyOneFrame() {
        // Setup
        var autocycleValue = 37;
        var image = mock(Image.class);
        var imageView = mock(ImageView.class);
        var updatableSpriteEntity = new TestUpdatableSpriteEntity(DEFAULT_RESOURCE, DEFAULT_POINT, DEFAULT_SIZE);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        updatableSpriteEntity.setImageRepository(imageRepository);
        updatableSpriteEntity.setImageViewFactory(imageViewFactory);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);

        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        when(imageViewFactory.create(image)).thenReturn(imageView);
        when(spriteAnimationDelegateFactory.create(imageView, 1)).thenReturn(spriteAnimationDelegate);

        updatableSpriteEntity.setAutoCycle(autocycleValue);

        // Test
        updatableSpriteEntity.init(injector);

        // Verify
    }

    @Test
    void autocycleGetsDelegatedToSpriteAnimationDelegate() {
        // Setup
        var frames = 2;
        var image = mock(Image.class);
        var imageView = mock(ImageView.class);
        var updatableSpriteEntity = new AutoCyclingUpdatableSpriteEntity(DEFAULT_RESOURCE, DEFAULT_POINT, DEFAULT_SIZE, frames);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);
        updatableSpriteEntity.setImageRepository(imageRepository);
        updatableSpriteEntity.setImageViewFactory(imageViewFactory);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);

        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH * frames, HEIGHT, true)).thenReturn(image);

        when(imageViewFactory.create(image)).thenReturn(imageView);
        when(spriteAnimationDelegateFactory.create(imageView, frames)).thenReturn(spriteAnimationDelegate);

        // Test
        updatableSpriteEntity.init(injector);

        // Verify
        verify(spriteAnimationDelegate).setAutoCycle(2);
    }

    @Test
    void updateUpdatesLocation() {
        // Setup
        var updatableSpriteEntity = new TestUpdatableSpriteEntity(DEFAULT_RESOURCE, DEFAULT_POINT, DEFAULT_SIZE, 1, MOVEMENT_VECTOR);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);

        updatableSpriteEntity.setImageRepository(imageRepository);
        updatableSpriteEntity.setImageViewFactory(imageViewFactory);
        updatableSpriteEntity.setSpriteAnimationDelegateFactory(spriteAnimationDelegateFactory);

        var image = mock(Image.class);
        when(imageRepository.get(DEFAULT_RESOURCE, WIDTH, HEIGHT, true)).thenReturn(image);

        var imageView = mock(ImageView.class);
        when(imageViewFactory.create(image)).thenReturn(imageView);

        var bounds = new BoundingBox(0d, 0d, 10d, 10d);
        when(imageView.getLayoutBounds()).thenReturn(bounds);

        var scene = mock(Scene.class);
        when(scene.getWidth()).thenReturn(100d);
        when(scene.getHeight()).thenReturn(100d);

        when(imageView.getScene()).thenReturn(scene);
        updatableSpriteEntity.init(injector);

        // Test
        updatableSpriteEntity.update(1l);

        // Verify
        assertNotEquals(DEFAULT_POINT, updatableSpriteEntity.getAnchorPoint());
    }

    private class TestUpdatableSpriteEntity extends UpdatableSpriteEntity {

        TestUpdatableSpriteEntity(String resource, Point point, Size size) {
            super(resource, point, size);
        }

        TestUpdatableSpriteEntity(String resource, Point point, Size size, int frames, MovementVector movementVector) {
            super(resource, point, size, frames, movementVector);
        }
    }

    private class AutoCyclingUpdatableSpriteEntity extends UpdatableSpriteEntity {

        AutoCyclingUpdatableSpriteEntity(String resource, Point point, Size size, int frames) {
            super(resource, point, size, frames);
            setAutoCycle(2);
        }
    }
}