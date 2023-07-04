import 'dart:async';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  // Create an EventChannel instance

  const MyApp({super.key});


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Background Location Permission',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyWidget(),
    );
  }

  
}



// Create a MethodChannel instance
const MethodChannel _channel = MethodChannel('com.akshay.location');

// Define a method to request the permission
Future<String> checkLocationPermission() async {
  try {
    final String permissionStatus = await _channel.invokeMethod('checkLocationPermission');
    log('permissionStatus ---------> $permissionStatus');
    if(permissionStatus=='DENIED'){
      await openLocationPermission();
    }
    return permissionStatus;
  } on PlatformException catch (e) {
    // Handle exception
    return 'Not All $e';
  }
}
Future<dynamic> openLocationPermission() async {
  try {
    final  permissionStatus = await _channel.invokeMethod('openLocationSettings');
    log('openLocationPermission ---------> $permissionStatus');
    return permissionStatus;
  } on PlatformException catch (e) {
    // Handle exception
    return 'openLocationPermission All $e';
  }
}


class MyWidget extends StatelessWidget {
  const MyWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Background Location Permission'),
        ),
        body: const Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: checkLocationPermission,
                child: Text('Request Permission'),
              ),
              
            ],
          ),
        ),
      );
  }
}