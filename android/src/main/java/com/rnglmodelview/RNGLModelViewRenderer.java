package com.rnglmodelview;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import com.rnglmodelview.TintEffect;
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
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.World;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

interface RendererDelegate {
  void onAddedToWorld(Object3D model);
}

public class RNGLModelViewRenderer implements GLSurfaceView.Renderer {
  public RendererDelegate delegate;

  private boolean hasToCreateBuffer = false;
  private int w = 0;
  private int h = 0;

  private long time = System.currentTimeMillis();
  private FrameBuffer fb;
  private World world;
  private Object3D mModel;
  private Texture mTexture;
  private RGBColor mModelTint;
  private GLSLShader shader;
  private Light light;
  private RGBColor clearColor = new RGBColor(0, 0, 0, 0);
  private Context mContext;
  private static RNGLModelViewRenderer master = null;
  private GL10 previousGL = null;
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
      light.setPosition(SimpleVector.create(0, 0, 1));

      // In jpct, the coordinate system is rotated 180 degrees around x compared to the OpenGL
      // coordinate system, which means that y faces towards the bottom and z faces away from the
      // screen. Since most engines and models use the OpenGL coordinate system, we fix the
      // rotation on the camera.
      Camera cam = world.getCamera();
      cam.rotateX((float)Math.PI);
      cam.setPosition(0, 0, 1);

      if (mModel != null) {
        updateTexture();
        mModel.build();
        world.addObject(mModel);

        if (delegate != null) {
          delegate.onAddedToWorld(mModel);
        }
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

  private void updateTexture() {
    TextureManager tm = TextureManager.getInstance();

    int textureWidth = mTexture == null ? 2 : mTexture.getWidth();
    int textureHeight = mTexture == null ? 2 : mTexture.getHeight();
    Texture currentTexture = null;

    if (mTexture != null) {
      currentTexture = mTexture;

      if (mModelTint != null) {
        currentTexture.setEffect(new TintEffect(mModelTint));
        currentTexture.applyEffect();
      }
    } else if (mModelTint != null) {
      currentTexture = new Texture(1, 1, mModelTint);
    }

    if (currentTexture != null) {
      if (tm.containsTexture("texture")) {
        tm.replaceTexture("texture", currentTexture);
      } else {
        tm.addTexture("texture", currentTexture);
      }
    }

    // setTransparency() acts as an upper bound to limit the alpha, so we set it to a very high value
    // Since the transparency value never makes the model 100% opaque, we turn it of if the alpha is 255
    mModel.setTransparency(mModelTint == null || mModelTint.getAlpha() >= 255 ? -1 : Integer.MAX_VALUE);

    TextureInfo ti = new TextureInfo(TextureManager.getInstance().getTextureID("texture"));
    mModel.setTexture(ti);
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

  public void setModelTint(RGBColor tint) {
    mModelTint = tint;
  }

  public void setAnimate(boolean animate) { mAnimate = animate; }
}