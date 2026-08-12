module dk.sdu.cbse.asteroids {
    requires dk.sdu.cbse.api;
    provides dk.sdu.cbse.api.IGamePluginService with dk.sdu.cbse.asteroids.AsteroidPlugin;
    provides dk.sdu.cbse.api.IEntityProcessingService with dk.sdu.cbse.asteroids.AsteroidPlugin;
}
