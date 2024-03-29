package Model.Editor;

import java.util.Observable;

import static Model.Editor.ToolsEnum.NONE;

public class ToolsState extends Observable {
    public ToolsEnum currentTools;

    public ToolsState() {
        currentTools = NONE;
    }

    public void setCurrentTools(ToolsEnum currentTools) {
        this.currentTools = currentTools;
        setChanged();
        notifyObservers(this.currentTools);
    }

}

