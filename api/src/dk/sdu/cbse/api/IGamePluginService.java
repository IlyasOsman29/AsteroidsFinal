package dk.sdu.cbse.api;
/** Lifecycle contract for a game component. */
public interface IGamePluginService {
    /** Pre: gameData != null. Post: component entities/resources may be added. */
    void start(GameData gameData);
    /** Pre: gameData != null. Post: component-owned entities/resources are removed. */
    void stop(GameData gameData);
    String name();
}
