package dk.sdu.cbse.api;

/** Cross-component rules that run after normal per-frame processors. */
public interface IPostEntityProcessingService {
    /**
     * Pre: normal processors completed, {@code gameData != null}, {@code deltaSeconds >= 0}.
     * Post: detected interactions are applied without mutating the collection during iteration.
     */
    void process(GameData gameData, double deltaSeconds);
}
