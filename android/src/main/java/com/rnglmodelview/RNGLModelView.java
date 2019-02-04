package com.rnglmodelview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.graphics.PixelFormat;
import android.util.Log;
import com.rnglmodelview.exceptions.ModelObjectNotSupportedException;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.annotation.Nullable;

public class RNGLModelView extends GLSurfaceView implements RendererDelegate {

  private static final String   HEADER_URI_BASE64_ENCODED = "data:application/octet-stream;base64,";
  private static final String   HEADER_URI_ASSETS         = "asset:/";
  private static final String[] SUPPORTED_GEOMETRIES      = new String[] {
    "obj", "3ds", "md2", "asc", "model",
  };

  private static final String getBase64EncodedGeometryHeader(final String pGeometryType) {
    return "data:geometry/" + pGeometryType + ";base64,";
  }

  private RNGLModelViewRenderer mRenderer;

  private Object3D mModel;

  private float mModelRotateX = 0;
  private float mModelRotateY = 0;
  private float mModelRotateZ = 0;
  private float mModelTranslateX = 0;
  private float mModelTranslateY = 0;
  private float mModelTranslateZ = 0;
  private float mModelScaleX = 1;
  private float mModelScaleY = 1;
  private float mModelScaleZ = 1;

  private SimpleVector mMeshTranslate = new SimpleVector();

  public RNGLModelView(Context context) {
    super(context);
    setEGLContextClientVersion(2);
    setZOrderOnTop(true);
    setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    getHolder().setFormat(PixelFormat.RGBA_8888);

    mRenderer = new RNGLModelViewRenderer(context);
    setRenderer(mRenderer);
    mRenderer.delegate = this;
  }

  public void setModelUri(@Nullable final String pUri) {
    Object3D model = null;
    
    try {
      for (final String lGeometryType: SUPPORTED_GEOMETRIES) {
        final String lGeometryHeader = RNGLModelView.getBase64EncodedGeometryHeader(lGeometryType);
        // XXX: Here, we've found a matching header.
        if (pUri.startsWith(lGeometryHeader)) {
          final String lBase64 = pUri.substring(lGeometryHeader.length());
          final InputStream lInputStream = getInputStreamFromBase64(lBase64);
          model = RNGLModelView.loadModelFromInputStream(lInputStream, lGeometryType);
        }
      }
      
      if (model == null) {
        if (pUri.startsWith(HEADER_URI_ASSETS)) {
          model = RNGLModelView.loadModel(getContext(), pUri.substring(HEADER_URI_ASSETS.length()));
        } else {
          // XXX: Fall back to the original scheme.
          model = RNGLModelView.loadModel(getContext(), pUri);
        }   
      }
    } catch(final IOException | ModelObjectNotSupportedException e) {
      e.printStackTrace();
    }

    mModel = model;
    // Invert the UVs in order to make it consistent with the iOS version
    if (model != null) {
      Matrix uvMatrix = new Matrix();
      uvMatrix.rotateX((float)Math.PI);
      mModel.setTextureMatrix(uvMatrix);
    }
    mRenderer.setModel(mModel);
  }

  public void onAddedToWorld(Object3D model) {
    if (model == null) return;

    // We cache the origin before setting it to (0, 0, 0) since we will create the matrix manually when
    // updating the transform
    mMeshTranslate = model.getCenter();

    Matrix meshTranslationMatrix = new Matrix();
    meshTranslationMatrix.translate(-mMeshTranslate.x, -mMeshTranslate.y, -mMeshTranslate.z);
    model.setTranslationMatrix(meshTranslationMatrix);
    model.translateMesh();
    model.clearTranslation();

    model.setSpecularLighting(true);
    model.build();
    model.strip();

    updateModelTransform();
  }

  private static final InputStream getInputStreamFromBase64(final String pBase64) {
    return new ByteArrayInputStream(Base64.getDecoder().decode(pBase64));
  }
  
  public void setModelTextureUri(@Nullable String pUri) {
    Texture texture = null;
    if (pUri != null) {
      if (pUri.startsWith(HEADER_URI_BASE64_ENCODED)) {
        texture = new Texture(getInputStreamFromBase64(pUri.substring(HEADER_URI_BASE64_ENCODED.length())));
      } else if (pUri.startsWith(HEADER_URI_ASSETS)) {
        texture = loadTexture(pUri.substring(HEADER_URI_ASSETS.length()));
      } else {
        // XXX: Fall back to the original scheme.
        texture = loadTexture(pUri);
      }
    }
    mRenderer.setTexture(texture);
  }

  public void setModelTint(int red, int green, int blue, int alpha) {
    mRenderer.setModelTint(new RGBColor(red, green, blue, alpha));
  }

