package models;

import javafx.scene.Node;

public class GridPaneContent {
    private Node node;
    private int row;
    private int col;

    public GridPaneContent(Node node, int row, int col) {
        this.node = node;
        this.row = row;
        this.col = col;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
