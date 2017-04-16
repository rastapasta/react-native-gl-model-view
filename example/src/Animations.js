import React, { Component } from 'react';
import {
  StyleSheet,
  Animated,
  Easing
} from 'react-native';

import ModelView from 'react-native-gl-model-view';
const AnimatedModelView = Animated.createAnimatedComponent(ModelView);

export default class Animations extends Component {
  constructor() {
    super();
    this.state = {
      rotateX: new Animated.Value(0),
      rotateZ: new Animated.Value(0),
      translateZ: new Animated.Value(-20),
    };
    Object.keys(this.state).forEach(key =>
      this.state[key] instanceof Animated.Value &&
      this.state[key].__makeNative()
    );
  }

  componentDidMount() {
    Animated.parallel([
      Animated.timing(
        this.state.translateZ, {
          toValue: -3,
          useNativeDriver: true,
          duration: 4000,
          easing: Easing.elastic(1)
        }
      ),
      Animated.timing(
        this.state.rotateX, {
          toValue: 270,
          useNativeDriver: true,
          duration: 5000,
          easing: Easing.elastic(5)
        }
      )
    ]).start();
  }

  render() {
    let { animate, rotateZ, rotateX, translateZ } = this.state;

    return (
      <AnimatedModelView
        model="demon.model"
        texture="demon.png"

        onStartShouldSetResponder={() => true}
        onResponderRelease={this.onMoveEnd}
        onResponderMove={this.onMove}

        animate={true}

        scale={0.01}
        translateZ={translateZ}

        rotateX={Animated.multiply(rotateX, Math.PI/180)}
        rotateZ={Animated.multiply(rotateZ, Math.PI/180)}

        style={styles.container}        
      />
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent'
  },
});
