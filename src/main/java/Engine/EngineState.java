package Engine;

import Model.World.Map;
import Model.World.Player;
import Model.World.World;

import java.util.Observable;

public class EngineState extends Observable {
    public World world;
    public Map currentMap;
    public Player player;

    static EngineState engineState;

    public EngineState(World world) {
        engineState = this;
        this.world = world;
        this.player = world.getPlayer();
    }

    public static EngineState getInstance() {
        return engineState;
    }

    public void init() {
        Map newMap = world.getMapById(world.getPlayer().getMapId());
        changeMap(newMap);
    }

    public void changeMap(Map map) {
        currentMap = map;
        setChanged();
        notifyObservers("Change Map");
    }

    public void redrawPerso() {
        setChanged();
        notifyObservers("Update Perso");
    }
}
