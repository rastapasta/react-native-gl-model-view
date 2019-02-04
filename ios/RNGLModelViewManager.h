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

FOUNDATION_EXPORT NSString *const HEADER_URI_BASE64_ENCODED;
FOUNDATION_EXPORT NSArray *const SUPPORTED_GEOMETRIES;

@interface RNGLModelViewManager : RCTViewManager

- (void)flipTexture:(RNGLModelView *)view;

@end
