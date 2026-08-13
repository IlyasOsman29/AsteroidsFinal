package dk.sdu.cbse.api;

/** Lifecycle contract implemented by an installable gameplay module. */
public interface IGamePluginService {
    /**
     * Starts the component.
     * Pre: {@code gameData != null}; the component is not already active in this layer.
     * Post: initial component-owned entities/resources exist and unrelated entities are unchanged.
     */
    void start(GameData gameData);

    /**
     * Stops the component.
     * Pre: {@code gameData != null}.
     * Post: component-owned entities/resources have been removed; repeated cleanup is harmless.
     */
    void stop(GameData gameData);

    /** @return a short human-readable component name */
    String name();
}
