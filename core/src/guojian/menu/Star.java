package guojian.menu;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class Star {
    static float range = (Gdx.graphics.getWidth() + Gdx.graphics.getHeight()) / 2f;
    static Vector2 center = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

    Vector2 position;
    Vector2 speed;
    Vector2 acc;// 加速度
    float m; // 质量
    Color color;
    PointLight pl;


    public Star(RayHandler rayHandler) {
        this.position = new Vector2(RandomUtils.nextFloat(0, range), RandomUtils.nextFloat(0, range));
        this.speed = new Vector2(RandomUtils.nextFloat(0, 400f) - 200f, RandomUtils.nextFloat(0, 400f) - 200f);
        this.acc = new Vector2();
        this.color = new Color(RandomUtils.nextFloat(0, 1f), RandomUtils.nextFloat(0, 1f), RandomUtils.nextFloat(0, 1f), 1f);
        m = RandomUtils.nextFloat(1f, 100f);

        pl = new PointLight(rayHandler, 12, color, RandomUtils.nextFloat(100f, 200f), position.x, position.y);
        pl.setSoft(true);
        pl.setStaticLight(false);
    }

    public void acc(List<Star> stars) {
        acc.set(0, 0);
        for (var s : stars) {
            if (s.equals(this)) continue;
            var d = s.position.cpy().sub(position);
            acc.add(d.scl(1f / d.cpy().dst(Vector2.Zero)).scl(m * s.m));
        }
        var cet = center.cpy().sub(position);
        acc.add(cet.scl(1f / cet.cpy().dst(Vector2.Zero)).scl(m * 100f));
        acc.scl(1f / m);
    }

    public void update(float dt) {
        var ds = speed.add(acc.cpy().scl(dt)).cpy().scl(dt);
        position.add(ds);
        pl.setPosition(position);
    }

    /**
     * 由ray handler 统一释放
     */
    public void dispose() {
        pl.dispose();
    }
}
