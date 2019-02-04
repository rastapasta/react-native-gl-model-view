//
//  react-native-gl-model-View
//  https://github.com/rastapasta/react-native-gl-model-view
//
//  RNGLModelViewManager.h
//

#if __has_include("RCTViewManager.h")
#import "RCTViewManager.h"
#else
#import <React/RCTViewManager.h>
#endif

#import "RNGLModelView.h"

@interface RNGLModelViewManager : RCTViewManager

- (void)flipTexture:(RNGLModelView *)view;
- (UIImage *)decodeBase64ToImage:(NSString *)strEncodeData;

@end
