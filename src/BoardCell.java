public class BoardCell {
    // Type of element in BoardCell
    public BoardElement[] elements;

    // Control for '-', so we don't lose them on overlap
    public boolean hasVisited;

    public BoardCell(BoardElement elements) {
        this.elements = new BoardElement[1];
        this.elements[0] = elements;

    }

    public BoardCell() {
    }

    // Get Element
    public BoardElement getElement(int pos) {
        if (pos < 1) {
            return elements[pos];
        }
        return elements[0];
    }

    // Add element in empty space of BoardCell.
    public void setElement (BoardElement elements) {
        if (this.elements[0] == null) {
            this.elements[0] = elements;
            return;
        }
        this.elements[1] = elements;
    }
    // Check if X has visited
    public boolean getHasVisited() {
        return hasVisited;
    }

    public void setHasVisited(boolean hasVisited) {
        this.hasVisited = hasVisited;
    }

}
