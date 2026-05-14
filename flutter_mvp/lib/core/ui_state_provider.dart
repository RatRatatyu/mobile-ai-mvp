import 'package:flutter/material.dart';
import 'package:flutter_mvp/data/models/api_model.dart';

import '../data/models/native_model.dart';


class UiStateProvider extends ChangeNotifier {
  String _latestImage = "";
  String _label = "";
  int _score = 0;
  bool _isLoading = false;
  int _timeTaken = 0;
  bool _isOnDevice =  true;

  String get latestImagePath  => _latestImage;
  String get label => _label;
  int get score => _score;
  bool get isLoading => _isLoading;
  int get timeTaken => _timeTaken;
  bool get isOnDevice => _isOnDevice;


  void onTakenPhoto(String imagePath){
    _latestImage = imagePath;
    notifyListeners();
  }
  void onCleanPhoto(){
    _latestImage = "";
    _label = "";
    _score = 0;
    _timeTaken = 0;
    notifyListeners();
  }

  void onChangeMethod(bool value){
    _isOnDevice = value;
    notifyListeners();
  }

  Future<void> chooseInterface(String image) async{
    await Future.delayed(const Duration(milliseconds: 200));


    final startTime = DateTime.now().millisecondsSinceEpoch;
    _isLoading = true;
    notifyListeners();

    try{
      if (_isOnDevice) {
        final result = await NativeMlService.onDeviceMethod(image);

        if (result != null && result.containsKey('label')) {
          _label = result['label']?.toString() ?? "Unknown";
          var confidenceRaw = result['confidence'] ?? 0;
          _score = (confidenceRaw is double) ? (confidenceRaw * 100).toInt() : confidenceRaw.toInt();
        }
      }else{
        final result = await apiModel(image);
        if (result != null && result.isNotEmpty) {
          _label = result[0]['label']?.toString() ?? "Unknown";
          _score = (((result[0]['score']) ?? 0).toDouble() * 100).round();
        }
      }

      _timeTaken = DateTime.now().millisecondsSinceEpoch - startTime;



    } catch (e) {
      debugPrint("Error: $e");
      _label = "Error analyzing image";
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}