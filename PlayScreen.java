package Screens;

import static Sprites.Slime.playerDirection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jnm.slime.Constants;
import com.jnm.slime.SlimeGame;

import org.w3c.dom.Text;

import Scenes.HUD;
import Sprites.Slime;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;

public class  PlayScreen implements Screen{

    private SlimeGame game;
    private TextureAtlas atlas;
    private Slime player;

    //camera variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private HUD hud;

    //gesture detector
//    GestureDetector gestureDetector;

    //tiledmap variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //player movement
    public static Vector2 playerPosition;


    public PlayScreen(SlimeGame game){
        atlas = new TextureAtlas("SlimeSprite.pack");

        this.game = game;

        //create cam used to follow slime through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen
        gamePort = new FitViewport(Constants.WORLD_WIDTH/Constants.PPM,Constants.WORLD_HEIGHT/Constants.PPM,gamecam);
        //create the game HUD for scores/timers/level info
        hud = new HUD(game.batch);

        //gesture detector initialization
//        Gdx.input.setInputProcessor(gestureDetector);

        //load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Stage1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Constants.PPM);

        //initially set up our gamecam to be centered correctly
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        //create Slime in game world
        player = new Slime(world, this);

        world.setContactListener(new WorldContactListener());

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

        InputMultiplexer multiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(multiplexer);

        multiplexer.addProcessor(new GestureDetector(player));

        multiplexer.addProcessor(hud.stage);
    }

    public void handleInput(float dt){



        //if Player's velocity is zero (not moving) than they are able to move
        if(player.b2body.getLinearVelocity().epsilonEquals(0,0)){

            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                playerDirection = 0;
                player.b2body.applyLinearImpulse(new Vector2(0, 16f), player.b2body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                playerDirection = 1;
                player.b2body.applyLinearImpulse(new Vector2(16f, 0), player.b2body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                playerDirection = 2;
                player.b2body.applyLinearImpulse(new Vector2(0, -16f), player.b2body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                playerDirection = 3;
                player.b2body.applyLinearImpulse(new Vector2(-16f, 0), player.b2body.getWorldCenter(), true);
            }

        }

    }


    public void update(float dt){

        handleInput(dt);

        playerPosition = player.b2body.getPosition();
        world.step(1/60f,6,2);

        player.update(dt);

        gamecam.position.set(player.b2body.getPosition(),0);

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //separate update logic from render
        update(delta);

        //clear the game screen with black
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the game map
        renderer.render();

        //render Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
