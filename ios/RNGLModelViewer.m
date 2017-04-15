//
//  react-native-gl-model-viewer
//  https://github.com/rastapasta/react-native-gl-model-viewer
//
//  RNGLModelViewer.h
//

#import <GLView/GLModelView.h>

@interface RNGLModelViewer : GLModelView

@property (nonatomic, assign) BOOL animated;

@property (nonatomic, assign) CGFloat rotateX;
@property (nonatomic, assign) CGFloat rotateY;
@property (nonatomic, assign) CGFloat rotateZ;

@property (nonatomic, assign) CGFloat scale;

@property (nonatomic, assign) CGFloat scaleX;
@property (nonatomic, assign) CGFloat scaleY;
@property (nonatomic, assign) CGFloat scaleZ;

@property (nonatomic, assign) CGFloat translateX;
@property (nonatomic, assign) CGFloat translateY;
@property (nonatomic, assign) CGFloat translateZ;

@end
