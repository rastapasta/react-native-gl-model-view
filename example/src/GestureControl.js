import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  Image,
  ScrollView,
  TouchableOpacity,
  Text
} from 'react-native';

import ModelView from 'react-native-gl-model-view';

export default class GestureControl extends Component {
  render() {
    return (
      <ModelView
        model="demon.model"
        texture="demon.png"

        animated={true}
        scale={0.01}
        
        style={styles.container}
        
        translateZ={-2}
        rotateX={270*Math.PI/180}
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
