module dk.sdu.cbse.player {
    requires dk.sdu.cbse.api;
    provides dk.sdu.cbse.api.IGamePluginService with dk.sdu.cbse.player.PlayerPlugin;
    provides dk.sdu.cbse.api.IEntityProcessingService with dk.sdu.cbse.player.PlayerPlugin;
}
