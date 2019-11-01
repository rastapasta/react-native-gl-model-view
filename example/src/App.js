import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  ScrollView,
  TouchableOpacity,
  Text,
} from 'react-native';

import GestureControl from './GestureControl';
import Animations from './Animations';
import Multiple from './Multiple';
import RuntimeAssets from './RuntimeAssets';

export default class example extends Component {
  state = {
    example: undefined,
  };

  select(example) {
    this.setState({example});
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          {this.state.example && (
            <TouchableOpacity
              onPress={() => this.select()}
              hitSlop={{top: 9, left: 9, bottom: 9, right: 9}}>
              <Text style={styles.backButton}>&lt;</Text>
            </TouchableOpacity>
          )}
          <Text style={styles.headerTitle}>GLModelView Examples</Text>
        </View>

        <View style={styles.body}>{this.renderContent()}</View>
      </View>
    );
  }

  renderContent() {
    if (this.state.example) {
      const Example = this.state.example;
      return <Example />;
    }

    const examples = [
      {component: Animations, info: 'Control via Animated API'},
      {component: GestureControl, info: 'Rotation via Gesture Responder'},
      {component: Multiple, info: 'Using multiple ModelViews'},
      {component: RuntimeAssets, info: 'Initializing ModelViews using Network'},
    ];

    return (
      <ScrollView style={styles.menu}>
        {examples.map((example, i) => {
          const title = i + 1 + '. ' + example.info;
          return (
            <TouchableOpacity
              onPress={this.select.bind(this, example.component)}
              key={example.info}>
              <Text style={styles.button}>{title}</Text>
            </TouchableOpacity>
          );
        })}
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'stretch',
    backgroundColor: 'white',
  },
  header: {
    height: 75,
    paddingTop: 22,
    paddingLeft: 20,
    flexDirection: 'row',
    backgroundColor: '#5894f3',
    alignItems: 'center',
    zIndex: 100,
  },
  body: {
    flex: 1,
    zIndex: 1,
  },
  menu: {
    flex: 1,
    paddingTop: 15,
    paddingLeft: 40,
    backgroundColor: '#fff',
  },
  backButton: {
    color: 'white',
    fontSize: 18,
    width: 30,
  },
  headerTitle: {
    color: 'white',
    fontSize: 20,
    flex: 1,
    textAlign: 'center',
  },
  button: {
    color: '#333',
    fontSize: 20,
    marginBottom: 24,
  },
});
