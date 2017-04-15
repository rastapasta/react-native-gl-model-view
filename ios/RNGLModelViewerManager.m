//
//  react-native-gl-model-viewer
//  https://github.com/rastapasta/react-native-gl-model-viewer
//
//  RNGLModelViewerManager.m
//

#if __has_include("RCTBridge.h")
#import "RCTBridge.h"
#else
#import <React/RCTBridge.h>
#endif

#import "RNGLModelViewerManager.h"

@implementation RNGLModelViewerManager
{
  RNGLModelViewer *glModelViewer;
}

RCT_EXPORT_MODULE()

- (UIView *)view
{
  glModelViewer = [[RNGLModelViewer alloc] init];
  return glModelViewer;
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
RCT_CUSTOM_VIEW_PROPERTY(model, NSString, RNGLModelViewer)
{
  view.model = [GLModel modelWithContentsOfFile:[RCTConvert NSString:json]];
}

// Loads a texture - PVR + all formats supported by UIImage
RCT_CUSTOM_VIEW_PROPERTY(texture, NSString, RNGLModelViewer)
{
  view.texture = [GLImage imageNamed:[RCTConvert NSString:json]];
}

// Starts or stops the animation loop to automatically re-render each 1/60 second
RCT_CUSTOM_VIEW_PROPERTY(animate, BOOL, RNGLModelViewer)
{
  view.animated = [RCTConvert BOOL:json];
  if (view.animated) {
    [view startAnimating];
  } else {
    [view stopAnimating];
  }
}

// Applys the current props and rerenders the scene
RCT_EXPORT_METHOD(render)
{
  [glModelViewer display];
}

@end
