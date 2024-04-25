package ce326.hw2;

// Representation of Board cells for Dijkstra
public class DijkstraCell {
    int row;
    int column;
    int distance;

    public DijkstraCell(int row, int column, int distance) {
        this.row = row;
        this.column = column;
        this.distance = distance;
    }
}
