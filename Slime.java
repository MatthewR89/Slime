package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jnm.slime.Constants;
import com.jnm.slime.SlimeGame;

import java.awt.print.PrinterIOException;

import Screens.PlayScreen;

public class Slime extends Sprite implements GestureDetector.GestureListener {
    public enum State {STANDING, JUMPING, JUMPINGDOWN, JUMPINGLEFT, JUMPINGRIGHT, DYING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion slimestand;
    private Animation<TextureRegion> slimeJump;
    private Animation<TextureRegion> slimeJumpDown;
    private Animation<TextureRegion> slimeJumpLeft;
    private Animation<TextureRegion> slimeJumpRight;
    private Animation<TextureRegion> slimeDie;
    private float stateTimer;
    public static boolean isDead;


    public static int playerDirection = 0;

    float xStart;
    float yStart;

    float xDrag;
    float yDrag;

    boolean gestureStarted = false;

    public Slime(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("Slime"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 5; i++)
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        slimeJumpDown = new Animation<TextureRegion>(0.05f, frames);
        frames.clear();

        for(int i = 6; i < 10; i++)
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        slimeJump = new Animation<TextureRegion>(0.05f, frames);
        frames.clear();

        for(int i = 11; i < 15; i++)
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        slimeJumpRight = new Animation<TextureRegion>(0.05f, frames);
        frames.clear();

        for(int i = 16; i < 20; i++)
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        slimeJumpLeft = new Animation<TextureRegion>(0.05f, frames);
        frames.clear();

        for(int i = 21; i < 28; i++)
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        slimeDie = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        slimestand = new TextureRegion(getTexture(), 0, 0, 16, 16);

        defineSlime();
        setBounds(0, 0, 16/Constants.PPM, 16/Constants.PPM);
        setRegion(slimestand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = slimeJump.getKeyFrame(stateTimer);
                break;
            case JUMPINGDOWN:
                region = slimeJumpDown.getKeyFrame(stateTimer);
                break;
            case JUMPINGLEFT:
                region = slimeJumpLeft.getKeyFrame(stateTimer);
                break;
            case JUMPINGRIGHT:
                region = slimeJumpRight.getKeyFrame(stateTimer);
                break;
            case DYING:
                region = slimeDie.getKeyFrame(stateTimer);
                break;
            case STANDING:
            default:
                region = slimestand;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;


    }



    public State getState(){
        if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.JUMPINGDOWN;
        else if(b2body.getLinearVelocity().x > 0)
            return State.JUMPINGRIGHT;
        else if(b2body.getLinearVelocity().x < 0)
            return State.JUMPINGLEFT;
        else if(Slime.isDead)
            return State.DYING;
        else
            return State.STANDING;
    }

    public void defineSlime() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(Constants.stage1Originx / Constants.PPM, Constants.stage1Originy / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(7.4f/Constants.PPM, 7.4f/Constants.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);



        EdgeShape touch = new EdgeShape();

        touch.set(new Vector2(7.4f / Constants.PPM, 7.4f / Constants.PPM), new Vector2(-7.4f / Constants.PPM, -7.4f / Constants.PPM));
        fdef.shape = touch;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("touch");


    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        if(b2body.getLinearVelocity().epsilonEquals(0,0)){


            if (Math.abs(velocityY) > Math.abs(velocityX)) {

                if (velocityY < 0) {

                    playerDirection = 0;
                    b2body.applyLinearImpulse(new Vector2(0, 16f), b2body.getWorldCenter(), true);



                } else {

                    playerDirection = 2;
                    b2body.applyLinearImpulse(new Vector2(0, -16f), b2body.getWorldCenter(), true);
                }

            } else {

                if (velocityX > 0) {

                    playerDirection = 1;
                    b2body.applyLinearImpulse(new Vector2(16f, 0), b2body.getWorldCenter(), true);

                } else {

                    playerDirection = 3;
                    b2body.applyLinearImpulse(new Vector2(-16f, 0), b2body.getWorldCenter(), true);

                }
            }
        }
        return false;
    }


            @Override
            public boolean touchDown ( float x, float y, int pointer, int button){

                xStart = x;
                yStart = y;

                return true;

            }

            @Override
            public boolean tap ( float x, float y, int count, int button){
                return false;
            }

            @Override
            public boolean longPress ( float x, float y){
                return false;
            }

            @Override
            public boolean pan ( float x, float y, float deltaX, float deltaY){
                return false;
            }

            @Override
            public boolean panStop ( float x, float y, int pointer, int button){
                return false;
            }

            @Override
            public boolean zoom ( float initialDistance, float distance){
                return false;
            }

            @Override
            public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2
            pointer1, Vector2 pointer2){
                return false;
            }

            @Override
            public void pinchStop () {

            }

}