  public void setAnimate(@Nullable boolean animate) {
    mRenderer.setAnimate(animate);
  }

  public void flipTexture(@Nullable boolean flipped) {
    mRenderer.flipTexture(flipped);
  }

  public void setModelRotateX(@Nullable float rotateX) {
    mModelRotateX = rotateX;
    updateModelTransform();
  }

  public void setModelRotateY(@Nullable float rotateY) {
    mModelRotateY = rotateY;
    updateModelTransform();
  }

  public void setModelRotateZ(@Nullable float rotateZ) {
    mModelRotateZ = rotateZ;
    updateModelTransform();
  }

  public void setModelScale(@Nullable float scale) {
    mModelScaleX = scale;
    mModelScaleY = scale;
    mModelScaleZ = scale;
    updateModelTransform();
  }

  public void setModelScaleX(@Nullable float scaleX) {
    mModelScaleX = scaleX;
    updateModelTransform();
  }

  public void setModelScaleY(@Nullable float scaleY) {
    mModelScaleY = scaleY;
    updateModelTransform();
  }

  public void setModelScaleZ(@Nullable float scaleZ) {
    mModelScaleZ = scaleZ;
    updateModelTransform();
  }

  public void setModelTranslateX(@Nullable float translateX) {
    mModelTranslateX = translateX;
    updateModelTransform();
  }

  public void setModelTranslateY(@Nullable float translateY) {
    mModelTranslateY = translateY;
    updateModelTransform();
  }

  public void setModelTranslateZ(@Nullable float translateZ) {
    mModelTranslateZ = translateZ;
    updateModelTransform();
  }

  private static final Object3D loadModelFromInputStream(final InputStream pInputStream, final String pExtension) throws IOException, ModelObjectNotSupportedException {
    switch (pExtension) {
      case "obj":
        return Object3D.mergeAll(Loader.loadOBJ(pInputStream, null, 1));
      case "3ds":
        return Object3D.mergeAll(Loader.load3DS(pInputStream, 1));
      case "md2":
        return Loader.loadMD2(pInputStream, 1);
      case "asc":
        return Loader.loadASC(pInputStream, 1, false);
      case "model":
        return RNGLModelViewModelLoader.loadMODEL(pInputStream);
    }
    return null;
  }

  private static Object3D loadModel(final Context pContext, @Nullable String modelFileName) throws IOException, ModelObjectNotSupportedException {

    if (modelFileName != null) {

      String modelFileNameArray[] = modelFileName.split("\\.");
      String extension = modelFileNameArray[modelFileNameArray.length - 1].toLowerCase();
      final InputStream modelStream = pContext.getAssets().open(modelFileName);
      return RNGLModelView.loadModelFromInputStream(modelStream, extension);
    }
  
    return null;
  }

  private Texture loadTexture(String textureFileName) {
    Texture texture = null;

    try {
      InputStream textureStream = getContext().getAssets().open(textureFileName);
      texture = new Texture(textureStream);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return texture;
  }
  
  private void updateModelTransform() {
    if (mModel == null) return;

    Matrix scaleMatrix = new Matrix();
    scaleMatrix.setRow(0, mModelScaleX, 0, 0, 0);
    scaleMatrix.setRow(1, 0, mModelScaleY, 0, 0);
    scaleMatrix.setRow(2, 0, 0, mModelScaleZ, 0);

    Matrix rotationMatrix = new Matrix();
    rotationMatrix.rotateZ((float)Math.toRadians(mModelRotateZ));
    rotationMatrix.rotateY((float)Math.toRadians(mModelRotateY));
    rotationMatrix.rotateX((float)Math.toRadians(mModelRotateX + 180)); // jPCT is upside down, so we add 180

    // Most 3D applications pre-multiply the matrices that way: Transform = T * R * S * Origin. But since
    // jPCT only scales in the local space by default, the origin of the mesh is never adjusted for the scale.
    // Therefore, we have to do it manually by creating our own origin translation matrix and scaling it
    // ourselves. This is why we cached and cleared the origin after loading the model. The result will
    // essentially be the following operation: Transform = T * R * S * CachedTranslation * IdentityOrigin.

    // First, we scale the cached mesh translation
    Matrix translationMatrix = new Matrix();
    translationMatrix.translate(mMeshTranslate.x, mMeshTranslate.y, mMeshTranslate.z);
    translationMatrix.matMul(scaleMatrix);

    // Then, we rotate the scaled model
    scaleMatrix.matMul(rotationMatrix);

    // Finally, we can translate the model to its final position regardless of the scale
    translationMatrix.translate(mModelTranslateX, mModelTranslateY, mModelTranslateZ);

    mModel.setRotationMatrix(scaleMatrix);
    mModel.setTranslationMatrix(translationMatrix);
  }
}
