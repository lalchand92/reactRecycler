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
    this.state = {index: 1, url: 'https://i.picsum.photos/id/866/400/200.jpg'}
    // this.setState({index: 1, url : 'https://i.picsum.photos/id/866/400/200.jpg'})
    DeviceEventEmitter.addListener("setData", (data = []) => {
      console.log("Got an event", data)
      var viewIdx = data[0]
      var shownData = data[1]
      var baseUrl = 'https://i.picsum.photos/id/866/400/200.jpg'

      if(this.props.viewIndex === viewIdx) {
        if(shownData % 4 == 0){
            baseUrl = 'https://i.picsum.photos/id/1000/400/200.jpg'
        } else if(shownData % 4 == 1){
            baseUrl = 'https://i.picsum.photos/id/10/400/200.jpg'
        } else if(shownData % 4 == 2){
            baseUrl = 'https://i.picsum.photos/id/100/400/200.jpg'
        } else if(shownData % 4 == 3){
            baseUrl = 'https://i.picsum.photos/id/1002/400/200.jpg'
        }
      }
      this.setState({
          index: shownData,
          url: baseUrl
      })

    })
  }
  render() {
    const subTitle = 'This is a subtitle text for this expriment.'
    const title = 'Sonar Experiment'
    const {url = '', index} = this.state
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>ITEM: {index ?? 1}</Text>
        <View style={styles.card}>
          <View style={styles.imageContainer}>
            <Image
              source={{uri: url}}
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

