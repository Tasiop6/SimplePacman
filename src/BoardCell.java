package ce326.hw2;

public class BoardCell {
    // Type of element in BoardCell
    public BoardElement[] elements;

    // Control for '-', so we don't lose them on overlap
    public boolean hasVisited;

    public BoardCell() {
    }


    public void setElements(BoardElement[] elements) {
        this.elements = elements;
    }

    // Get Element
    public BoardElement getElement(int pos) {
        if (pos < 1) {
            return elements[pos];
        }
        return elements[0];
    }


    // Check if X has visited
    public boolean getHasVisited() {
        return hasVisited;
    }

    // Change hasVisited
    public void setHasVisited(boolean hasVisited) {
        this.hasVisited = hasVisited;
    }

}
