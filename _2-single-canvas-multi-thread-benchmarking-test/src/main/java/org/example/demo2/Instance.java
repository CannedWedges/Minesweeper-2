package org.example.demo2;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Instance {

    private String className = "";

    private List<Instance> children = new ArrayList<Instance>();
    private Instance parent;

    public List<Instance> getChildren() {
        return children;
    }

    public void setParent(Instance parent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }
        if (parent != null) {
            parent.getChildren().add(this);
        }
        this.parent = parent;
    }



}

