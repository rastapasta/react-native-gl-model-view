import React, {Component} from 'react';
import {StyleSheet, Animated} from 'react-native';

import ModelView from 'react-native-gl-model-view';
const AnimatedModelView = Animated.createAnimatedComponent(ModelView);

export default class GestureControl extends Component {
  constructor() {
    super();
    this.state = {
      rotateX: new Animated.Value(-90),
      rotateZ: new Animated.Value(0),

      fromXY: undefined,
      valueXY: undefined,
    };
  }

  onMoveEnd = () => {
    this.setState({
      fromXY: undefined,
    });
  };

  onMove = e => {
    let {pageX, pageY} = e.nativeEvent,
      {rotateX, rotateZ, fromXY, valueXY} = this.state;
    if (!this.state.fromXY) {
      this.setState({
        fromXY: [pageX, pageY],
        valueXY: [rotateZ.__getValue(), rotateX.__getValue()],
      });
    } else {
      rotateZ.setValue(valueXY[0] + (pageX - fromXY[0]) / 2);
      rotateX.setValue(valueXY[1] + (pageY - fromXY[1]) / 2);
    }
  };

  render() {
    let {rotateZ, rotateX, fromXY} = this.state;

    return (
      <AnimatedModelView
        model={{
          uri: 'demon.model',
        }}
        texture={{
          uri: 'demon.png',
        }}
        onStartShouldSetResponder={() => true}
        onResponderRelease={this.onMoveEnd}
        onResponderMove={this.onMove}
        animate={!!fromXY}
        tint={{r: 1.0, g: 1.0, b: 1.0, a: 1.0}}
        scale={0.01}
        rotateX={rotateX}
        rotateZ={rotateZ}
        translateZ={-4}
        style={styles.container}
      />
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
  },
});
