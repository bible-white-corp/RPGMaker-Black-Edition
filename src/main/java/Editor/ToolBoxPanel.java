package Editor;

import javax.swing.*;
import java.awt.*;

public class ToolBoxPanel extends JPanel {
    public JButton setSpawnButton;
    public JButton addTeleporterButton;

    public JCheckBox showWalkable;
    public JButton forceWalkable;
    public JButton forceUnwalkable;

    public ToolBoxPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.YELLOW);
        setSpawnButton = new JButton("Set Player Spawn");
        addTeleporterButton = new JButton("Add Teleporter");
        showWalkable = new JCheckBox("Show walkable on map");
        forceWalkable = new JButton("Set selection walkable");
        forceUnwalkable = new JButton("Set selection not walkable");

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(setSpawnButton, c);
        c.gridy += 1;
        this.add(addTeleporterButton, c);
        c.gridy += 1;
        this.add(showWalkable, c);
        c.gridy += 1;
        this.add(forceWalkable, c);
        c.gridy += 1;
        this.add(forceUnwalkable, c);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        setSpawnButton.setVisible(aFlag);
        addTeleporterButton.setVisible(aFlag);
        showWalkable.setVisible(aFlag);
    }
}
