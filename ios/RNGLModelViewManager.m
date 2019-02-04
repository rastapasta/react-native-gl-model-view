//
//  react-native-gl-model-view
//  https://github.com/rastapasta/react-native-gl-model-view
//
//  RNGLModelViewManager.m
//

#if __has_include("RCTBridge.h")
#import "RCTBridge.h"
#else
#import <React/RCTBridge.h>
#endif

#import "RNGLModelViewManager.h"
#import <UIKit/UIKit.h>

@implementation RNGLModelViewManager
{
  RNGLModelView *glModelView;
  BOOL textureAlreadyFlipped;
  //NSString *textureName;
  NSString *textureUri;
}

RCT_EXPORT_MODULE()

- (UIView *)view
{
  glModelView = [[RNGLModelView alloc] init];
  textureAlreadyFlipped = NO;
  return glModelView;
}

RCT_EXPORT_VIEW_PROPERTY(rotateX, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(rotateY, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(rotateZ, CGFloat);

RCT_EXPORT_VIEW_PROPERTY(scale, CGFloat);

RCT_EXPORT_VIEW_PROPERTY(scaleX, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(scaleY, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(scaleZ, CGFloat);

RCT_EXPORT_VIEW_PROPERTY(translateX, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(translateY, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(translateZ, CGFloat);

// Loads a model - Wavefront (OBJ) or WWDC2010 model format
RCT_CUSTOM_VIEW_PROPERTY(model, NSString, RNGLModelView)
{
  view.model = [GLModel modelWithContentsOfFile:[RCTConvert NSString:json]];
}

// Loads a texture - PVR + all formats supported by UIImage
RCT_CUSTOM_VIEW_PROPERTY(texture, NSString, RNGLModelView)
{
  textureName = [RCTConvert NSString:json];

  if (view.textureFlipped) {
    [self flipTextureForView:view];
  } else {
    view.texture = [GLImage imageNamed:textureName];
  }
}

RCT_CUSTOM_VIEW_PROPERTY(tint, NSDictionary, RNGLModelView)
{
  NSDictionary *tint = [RCTConvert NSDictionary:json];

  // If the alpha is not specified, we assume that the user wants a fully opaque object
  float alpha = [tint objectForKey:@"a"] == nil ? 1.0 : [[tint valueForKey:@"a"] floatValue];
  float red = [tint objectForKey:@"r"] == nil ? 1.0 : [[tint valueForKey:@"r"] floatValue];
  float green = [tint objectForKey:@"g"] == nil ? 1.0 : [[tint valueForKey:@"g"] floatValue];
  float blue = [tint objectForKey:@"b"] == nil ? 1.0 : [[tint valueForKey:@"b"] floatValue];

  view.blendColor = [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

// Starts or stops the animation loop to automatically re-render each 1/60 second
RCT_CUSTOM_VIEW_PROPERTY(animate, BOOL, RNGLModelView)
{
  view.animated = [RCTConvert BOOL:json];
  if (view.animated) {
    [view startAnimating];
  } else {
    [view stopAnimating];
  }
}

// Vertically flips the texture
RCT_CUSTOM_VIEW_PROPERTY(flipTexture, BOOL, RNGLModelView)
{
  view.textureFlipped = [RCTConvert BOOL:json];

  if (view.texture != nil && view.textureFlipped && !textureAlreadyFlipped) {
    [self flipTextureForView:view];
  }
}

// Applys the current props and rerenders the scene
RCT_EXPORT_METHOD(render)
{
  [glModelView display];
}

- (void)flipTextureForView:(RNGLModelView *)view
{
  // Although the GLView documentation has a 'imageWithOrientation' function,
  // it's not actually in the code. And since the library has been deprecated,
  // it will never be added. Therefore, we need to flip the texture manually
  // via UIImage and CGImage methods.
  UIImage *uiImage = [UIImage imageNamed:textureName];
  uiImage = [UIImage imageWithCGImage:(__nonnull CGImageRef)uiImage.CGImage
    scale:1
    orientation:UIImageOrientationDownMirrored];

  view.texture = [GLImage imageWithUIImage:uiImage];

  textureAlreadyFlipped = YES;
}

// Credit to: https://stackoverflow.com/a/11251478/1701465
- (UIImage *)decodeBase64ToImage:(NSString *)strEncodeData {
  NSData *data = [[NSData alloc]initWithBase64EncodedString:strEncodeData options:NSDataBase64DecodingIgnoreUnknownCharacters];
  return [UIImage imageWithData:data];
}

@end
