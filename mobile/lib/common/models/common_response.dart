import 'dart:convert';

class CommonResponse<T> {
  String message;
  T? content;
  bool success;

  CommonResponse({
    required this.message,
    required this.content,
    required this.success,
  });

  factory CommonResponse.fromJson(Map<String, dynamic> json, [T Function(dynamic)? fromJsonT]) {
    return CommonResponse(
      message: json["message"],
      content: fromJsonT?.call(json["content"]),
      success: json["success"],
    );
  }
}

CommonResponse<T> commonResponseFromJson<T>(String str, [T Function(dynamic)? fromJsonT]) =>
    CommonResponse.fromJson(json.decode(str), fromJsonT);