package dk.sdu.cbse.api;
/** Updates game state once per frame. */
public interface IEntityProcessingService {
    /** Pre: gameData != null, deltaSeconds >= 0. Post: owned game state may be updated. */
    void process(GameData gameData, double deltaSeconds);
}
