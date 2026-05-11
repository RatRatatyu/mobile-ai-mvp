import 'package:flutter/material.dart';


class UiStateProvider extends ChangeNotifier {


  String _latestImage = "";
  String get latestImagePath  => _latestImage;

  void onTakenPhoto(String imagePath){
    _latestImage = imagePath;
    notifyListeners();
  }
  void onCleanPhoto(){
    _latestImage = "";
    notifyListeners();
  }


}