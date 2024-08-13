import 'package:custom_app_logo/activityModel.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:custom_app_logo/custom_app_logo.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _customAppLogoPlugin = CustomAppLogo();
  List<ActivityModel> _activties = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    final result = await _customAppLogoPlugin.getActivities();
    setState(() {
      _activties = result;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: ListView.builder(
          itemCount: _activties.length,
          itemBuilder: (context, index) {
            return ListTile(
              onTap: () => showDialog(
                context: context,
                builder: (context) => Dialog(
                  child: Padding(
                    padding: EdgeInsets.all(30),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                       const Text(
                          "are you sure you want to change to this Logo ?",
                          style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                        Row(
                          children: [
                            TextButton(onPressed: () {
                              _activties[index].onChangeActivtiyLogo() ;
                              Navigator.pop(context);
                            }, child: Text("OK")) ,
                            TextButton(onPressed: () => Navigator.pop(context), child: Text("Cancel")) ,
                          ],
                        )
                      ],
                    ),
                  ),
                ),
              ),
              title: Text(_activties[index].activtiyName , style: TextStyle(fontSize: 14,),),
              leading: Image.memory(_activties[index].activtiyLogo),
            );
          },
        )),
      ),
    );
  }
}
