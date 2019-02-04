import React from 'react';
import {ActivityIndicator, View, StyleSheet, TouchableOpacity, Text} from 'react-native';
import ModelView from 'react-native-gl-model-view';
import {Buffer} from 'buffer';
import axios from 'axios';

class RuntimeAssets extends React.Component {
  constructor(nextProps) {
    super(nextProps);
    this.state = ({
      model: null,
      texture: null,
      error: null,
      loading: false,
    });
    this.fetchDemonFromNetwork = this.fetchDemonFromNetwork.bind(this);
  }
  getContentFromUrl(url, decode = false) {
    return axios({
      method: 'get',
      url,
      responseType: 'blob',
    })
    .then((res) => new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.onloadend = () => resolve(decode ? new Buffer(fileReader.result, 'base64') : fileReader.result);
      fileReader.onerror = reject;
      fileReader.readAsDataURL(res.data); 
    }));
  }
  fetchDemonFromNetwork() {
    this.setState({
      loading: true,
      error: null,
    });
    return Promise.all([
      this.getContentFromUrl(
        'https://github.com/rastapasta/react-native-gl-model-view/raw/master/example/data/demon.model',
      ),
      this.getContentFromUrl(
        'https://github.com/rastapasta/react-native-gl-model-view/raw/master/example/data/demon.png',
      ),
    ])
      .then((binaries) => {
        const model = binaries[0];
        const texture = binaries[1];
        this.setState({
          model,
          texture,
          loading: false,
          error: null,
        });
      })
      //.then(() => new Promise((resolve, reject) => setTimeout(() => reject(), 5000)))
      .catch(e => this.setState({
        loading: false,
        error: e || new Error('Something unexpected has happened.'),
      }));
  }
  renderModel(nextProps, nextState) {
    // expects demon.model and demon.png (standard string)
    // should translate to source={require()} *OR* source={{ uri: }}
    // we will make uri, of form
    // {uri: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADMAAAAzCAYAAAA6oTAqAAAAEXRFWHRTb2Z0d2FyZQBwbmdjcnVzaEB1SfMAAABQSURBVGje7dSxCQBACARB+2/ab8BEeQNhFi6WSYzYLYudDQYGBgYGBgYGBgYGBgYGBgZmcvDqYGBgmhivGQYGBgYGBgYGBgYGBgYGBgbmQw+P/eMrC5UTVAAAAABJRU5ErkJggg=='}
    const {
      model,
      texture,
    } = nextState;
    const textureSrc = ({
      uri: texture,
    }); // was demon.png
    const modelSrc = ({
      // XXX: Is data:geometry a valid format?
      uri: model,
    }); // was demon.model
    // TODO: Need to test this scheme by specifying a conventional URI, too.
    return (
      <ModelView
        style={{flex: 1}}
        model="demon.model"
        texture={textureSrc}
        scale={0.01}
        translateZ={-2.5}
        rotateX={270}
        rotateY={0}
        rotateZ={0}
      />
    );
  }
  renderControls(nextProps, nextState) {
    const {
      error,
      loading,
    } = nextState;
    return (
      <View
        style={styles.controls}
      >
        {(!!loading) && (
          <ActivityIndicator
          />
        )}
        {(!loading) && (
          <TouchableOpacity
            style={styles.controlBox}
            disabled={loading}
            onPress={this.fetchDemonFromNetwork}
          >
            <Text
              style={error? styles.controlTextError : styles.controlText}
            >
              {error ? 'Retry' : 'Load'}
            </Text>
          </TouchableOpacity>
        )}
      </View>
    );
  }
  render() {
    const {
      model,
      texture,
    } = this.state;
    return (
      <View
        style={styles.container}
      >
      {(model && texture) ? this.renderModel(this.props, this.state) : this.renderControls(this.props, this.state)}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  controls: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  controlBox: {
    justifyContent: 'center',
    alignItems: 'center',
    padding: 15,
    borderRadius: 5,
  },
  controlTextError: {
    color: 'red',
    fontSize: 30,
  },
  controlText: {
    color: 'black',
    fontSize: 30,
  },
});

export default RuntimeAssets;
