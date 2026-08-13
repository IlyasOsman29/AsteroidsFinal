package dk.sdu.cbse.api;

/** Per-frame behaviour supplied by a gameplay module. */
public interface IEntityProcessingService {
    /**
     * Pre: {@code gameData != null} and {@code deltaSeconds >= 0}.
     * Post: owned entity state reflects one frame; optional missing entity types are tolerated.
     */
    void process(GameData gameData, double deltaSeconds);
}
