package Model.World;

import Tools.ActionManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

public class Map {
    String name;
    Dimension dimension;
    Vector<Tile> background;
    Vector<Boolean> walkable;
    HashMap<Point, Tile> foreground; // Tile but it's a ImportedTile. Point is top left corner.
    Vector<Teleporter> teleporters;
    Tile backgroundTile;

    public Map(Dimension dimension, String name) {
        this.name = name;
        this.dimension = dimension;
        this.background = new Vector<>(dimension.width * dimension.height);
        this.walkable = new Vector<>(dimension.width * dimension.height);
        this.foreground = new HashMap<>();
        this.teleporters = new Vector<>();
        for (int i = 0; i < dimension.width * dimension.height; i++) {
            background.add(i, Tile.getTransPlaceholder());
            walkable.add(true);
        }
        this.backgroundTile = Tile.getPlaceholder();
    }

    public void setBackgroundTile(Tile backgroundTile) {
        this.backgroundTile = backgroundTile;
    }

    public Map(Map map) {
        this.name = map.name;
        this.dimension = new Dimension(map.dimension);
        this.background = new Vector<>(map.background);
        this.foreground = new HashMap<>(map.foreground);
        this.teleporters = new Vector<>(map.teleporters);
        this.walkable = new Vector<>(map.walkable);
        this.backgroundTile = map.backgroundTile;
    }

    @Override
    public String toString() {
        return name;
    }

    public void draw(Tile currentTile, Point in, Point out) {
        ActionManager.saveAction(new Map(this));
        if (out == null)
            out = in;
        if (in != null) {
            if (currentTile instanceof BigTile) {
                drawBigTile((BigTile)currentTile, in, out);
                return;
            }
            for (int x = in.x; x <= out.x; x++) {
                for (int y = in.y; y <= out.y; y++) {
                    if (checkBounds(x, y))
                        continue;
                    if (currentTile instanceof ImportedTile) {
                        Point isOccupied = isOccupied(x, y, ((ImportedTile) currentTile).getDimention());
                        if (currentTile.getName().equals("eraser.png")) {
                            if (isOccupied != null)
                                removeFore(isOccupied);
                            continue;
                        }
                        if (isOccupied != null) {
                            continue;
                        }
                        setFore(x, y, (ImportedTile) currentTile);
                    } else {
                        if (currentTile.getName().equals("eraser.png"))
                            setTile(x, y, Tile.getTransPlaceholder());
                        else
                            setTile(x, y, currentTile);
                    }
                }
            }
        }
    }

    private void drawBigTile(BigTile bt, Point in, Point out) {
        if (in.equals(out))
            out = new Point(out.x + bt.getWidth() - 1, out.y + bt.getHeight() - 1);
        for (int x = in.x; x <= out.x; x++) {
            for (int y = in.y; y <= out.y; y++) {
                if (bt.cur == -1) {
                    if (bt.getWidth() == 3 && bt.getHeight() == 3) {
                        setTile(x, y, bt.getTile(1, 1));
                    } else {
                        setTile(x, y, bt.getTile(x % bt.getWidth(), y % bt.getHeight()));
                    }
                } else {
                    setTile(x, y, bt.getTile(bt.cur));
                }
            }
        }
        if (bt.cur == -1 && bt.getWidth() == 3 && bt.getHeight() == 3) {
            for (int x = in.x + 1; x < out.x; x++) {
                setTile(x, in.y, bt.getTile(1,0));
                setTile(x, out.y, bt.getTile(1,2));
            }
            for (int y = in.y + 1; y < out.y; y++) {
                setTile(in.x, y, bt.getTile(0,1));
                setTile(out.x, y, bt.getTile(2,1));
            }
            setTile(in.x, in.y, bt.getTile(0, 0));
            setTile(in.x, out.y, bt.getTile(0, 2));
            setTile(out.x, in.y, bt.getTile(2, 0));
            setTile(out.x, out.y, bt.getTile(2, 2));
        }
    }

    public Point isOccupied(int x, int y, Dimension dimension) {
        for (int i = x; i < x + dimension.width; i++) {
            for (int j = y; j < y + dimension.height; j++) {
                for (var item : foreground.entrySet()) {
                    ImportedTile tile = (ImportedTile)item.getValue();
                    Point pt = item.getKey();
                    if (i >= pt.x && i < pt.x + tile.getWidth() && j >= pt.y && j < pt.y + tile.getHeight()) {
                        return pt;
                    }
                }
            }
        }
        return null;
    }

    public boolean checkBounds(int x, int y) {
        if (x < 0 || y < 0)
            return true;
        if (x >= dimension.width || y >= dimension.height)
            return true;
        return false;
    }

    public Tile getTile(int x, int y) {
        if (checkBounds(x, y))
            return Tile.getTransPlaceholder();
        return background.get(x + dimension.width * y);
    }
    public void setTile(int x, int y, Tile tile) {
        if (checkBounds(x, y))
            return;
        background.set(x + dimension.width * y, tile);
        if (isOccupied(x, y, new Dimension(1, 1)) == null)
            setWalkable(x, y, tile.defaultWalkable);
    }

    public void setFore(int x, int y, ImportedTile tile) {
        if (checkBounds(x, y))
            return;

        foreground.put(new Point(x, y), tile);

        for (int i = x; i < x + tile.getWidth(); i++) {
            for (int j = y; j < y + tile.getHeight(); j++) {
                setWalkable(i, j, tile.defaultWalkable);
            }
        }
    }
    private void removeFore(Point pt) {
        if (checkBounds(pt.x, pt.y))
            return;
        ImportedTile imp = (ImportedTile)foreground.get(pt);
        Dimension dim = new Dimension(imp.getWidth(), imp.getHeight());
        foreground.remove(pt);

        for (int i = pt.x; i < pt.x + dim.getWidth(); i++) {
            for (int j = pt.y; j < pt.y + dim.getHeight(); j++) {
                setWalkable(i, j, getTile(i, j).defaultWalkable);
            }
        }
    }


    public boolean getWalkable(int x, int y) {
        if (checkBounds(x, y))
            return false;
        return walkable.get(x + dimension.width * y);
    }

    public void setWalkable(int x, int y, boolean bool) {
        if (checkBounds(x, y))
            return;
        walkable.set(x + dimension.width * y, bool);
    }

    public HashMap<Point, Tile> getForegroundSet() {
        return foreground;
    }
    public Vector<Tile> getBackgroundSet() {
        return background;
    }

    public Dimension getDim() {
        return dimension;
    }

    public Teleporter addTeleporter(Point point) {
        if (checkBounds(point.x, point.y))
            return null;
        Teleporter res = new Teleporter(this, String.valueOf(teleporters.size()), point);
        teleporters.add(res);
        return res;
    }

    public Vector<Teleporter> getTeleporters() {
        return this.teleporters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tile getBackgroundTile() {
        return backgroundTile;
    }
}
