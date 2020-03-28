package com.rnglmodelview;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
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

  /** TODO: This is a breaking change! Expected a String previously. */
  @ReactProp(name = "model")
  public void setModel(RNGLModelView view, @Nullable ReadableMap pModelSource) {
    if (pModelSource == null) return;
    view.setModelUri(pModelSource.getString("uri"));
  }

  /** TODO: This is a breaking change! Expected a String previously. */
  @ReactProp(name = "texture")
  public void setModelTexture(RNGLModelView view, @Nullable ReadableMap pTextureSource) {
    if (pTextureSource == null) return;
    view.setModelTextureUri(pTextureSource.getString("uri"));
  }

  @ReactProp(name = "tint")
  public void setModelTint(RNGLModelView view, @Nullable ReadableMap tintMap) {
    if (tintMap == null) return;

    // The tint property on the react-native side is specified in terms of float values
    int red = tintMap.hasKey("r") ? (int)(tintMap.getDouble("r") * 255) : 1;
    int green = tintMap.hasKey("g") ? (int)(tintMap.getDouble("g") * 255) : 1;
    int blue = tintMap.hasKey("b") ? (int)(tintMap.getDouble("b") * 255) : 1;
    int alpha = tintMap.hasKey("a") ? (int)(tintMap.getDouble("a") * 255) : 1;

    view.setModelTint(red, green, blue, alpha);
  }

  @ReactProp(name = "animate")
  public void setAnimate(RNGLModelView view, @Nullable boolean animate) {
    view.setAnimate(animate);
  }

  @ReactProp(name = "flipTexture")
  public void flipTexture(RNGLModelView view, @Nullable boolean flipped) {
    view.flipTexture(flipped);
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
