import 'dart:typed_data';
import 'package:dio/dio.dart';
import 'package:flutter_image_compress/flutter_image_compress.dart';


final dio = Dio();

Future<List<dynamic>?> apiModel(String path) async {
  final Uint8List? imageBytes = await compressImage(path);

  if (imageBytes == null) {
    print("Ошибка сжатия");
    return null;
  }

  try {
    final response = await dio.post(
      "https://router.huggingface.co/hf-inference/models/google/vit-base-patch16-224",
      data: imageBytes,
      options: Options(
        headers: {
          "Authorization": "Bearer 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",
          "Content-Type": "image/jpeg",
        },
      ),
    );
    print(response.data);
    return response.data;


  } on DioException catch (e) {
    print("Ошибка сети: ${e.message}");

  }
  return null;
}

// Compresses an image and returns the bytes
Future<Uint8List?> compressImage(String path) async {
  final Uint8List? result = await FlutterImageCompress.compressWithFile(
    path,
    quality: 80,
    format: CompressFormat.jpeg,
  );

  return result;
}