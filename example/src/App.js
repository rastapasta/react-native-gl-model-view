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

import GestureControl from './GestureControl';

export default class example extends Component {
  state = {
    example: undefined
  };

  select(example) {
    this.setState({example});
  }

  render() {
    return (
      <View style={styles.container}>

        <View style={styles.header}>
          {this.state.example &&
            <TouchableOpacity onPress={() => this.select()}>
              <Text style={styles.backButton}>&lt;</Text>
            </TouchableOpacity>
          }
          <Text style={styles.headerTitle}>GLModelView Examples</Text>
        </View>

        <View style={styles.body}>
          {this.renderContent()}
        </View>

      </View>
    );
  }

  renderContent() {
    if (this.state.example) {
      const Example = this.state.example;
      return <Example />;
    }

    examples = [
      {component: Text, info: 'Utilizing the Animation Framework'},
      {component: GestureControl, info: 'Rotation via Gesture Responder'},
      {component: Text, info: 'Looped left/right slew'},
    ];

    return (
      <ScrollView style={styles.menuContainer}>
        {examples.map(example =>
          <TouchableOpacity
            onPress={() => this.select(example.component)}
            key={example.info}
          >
            <Text style={styles.button}>{example.info}</Text>
          </TouchableOpacity>
        )}
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
    zIndex: 100
  },
  body: {
    flex: 1,
    zIndex: 1
  },
  menuContainer: {
    flex: 1,
    paddingTop: 15,
    paddingLeft: 40,
    backgroundColor: '#fff'
  },
  backButton: {
    color: 'white',
    fontSize: 18,
    width: 10
  },
  headerTitle: {
    color: 'white',
    fontSize: 20,
    flex: 1,
    textAlign: 'center'
  },
  button: {
    color: '#333',
    fontSize: 20,
    marginBottom: 24
  },
});
