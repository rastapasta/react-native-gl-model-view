package com.rnglmodelview;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RNGLModelViewRenderer implements GLSurfaceView.Renderer {

  private boolean hasToCreateBuffer = false;
  private int w = 0;
  private int h = 0;

  private long time = System.currentTimeMillis();
  private FrameBuffer fb;
  private World world;
  private Object3D mModel;
  private Texture mTexture;
  private GLSLShader shader;
  private Light light;
  private RGBColor clearColor = new RGBColor(255, 255, 255);
  private Context mContext;
  private static RNGLModelViewRenderer master = null;
  private GL10 previousGL = null;
  private float scale = 0.05f;
  private boolean mAnimate = false;

  public RNGLModelViewRenderer(Context context) {
    Texture.defaultToMipmapping(true);
    Texture.defaultTo4bpp(true);
    mContext = context;
  }

  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    Logger.log("onSurfaceCreated");
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    Resources res = mContext.getResources();

    if (world == null) {
      fb = new FrameBuffer(width, height);
      world = new World();

      light = new Light(world);
      light.enable();
      light.setIntensity(51, 51, 51);
      light.setPosition(SimpleVector.create(0, 16, 6));

      // In jpct, the coordinate system is rotated 180 degrees around x compared to the OpenGL
      // coordinate system, which means that y faces towards the bottom and z faces away from the
      // screen. Since most engines and models use the OpenGL coordinate system, we fix the
      // rotation on the camera.
      Camera cam = world.getCamera();
      SimpleVector pos = cam.getPosition();
      cam.rotateX((float)Math.PI);

      // We move the camera to make the iOS and Android views look alike as much as possible
      cam.setPosition(0, 16, 5);

      if (mModel != null) {
        TextureManager tm = TextureManager.getInstance();

        if (mTexture == null) {
          if (tm.containsTexture("texture")) {
            tm.removeTexture("texture");
          }
        } else {
          if (tm.containsTexture("texture")) {
            tm.replaceTexture("texture", mTexture);
          } else {
            tm.addTexture("texture", mTexture);
          }

          TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID("texture"));
          mModel.setTexture(ti);
        }

        mModel.setSpecularLighting(true);
        mModel.build();
        mModel.strip();
        world.addObject(mModel);
      }

      MemoryHelper.compact();

      world.compileAllObjects();
    } else if (previousGL != gl) {
      fb = new FrameBuffer(width, height);
    }

    previousGL = gl;

    renderFrame();
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    if (mAnimate) {
      renderFrame();
    }
  }

  private void renderFrame() {
    fb.clear(clearColor);
    world.renderScene(fb);
    world.draw(fb);
    fb.display();
  }

  public void setModel(Object3D model) {
    mModel = model;
  }

  public void setTexture(Texture texture) {
    mTexture = texture;
  }

  public void setAnimate(boolean animate) { mAnimate = animate; }
}