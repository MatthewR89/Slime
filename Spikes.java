package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import Screens.PlayScreen;

public class Spikes extends InteractiveTileObject {
    public Spikes(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onTouch() {

        switch (Slime.playerDirection) {

            case 0:

                if((Math.floor(body.getPosition().y) > (Math.floor(PlayScreen.playerPosition.y))) &&
                        (Math.floor(body.getPosition().x) == (Math.floor(PlayScreen.playerPosition.x)))) {
                    Gdx.app.log("Spikes", "Collision");
                    Slime.isDead = true;

                }
                break;

            case 1:

                if((Math.floor(body.getPosition().y) == (Math.floor(PlayScreen.playerPosition.y))) &&
                        (Math.floor(body.getPosition().x) > (Math.floor(PlayScreen.playerPosition.x)))) {
                    Gdx.app.log("Spikes", "Collision");
                    Slime.isDead = true;
                }
                break;

            case 2:

                if((Math.floor(body.getPosition().y) < (Math.floor(PlayScreen.playerPosition.y))) &&
                        (Math.floor(body.getPosition().x) == (Math.floor(PlayScreen.playerPosition.x)))) {
                    Gdx.app.log("Spikes", "Collision");
                    Slime.isDead = true;
                }
                break;

            case 3:

                if((Math.floor(body.getPosition().y) == (Math.floor(PlayScreen.playerPosition.y))) &&
                        (Math.floor(body.getPosition().x) < (Math.floor(PlayScreen.playerPosition.x)))) {
                    Gdx.app.log("Spikes", "Collision");
                    Slime.isDead = true;
                }
                break;

        }
    }


}
