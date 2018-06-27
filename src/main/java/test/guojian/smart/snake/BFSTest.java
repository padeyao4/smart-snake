package test.guojian.smart.snake;

import guojian.smart.snake.BFS;

import static guojian.smart.snake.Model.*;

import guojian.smart.snake.Model;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.List;

/**
 * BFS Tester.
 *
 * @author guojian
 * @version 1.0
 * @since <pre>Jun 25, 2018</pre>
 */
public class BFSTest {

    int[] src;
    int[] dst;
    int[][] world;
    Model m;

    @Before
    public void before() throws Exception {
        m = new Model();
        world = m.world.clone();
        src = new int[]{ 1,1};
        dst = new int[]{ROWS / 2, COLS / 2};
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: search()
     */
    @Test
    public void testSearch() throws Exception {
    }

    /**
     * Method: findFartherPath(int[] src, int[] dst, int[][] tmpWorld)
     */
    @Test
    public void testFindFartherPath() throws Exception {
        System.out.println("test find farther paths");
        debug_log(world);
        List<int[]> farther = BFS.findFartherPath(src, dst, world);
        int[][] tmpWorld = debug_path(world, farther);
        debug_log(tmpWorld);
    }

    public void debug_log(int[][] world) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (world[row][col] < 0) {
                    System.out.print(world[row][col]);
                } else {
                    System.out.print(" " + world[row][col]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[][] debug_path(int[][] world, List<int[]> paths) {
        int[][] tmpWorld = world.clone();
        for (int[] point : paths) {
            tmpWorld[point[0]][point[1]] = 8;
        }
        return tmpWorld;
    }

    /**
     * Method: searchShortestPath(int[] src, int[] dst, int[][] tmpWorld)
     */
    @Test
    public void testSearchShortestPath() throws Exception {
        System.out.println("test search shortest path");
        debug_log(world);
        List<int[]> shortList = BFS.searchShortestPath(src, dst, world);
        int[][] tmpWorld = debug_path(world, shortList);
        debug_log(tmpWorld);
    }


} 
