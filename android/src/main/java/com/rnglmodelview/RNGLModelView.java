package com.rnglmodelview;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.rnglmodelview.exceptions.ModelObjectNotSupportedException;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Texture;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

public class RNGLModelView extends GLSurfaceView {

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

  public RNGLModelView(Context context) {
    super(context);
    setEGLContextClientVersion(2);

    mRenderer = new RNGLModelViewRenderer(context);
    setRenderer(mRenderer);
  }

  public void setModel(String modelFileName) {
    mModel = loadModel(modelFileName);
    mRenderer.setModel(mModel);
    updateModelTransform();
  }

  public void setModelTexture(@Nullable String textureFileName) {
    mRenderer.setTexture(loadTexture(textureFileName));
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

  private Object3D loadModel(String modelFileName) {
    String modelFileNameArray[] = modelFileName.split("\\.");
    String extension = modelFileNameArray[modelFileNameArray.length - 1].toLowerCase();

    Object3D model = null;

    try {
      InputStream modelStream = getContext().getAssets().open(modelFileName);

      switch (extension) {
        case "obj":
          model = Object3D.mergeAll(Loader.loadOBJ(modelStream, null, 1));
          break;
        case "3ds":
          model = Object3D.mergeAll(Loader.load3DS(modelStream, 1));
          break;
        case "md2":
          model = Loader.loadMD2(modelStream, 1);
          break;
        case "asc":
          model = Loader.loadASC(modelStream, 1, false);
          break;
        case "model":
          model = RNGLModelViewModelLoader.loadMODEL(modelStream);
          break;
      }
    } catch (IOException | ModelObjectNotSupportedException e) {
      e.printStackTrace();
    }

    return model;
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
    if (mModel != null) {
      Matrix rotationMatrix = new Matrix();

      // First, we scale the identity matrix
      rotationMatrix.setRow(0, mModelScaleX, 0, 0, 0);
      rotationMatrix.setRow(1, 0, mModelScaleY, 0, 0);
      rotationMatrix.setRow(2, 0, 0, mModelScaleZ, 0);

      // Second, we rotate the scaled matrix
      rotationMatrix.rotateZ((float)Math.toRadians(mModelRotateZ));
      rotationMatrix.rotateY((float)Math.toRadians(mModelRotateY));
      rotationMatrix.rotateX((float)Math.toRadians(mModelRotateX));

      // Finally, we create the translation matrix
      Matrix translatonMatrix = new Matrix();
      translatonMatrix.translate(mModelTranslateX, mModelTranslateY, mModelTranslateZ);

      mModel.setTranslationMatrix(translatonMatrix);
      mModel.setRotationMatrix(rotationMatrix);
    }
  }
}