module dk.sdu.cbse.collision {
    requires dk.sdu.cbse.api;
    provides dk.sdu.cbse.api.IGamePluginService with dk.sdu.cbse.collision.CollisionPlugin;
    provides dk.sdu.cbse.api.IPostEntityProcessingService with dk.sdu.cbse.collision.CollisionPlugin;
}
