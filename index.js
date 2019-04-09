import React, { Component } from 'react';
import { requireNativeComponent, View } from 'react-native';
import PropTypes from 'prop-types';

class GLModelView extends Component {
  render() {
    return <RNGLModelView {...this.props} />;
  }
}

GLModelView.propTypes = {
  ...View.propTypes,
  animate: PropTypes.bool,
  flipTexture: PropTypes.bool,

  model: PropTypes.object,
  texture: PropTypes.object,
  tint: PropTypes.object,

  rotateX: PropTypes.number,
  rotateY: PropTypes.number,
  rotateZ: PropTypes.number,

  scale: PropTypes.number,

  scaleX: PropTypes.number,
  scaleY: PropTypes.number,
  scaleZ: PropTypes.number,

  translateX: PropTypes.number,
  translateY: PropTypes.number,
  translateZ: PropTypes.number
};

var RNGLModelView = requireNativeComponent('RNGLModelView', GLModelView);

export default GLModelView;
