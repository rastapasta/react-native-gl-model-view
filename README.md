# react-native-gl-model-view

[![npm version](https://badge.fury.io/js/react-native-gl-model-view.svg)](https://badge.fury.io/js/react-native-gl-model-view)
![license](https://img.shields.io/github/license/rastapasta/react-native-gl-model-view.svg)

A `<ModelView>` component for [react-native](https://github.com/facebook/react-native), allowing you to
display and animate any Wavefront .OBJ 3D object. Realized with a native bridge to [GLView](https://github.com/nicklockwood/GLView) for iOS and a native bridge to [jPCT-AE](http://www.jpct.net/jpct-ae/) for Android.

<img src="docs/AnimatedAPI.gif" width="32%"/> <img src="docs/GestureResponder.gif" width="32%"/> <img src="docs/Multiple.gif" width="32%" />

Main features:

* Display, rotate, scale and translate textured 3D models!
* Animate with blasting fast 60 fps by using the [Animated API](https://facebook.github.io/react-native/docs/animations.html#using-the-native-driver) native driver
* Supports [Wavefront .OBJ](https://en.wikipedia.org/wiki/Wavefront_.obj_file) and GLEssentials model formats (iOS)
* Supports [Wavefront .OBJ](https://en.wikipedia.org/wiki/Wavefront_.obj_file), [Autodesk 3DS](https://en.wikipedia.org/wiki/.3ds), [Quake 2 MD2](https://en.wikipedia.org/wiki/MD2_(file_format)), [ASC](https://codeyarns.com/2011/08/17/asc-file-format-for-3d-points/) and GLEssentials model formats (Android)
* Supports all texture image formats supported by [UIImage](https://developer.apple.com/library/content/documentation/2DDrawing/Conceptual/DrawingPrintingiOS/LoadingImages/LoadingImages.html#//apple_ref/doc/uid/TP40010156-CH17-SW8)

## Requirements

* Cocoapods (for iOS) - to install the [GLView](https://github.com/nicklockwood/GLView) dependency.

## Getting started

You can install and try linking the project automatically:

```sh
$ npm install --save react-native-gl-model-view
$ react-native link
```

or do it manually as described below:

### iOS Manual installation

`$ npm install --save react-native-gl-model-view`

Afterwards add following lines to your Podfile:

```sh
pod 'React', :path => '../node_modules/react-native'
pod 'RNGLModelView', :path => '../node_modules/react-native-gl-model-view'
```

### Android Manual installation

`$ npm install --save react-native-gl-model-view`

Afterwards, add the following lines to the **android/settings.gradle** file:

```gradle
include ':react-native-gl-model-view'
project(':react-native-gl-model-view').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-gl-model-view/android/app')
```

Finally, add the react-native-gl-model-view project as a dependency of the **android/app/build.gradle** file:

```gradle
dependencies {
    compile project(':react-native-gl-model-view')
    ...
}
```


## Usage

### Model and texture loading

#### iOS

To load a model on iOS, you need to put the file at the root of your Xcode project via the editor.

#### Android

To load a model on Android, you need to place the model in the **android/app/src/main/assets** folder. Create a new folder if it doesn't exist yet.

### Static

```javascript
import ModelView from 'react-native-gl-model-view';

<ModelView
    model="model.obj"
    texture="texture.png"

    scale={0.01}

    translateZ={-2}
    rotateZ={270}

    style={{flex: 1}}
/>
```

### Animated

Make the `<ModelView>`'s native props animatable by wrapping the Animated API around it:

```javascript
import ModelView from 'react-native-gl-model-view';
import { Animated, Easing } from 'react-native';

const AnimatedModelView = Animated.createAnimatedComponent(ModelView);
```

As this usage of the Animated API is *kinda* hacky, you must call the private [__makeNative()](https://github.com/facebook/react-native/blob/master/Libraries/Animated/src/AnimatedImplementation.js#L155) method on all `Animated.Values` before using `Animated.multiply` and such.

```javascript
constructor() {
    this.state = {
        zoom: new Animated.Value(0),
        // ...
    };
    Object.keys(this.state).forEach(key =>
        this.state[key] instanceof Animated.Value &&
        this.state[key].__makeNative()
    );
}
```

Now you can apply all the Animated API magic to the `<AnimatedModelView>`'s props.
```javascript
render() {
    <AnimatedModelView
        ...
        animate={true}
        translateZ={this.state.zoom}
    />
}
componentDidMount() {
    Animated.timing(this.state.zoom, {
        toValue: -2,
        useNativeDriver: true,
        duration: 2000,
        easing: Easing.bounce
    }).start();
}
```

### Properties

| Prop  | Default  | Type | Description |
| :------------ |:---------------:| :---------------:| :-----|
| model | *required* | `string` | Filename of the model, must be included via Xcode |
| texture | undefined | `string` | Filename of the texture, must be included via Xcode |
| animate | false | `bool` | Model re-renders each 1/60s when set to `true` |
| scale | 1 | `number` | Scale all axes of the model by given factor (overwrites scale*) |
| scaleX | 1 | `number` | Scale X axis by given factor |
| scaleY | 1 | `number` | Scale Y axis by given factor |
| scaleZ | 1 | `number` | Scale Z axis by given factor |
| rotateX | 0 | `number` | Rotate around X axis by given degree |
| rotateY | 0 | `number` | Rotate around Y axis by given degree |
| rotateZ | 0 | `number` | Rotate around Z axis by given degree |
| translateX | 0 | `number` | Translate X position by given points |
| translateY | 0 | `number` | Translate Y position by given points |
| translateZ | 0 | `number` | Translate Z position by given points |

### Examples

Check out the [example project](https://github.com/rastapasta/react-native-gl-model-view/tree/master/example/src):

* [Usage of the Animated API](https://github.com/rastapasta/react-native-gl-model-view/blob/master/example/src/Animations.js)
* [Using the GestureResponder to control rotation](https://github.com/rastapasta/react-native-gl-model-view/blob/master/example/src/GestureControl.js)
* [Animating multiple ModelViews at once](https://github.com/rastapasta/react-native-gl-model-view/blob/master/example/src/Multiple.js)

To install the dependencies, switch into the `example` folder and set it up as following:

#### For npm < 5

```sh
$ npm install
```

#### For npm >= 5

npm 5 changed the way `npm install` installs and links local modules. Previously, if a dependency in package.json was using the `file: <relative/path/to/file` protocol, npm install would copy the folder inside node_modules as if it were downloading a module from the npm registry. But since npm 5, `npm install` only creates a symlink to the module. Since the react-native packager doesn't work well with symlinks, it cannot find the react module when running the app. A workaround is to install "instal-local", which is a tool made specifically to fix this npm 5 problem:

```sh
$ npm install -g install-local
$ install-local
```

To build and run the app, set it up as following:

## iOS

```sh
$ cd ios
$ pod install
$ cd ..
$ react-native run-ios
```

## Android

```sh
$ react-native run-android
```

## Backlog

* Bridge to [GLModel.modelWithData](https://github.com/nicklockwood/GLView/blob/master/GLView/Models/GLModel.m#L424) to allow flexbile model sources

## Special thanks

* [nicklockwood](https://github.com/nicklockwood) for his work on [GLView](https://github.com/nicklockwood/GLView/)
* [frostney](https://github.com/frostney) for his work on [react-native-create-library](https://github.com/frostney/react-native-create-library) used to bootstrap this project

## License

#### The MIT License (MIT)

Copyright (c) 2017 Michael Straßburger

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
