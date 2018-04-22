package guojian.smart.snake;

import java.io.IOException;

public class Model {

    byte[][] world;
    int cols ;
    int rows ;

    public Model() throws IOException {
        Util u = Util.getInstance();
        cols = Integer.parseInt(u.get("world.width"));
        rows = Integer.parseInt(u.get("world.height"));
        world = new byte[rows][cols];
    }

    public void update(){

    }
}
