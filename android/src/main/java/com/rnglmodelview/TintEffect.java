package com.rnglmodelview;

import com.threed.jpct.ITextureEffect;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;

import android.util.Log;

public class TintEffect implements ITextureEffect {
  RGBColor mTint;

  public TintEffect(RGBColor tint) {
    if (tint != null) {
      mTint = tint;
    }
  }

  @Override
  public void init(Texture tex) {}

  @Override
  public void apply(int[] dest, int[] source) {
    
    if (mTint != null) {
      // The format source bitmap is in the 0xAARRGGBB format
      for (int i = 0; i < source.length; i++) {
        int a = mTint.getAlpha();
        int r = (source[i] & 0x00FF0000) >> 16;
        int g = (source[i] & 0x0000FF00) >> 8;
        int b = (source[i] & 0x000000FF);

        // Optimized from: ((a / 255) * (b / 255)) * 255
        r = (r * mTint.getRed() / 255);
        g = (g * mTint.getGreen() / 255);
        b = b * mTint.getBlue() / 255;

        dest[i] = (a << 24) | (r << 16) | (g << 8) | b;
      }
    }
  }

  @Override
  public boolean containsAlpha() {
    return true;
  }
}