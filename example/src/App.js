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
import Slew from './Slew';
import Animations from './Animations';

export default class example extends Component {
  state = {
    example: undefined
  };

  select(example) {
    this.setState({example});
  }

  render() {
    let top = left = bottom = right = 20;
    return (
      <View style={styles.container}>

        <View style={styles.header}>
          {this.state.example &&
            <TouchableOpacity onPress={() => this.select()} hitSlop={{top, left, bottom, right}}>
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
      {component: Animations, info: 'Control via Animated API'},
      {component: GestureControl, info: 'Rotation via Gesture Responder'},
      {component: Slew, info: 'Show as many as you want'},
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
    width: 30
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
