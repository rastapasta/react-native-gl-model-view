import React, {Component} from 'react';
import {StyleSheet, View, Animated} from 'react-native';

import ModelView from 'react-native-gl-model-view';
const AnimatedModelView = Animated.createAnimatedComponent(ModelView);

export default class Multiple extends Component {
  constructor() {
    super();
    this.state = {
      rotateZ: new Animated.Value(0),
    };
  }

  componentDidMount() {
    this.animate(0);
  }

  animate(iteration) {
    Animated.timing(this.state.rotateZ, {
      toValue: ++iteration * 360,
      useNativeDriver: true,
      duration: 5000,
    }).start(this.animate.bind(this, iteration++));
  }

  renderModel() {
    return (
      <AnimatedModelView
        model={{
          uri: 'demon.model',
        }}
        texture={{
          uri: 'demon.png',
        }}
        tint={{r: 1.0, g: 1.0, b: 1.0, a: 1.0}}
        animate
        scale={0.01}
        translateZ={-2.5}
        rotateX={270}
        rotateZ={Animated.add(this.state.rotateZ, Math.random() * 360)}
        style={styles.model}
      />
    );
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.row}>
          {this.renderModel()}
          {this.renderModel()}
        </View>
        <View style={styles.row}>
          {this.renderModel()}
          {this.renderModel()}
        </View>
        <View style={styles.row}>
          {this.renderModel()}
          {this.renderModel()}
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  row: {
    flex: 1,
    flexDirection: 'row',
  },
  model: {
    flex: 1,
    backgroundColor: 'transparent',
  },
});
