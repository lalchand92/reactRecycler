/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, DeviceEventEmitter, Image} from 'react-native';

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
    const baseImage = 'https://i.picsum.photos/id/866/400/200.jpg'
    const subTitle = 'This is a subtitle text for this expriment.'
    const title = 'Sonar Experiment'
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>ITEM: {this.state.index ?? 1}</Text>
        <View style={styles.card}>
          <View style={styles.imageContainer}>
            <Image
              source={{uri: baseImage}}
              style={styles.imageStyle}
            />
          </View>
          <View style={styles.textContainer}>
              <Text style={styles.titleTextStyle}>{title}</Text>
              <Text style={styles.subtitleTextStyle}>{subTitle}</Text>
          </View>

        </View>
      </View>
    );
  }
}
console.disableYellowBox = true


const styles = StyleSheet.create({
  container: {
    backgroundColor: 'white',
    width: 360,
    padding: 8,
    height: '100%'
  },
  subtitleTextStyle: {
    color: 'rgba(0,0,0,0.4)'
  },
  titleTextStyle: {
    fontSize: 16,
    color: 'rgba(0,0,0,6)'
  },
  imageContainer: {
    flex: 0.7
  },
  textContainer: {
    flex: 0.3,
    marginLeft: 8,
    marginRight: 8
  },
  card: {
    flex: 1,
    margin: 4,
    flexDirection: 'row'
  },
  welcome: {
    fontSize: 18,
    textAlign: 'center',
    margin: 10,
  },
  imageStyle: {
    height: 160,
    width: '100%'
  }
});

