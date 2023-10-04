package main.java.com.shawarmaland.snakegame;

import java.util.Objects;

public class Wall {
    private int x;
    private int y;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Wall wall = (Wall) obj;
        return x == wall.x && y == wall.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
