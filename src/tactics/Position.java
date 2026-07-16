package tactics;

import java.util.Objects;

public class Position {

    int col;
    int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position other)) return false;
        return this.col == other.col && this.row == other.row;
    }
}
