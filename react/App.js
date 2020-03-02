/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, DeviceEventEmitter} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {
  viewIndex: Int
};
export default class App extends Component<Props> {

  constructor(props) {
    super(props)

    console.log("Created", props)
    this.state = {index: 1}
    this.setState({index: 1})
    DeviceEventEmitter.addListener("setData", (data = []) => {
      console.log("Got an event", data)
      console.log("this.props.viewIndex", this.props.viewIndex)
      var viewIdx = data[0]
      if(this.props.viewIndex == viewIdx) {

        var shownData = data[1]
        this.setState({index: shownData})
      }

    })
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>ITEM: {this.state.index ?? 1}</Text>
      </View>
    );
  }
}
console.disableYellowBox = true


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

