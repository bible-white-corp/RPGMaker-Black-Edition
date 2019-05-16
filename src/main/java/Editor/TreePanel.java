package Editor;

import Model.World.Map;
import Model.World.World;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreePanel extends JTree {
    World world = null;

    public TreePanel() {
    }

    public void setWorld(World world) {
        this.world  = world;
        show_world();
    }

    public void show_world() {
        if (world == null)
            return;

        DefaultMutableTreeNode Tworld = new DefaultMutableTreeNode(world);
        DefaultTreeModel model = new DefaultTreeModel(Tworld);

        DefaultMutableTreeNode Tmaps = new DefaultMutableTreeNode("Maps");
        DefaultMutableTreeNode Tplayer = new DefaultMutableTreeNode("Player");

        Tworld.add(Tmaps);
        Tworld.add(Tplayer);

        for (Map map : world.getMaps()) {
            DefaultMutableTreeNode Tmap = new DefaultMutableTreeNode(map);
            Tmaps.add(Tmap);
        }

        this.setModel(model);
        this.setShowsRootHandles(true);

        //CreateChildNodes ccn = new CreateChildNodes(dir, root);
        //new Thread(ccn).start();
    }
}