module dk.sdu.cbse.weapon {
    requires dk.sdu.cbse.api;
    provides dk.sdu.cbse.api.IGamePluginService with dk.sdu.cbse.weapon.WeaponPlugin;
    provides dk.sdu.cbse.api.IEntityProcessingService with dk.sdu.cbse.weapon.WeaponPlugin;
}
