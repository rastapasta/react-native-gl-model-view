import React from 'react';
import {
  ActivityIndicator,
  View,
  StyleSheet,
  TouchableOpacity,
  Text,
} from 'react-native';
import ModelView from 'react-native-gl-model-view';
import {Buffer} from 'buffer';
import axios from 'axios';

// XXX: This is the standard content header returned for a blob.
const octetStreamHeader = 'data:application/octet-stream;base64,';

class RuntimeAssets extends React.Component {
  state = {
    model: null,
    texture: null,
    error: null,
    loading: false,
  };

  getContentFromUrl(url, decode = false) {
    return axios({
      method: 'get',
      url,
      responseType: 'blob',
    }).then(
      res =>
        new Promise((resolve, reject) => {
          const fileReader = new FileReader();
          fileReader.onloadend = () =>
            resolve(
              decode
                ? new Buffer(fileReader.result, 'base64')
                : fileReader.result,
            );
          fileReader.onerror = reject;
          fileReader.readAsDataURL(res.data);
        }),
    );
  }

  // XXX: The underlying application needs to know the file type of the model.
  //      Therefore, we must change the header returned by axios to something
  //      more indicative of the type.
  formatContent(uri, header) {
    return `${header}${uri.substring(octetStreamHeader.length)}`;
  }

  fetchDemonFromNetwork = () => {
    this.setState({
      loading: true,
      error: null,
    });
    return Promise.all([
      this.getContentFromUrl(
        'https://github.com/rastapasta/react-native-gl-model-view/raw/master/example/data/demon.model',
      ).then(content =>
        this.formatContent(content, 'data:geometry/model;base64,'),
      ),
      this.getContentFromUrl(
        'https://github.com/rastapasta/react-native-gl-model-view/raw/master/example/data/demon.png',
      ),
    ])
      .then(binaries => {
        const model = binaries[0];
        const texture = binaries[1];
        this.setState({
          model,
          texture,
          loading: false,
          error: null,
        });
      })
      .catch(e =>
        this.setState({
          loading: false,
          error: e || new Error('Something unexpected has happened.'),
        }),
      );
  };

  renderModel(nextProps, nextState) {
    const {model, texture} = nextState;
    const textureSrc = {
      uri: texture,
    };
    const modelSrc = {
      uri: model,
    };
    return (
      <ModelView
        style={{flex: 1}}
        model={modelSrc}
        texture={textureSrc}
        scale={0.01}
        translateZ={-2.5}
        rotateX={270}
        rotateY={0}
        rotateZ={0}
        animate
      />
    );
  }
  renderControls(nextProps, nextState) {
    const {error, loading} = nextState;
    return (
      <View style={styles.controls}>
        {!!loading && <ActivityIndicator />}
        {!loading && (
          <TouchableOpacity
            style={styles.controlBox}
            disabled={loading}
            onPress={this.fetchDemonFromNetwork}>
            <Text style={error ? styles.controlTextError : styles.controlText}>
              {error ? 'Retry' : 'Load'}
            </Text>
          </TouchableOpacity>
        )}
      </View>
    );
  }
  render() {
    const {model, texture} = this.state;
    return (
      <View style={styles.container}>
        {model && texture
          ? this.renderModel(this.props, this.state)
          : this.renderControls(this.props, this.state)}
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
