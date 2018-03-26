import React, { Component } from 'react';
import {
  StyleSheet,
  Animated,
  Platform
} from 'react-native';

import ModelView from 'react-native-gl-model-view';
const AnimatedModelView = Animated.createAnimatedComponent(ModelView);

export default class GestureControl extends Component {
  constructor() {
    super();
    this.state = {
      animate: false,

      rotateX: new Animated.Value(0),
      rotateZ: new Animated.Value(0),

      fromXY: undefined,
      valueXY: undefined
    };
    Object.keys(this.state).forEach(key =>
      this.state[key] instanceof Animated.Value &&
      this.state[key].__makeNative()
    );
  }

  onMoveEnd = () => {
    this.setState({
      fromXY: undefined,
      animate: false
    });
  }

  onMove = (e) => {
    let { pageX, pageY } = e.nativeEvent,
      { rotateX, rotateZ, fromXY, valueXY } = this.state;
    if (!this.state.fromXY) {
      this.setState({
        animate: true,
        fromXY: [pageX, pageY],
        valueXY: [
          rotateZ.__getValue(),
          rotateX.__getValue()
        ]
      });
    } else {
      rotateZ.setValue(valueXY[0]+(pageX-fromXY[0])/2);
      rotateX.setValue(valueXY[1]+(Platform.OS === 'ios' ? 1 : -1)*(pageY-fromXY[1])/2);
    }
  }

  render() {
    let { animate, rotateZ, rotateX } = this.state;

    return (
      <AnimatedModelView
        model="demon.model"
        texture="demon.png"

        onStartShouldSetResponder={() => true}
        onResponderRelease={this.onMoveEnd}
        onResponderMove={this.onMove}

        animate={animate}

        scale={0.01}
        translateZ={-2}

        rotateX={rotateX}
        rotateZ={rotateZ}

        style={styles.container}
      />
    );
  }
}

const styles = StyleSheet.create({
  container: {flex: 1,backgroundColor: 'transparent'}});
