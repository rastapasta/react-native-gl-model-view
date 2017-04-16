import React, { Component } from 'react';
import {
  StyleSheet,
  Animated
} from 'react-native';

import ModelView from 'react-native-gl-model-view';
const AnimatedModelView = Animated.createAnimatedComponent(ModelView);

export default class Slew extends Component {
  state = {
    rotateZ: new Animated.Value(0)
  };

  componentDidMount() {
    this.animate();
  }

  animate() {
    Animated.sequence([
      Animated.timing(
        this.state.rotateZ,
        {toValue: 1, duration: 5000, useNativeDriver: true}
      ),
      Animated.timing(
        this.state.rotateZ,
        {toValue: -1, duration: 5000, useNativeDriver: true}
      ),      
    ]).start(() => this.animate());
  }

  render() {
    return (
      <AnimatedModelView
        model="demon.model"
        texture="demon.png"

        animate={true}

        scale={0.01}
        translateZ={-2}

        rotateX={270*Math.PI/180}
        rotateZ={this.state.rotateZ}

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
