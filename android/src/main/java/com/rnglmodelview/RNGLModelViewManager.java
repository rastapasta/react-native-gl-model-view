package com.rnglmodelview;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;

@ReactModule(name = RNGLModelViewManager.REACT_CLASS)
public class RNGLModelViewManager extends SimpleViewManager<RNGLModelView> {

  protected static final String REACT_CLASS = "RNGLModelView";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  private RNGLModelView mModelView;

  @Override
  public RNGLModelView createViewInstance(ThemedReactContext context) {
    mModelView = new RNGLModelView(context);

    return mModelView;
  }

  @ReactProp(name = "model")
  public void setModel(RNGLModelView view, String modelFileName) {
    view.setModel(modelFileName);
  }

  @ReactProp(name = "texture")
  public void setModelTexture(RNGLModelView view, @Nullable String textureFileName) {
    view.setModelTexture(textureFileName);
  }

  @ReactProp(name = "rotateX")
  public void setModelRotateX(RNGLModelView view, @Nullable float rotateX) {
    view.setModelRotateX(rotateX);
  }

  @ReactProp(name = "rotateY")
  public void setModelRotateY(RNGLModelView view, @Nullable float rotateY) {
    view.setModelRotateY(rotateY);
  }

  @ReactProp(name = "rotateZ")
  public void setModelRotateZ(RNGLModelView view, @Nullable float rotateZ) {
    view.setModelRotateZ(rotateZ);
  }

  @ReactProp(name = "scale")
  public void setModelScale(RNGLModelView view, @Nullable float scale) {
    view.setModelScale(scale);
  }

  @ReactProp(name = "scaleX")
  public void setModelScaleX(RNGLModelView view, @Nullable float scaleX) {
    view.setModelScaleX(scaleX);
  }

  @ReactProp(name = "scaleY")
  public void setModelScaleY(RNGLModelView view, @Nullable float scaleY) {
    view.setModelScaleY(scaleY);
  }

  @ReactProp(name = "scaleZ")
  public void setModelScaleZ(RNGLModelView view, @Nullable float scaleZ) {
    view.setModelScaleZ(scaleZ);
  }

  @ReactProp(name = "translateX")
  public void setModelTranslateX(RNGLModelView view, @Nullable float translateX) {
    view.setModelTranslateX(translateX);
  }

  @ReactProp(name = "translateY")
  public void setModelTranslateY(RNGLModelView view, @Nullable float translateY) {
    view.setModelTranslateY(translateY);
  }

  @ReactProp(name = "translateZ")
  public void setModelTranslateZ(RNGLModelView view, @Nullable float translateZ) {
    view.setModelTranslateZ(translateZ);
  }
}
