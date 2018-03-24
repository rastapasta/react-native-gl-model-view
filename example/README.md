# GLModelView Examples

To install the dependencies, switch into this folder and set it up as following:

## For npm < 5

```sh
$ npm install
```

## For npm >= 5

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

Note: Sometimes, the `react-native run-ios` and `react-native run-android` commands fail to automatically launch the development server. If you get a "No bundle URL present" error message, open a new console window and type `npm start` in the example folder. This will launch the development server and let you successfully run the Android or ios builds wit
h their respective commands.
