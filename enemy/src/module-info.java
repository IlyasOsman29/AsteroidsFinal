module dk.sdu.cbse.enemy {
    requires dk.sdu.cbse.api;
    provides dk.sdu.cbse.api.IGamePluginService with dk.sdu.cbse.enemy.EnemyPlugin;
    provides dk.sdu.cbse.api.IEntityProcessingService with dk.sdu.cbse.enemy.EnemyPlugin;
}
