module dk.sdu.cbse.core {
    requires java.desktop;
    requires dk.sdu.cbse.api;
    uses dk.sdu.cbse.api.IGamePluginService;
    uses dk.sdu.cbse.api.IEntityProcessingService;
    uses dk.sdu.cbse.api.IPostEntityProcessingService;
}
