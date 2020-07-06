package com.github.hanyaeger.api.engine.entities.entity.sprite;

import com.github.hanyaeger.api.engine.entities.EntityCollection;
import com.github.hanyaeger.api.engine.entities.entity.motion.BufferedMoveable;
import com.github.hanyaeger.api.engine.entities.entity.motion.EntityMotionInitBuffer;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.github.hanyaeger.api.engine.entities.entity.ContinuousRotatable;
import com.github.hanyaeger.api.engine.entities.entity.Location;
import com.github.hanyaeger.api.engine.entities.entity.motion.DefaultMotionApplier;
import com.github.hanyaeger.api.engine.Size;
import com.github.hanyaeger.api.engine.Updatable;
import com.github.hanyaeger.api.engine.UpdateDelegator;
import com.github.hanyaeger.api.engine.Updater;
import com.github.hanyaeger.api.engine.entities.entity.motion.MotionApplier;

import java.util.Optional;

/**
 * A {@link DynamicSpriteEntity} extends all behaviour of a {@link SpriteEntity}, but also implements the
 * {@link Updatable} Interface.
 */
public abstract class DynamicSpriteEntity extends SpriteEntity implements UpdateDelegator, BufferedMoveable, ContinuousRotatable {

    private DefaultMotionApplier motionApplier;
    private long autoCycleInterval = 0;
    private Updater updater;
    private Optional<EntityMotionInitBuffer> buffer;
    private double rotationAngle;

    /**
     * Create a new {@link DynamicSpriteEntity} for the given image resource on the given {@link Location}, with the given {@link Size}.
     *
     * @param resource        The url of the image file. Relative to the resources folder.
     * @param initialLocation the initial {@link Location} of this Entity
     * @param size            The {@link Size} (width and height) with which the image should be shown. This {@link Size}
     *                        will also be used as the {@link javafx.geometry.BoundingBox} in case of collision detection.
     */
    public DynamicSpriteEntity(final String resource, final Location initialLocation, final Size size) {
        this(resource, initialLocation, size, 1);
    }

    /**
     * Create a new {@link DynamicSpriteEntity} for the given image resource on the given {@link Location},
     * with the given {@link Size}.
     *
     * @param resource        The url of the image file. Relative to the resources folder.
     * @param initialLocation the initial {@link Location} of this Entity
     * @param size            The {@link Size} (width and height) that should be used. The height and width divided by the
     *                        number of frames will be used for displaying the Image and as the {@link javafx.geometry.BoundingBox} in case of collision detection.
     * @param frames          The number of frames the Image contains. By default the first frame is loaded.
     */
    public DynamicSpriteEntity(final String resource, final Location initialLocation, final Size size, final int frames) {
        super(resource, initialLocation, size, frames);

        buffer = Optional.of(new EntityMotionInitBuffer());
    }

    /**
     * Set the interval at which the sprite should be automatically cycled.
     *
     * @param interval The interval milli-seconds.
     */
    protected void setAutoCycle(final long interval) {
        this.autoCycleInterval = interval;
    }

    @Override
    public MotionApplier getMotionApplier() {
        return motionApplier;
    }

    @Override
    public Updater getUpdater() {
        return updater;
    }

    @Override
    public void init(final Injector injector) {
        super.init(injector);

        if (spriteAnimationDelegate != null) {
            updater.addUpdatable(spriteAnimationDelegate);
        }
        if (getFrames() > 1 && autoCycleInterval != 0) {
            spriteAnimationDelegate.setAutoCycle(autoCycleInterval);
        }
        buffer.ifPresent(entityMotionInitBuffer -> {
            entityMotionInitBuffer.setMotionApplier(motionApplier);
            entityMotionInitBuffer.init(injector);
        });
        buffer = Optional.empty();
    }

    @Override
    public void addToEntityCollection(EntityCollection collection) {
        collection.addDynamicEntity(this);
    }

    @Override
    public Optional<EntityMotionInitBuffer> getBuffer() {
        return buffer;
    }

    @Override
    public void setRotationSpeed(final double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    @Inject
    public void setUpdater(final Updater updater) {
        this.updater = updater;
    }

    @Override
    public double getRotationSpeed() {
        return rotationAngle;
    }

    @Inject
    @Override
    public void setMotionApplier(final DefaultMotionApplier motionApplier) {
        this.motionApplier = motionApplier;
    }
}
