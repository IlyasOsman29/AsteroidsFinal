package dk.sdu.cbse.api;
/** Runs after normal entity processing, e.g. collision detection. */
public interface IPostEntityProcessingService {
    void process(GameData gameData, double deltaSeconds);
}
