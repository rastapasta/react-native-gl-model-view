//
//  react-native-gl-model-viewer
//  https://github.com/rastapasta/react-native-gl-model-viewer
//
//  RNGLModelViewer.m
//

#import "RNGLModelViewer.h"

@implementation RNGLModelViewer

- (void)didMoveToWindow
{
  [super didMoveToWindow];

  // Setup GLViewModel
  self.blendColor = nil;
  
  if (self.window) {
    // Render with our applied props!
    [self display];
    
    // Restart animation in case it got stopped because of previous moveOut
    if (self.animated && !self.isAnimating) {
      [self startAnimating];
    }
  } else {
    // Stop the animation loop until the View gets moved back in
    if (self.isAnimating) {
      [self stopAnimating];
    }
  }
}

// Apply the GLViewModel updates before any render outside of the animation loop
- (void)display
{
  [self applyProps];
  [super display];
}

// GLViewModel render loop, gets called self.frameInterval times per second
- (void)step:(NSTimeInterval)dt
{
  [self applyProps];
}

// Apply the current known values of our (maybe Native Animated) props
- (void)applyProps
{
  CATransform3D transform = CATransform3DMakeTranslation(self.translateX,
                                                         self.translateY,
                                                         self.translateZ);
  if (self.scale != 0) {
    transform = CATransform3DScale(transform,
                                   self.scale,
                                   self.scale,
                                   self.scale);
  } else {
    transform = CATransform3DScale(transform,
                                   self.scaleX,
                                   self.scaleY,
                                   self.scaleZ);
  }
  
  if (self.rotateX != 0)
    transform = CATransform3DRotate(transform,
                                    self.rotateX,
                                    1, 0, 0);
  
  if (self.rotateY != 0)
    transform = CATransform3DRotate(transform,
                                    self.rotateY,
                                    0, 1, 0);
  
  if (self.rotateZ != 0)
    transform = CATransform3DRotate(transform,
                                    self.rotateZ,
                                    0, 0, 1);
  
  self.modelTransform = transform;
}

@end
