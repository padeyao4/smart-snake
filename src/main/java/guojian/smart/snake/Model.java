package guojian.smart.snake;

import java.io.IOException;

public class Model {

    Cell[][] world;
    int cols ;
    int rows ;

    enum Cell{
        header,tail,apple,body,blank,wall,
    }


    public Model() throws IOException {
        Util u = Util.getInstance();
        cols = Integer.parseInt(u.get("world.width"));
        rows = Integer.parseInt(u.get("world.height"));
        world = new Cell[rows][cols];

        for(int row=0;row<rows;row++){
            for(int col=0;col<cols;col++){
                world[row][col]=Cell.blank;
            }
        }
    }

    public void update(){

    }
}
