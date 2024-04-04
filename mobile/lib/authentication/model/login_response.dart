// To parse this JSON data, do
//
//     final loginResponse = loginResponseFromJson(jsonString);

import 'dart:convert';

LoginResponse loginResponseFromJson(String str) => LoginResponse.fromJson(json.decode(str));

String loginResponseToJson(LoginResponse data) => json.encode(data.toJson());

class LoginResponse {
  String? token;

  LoginResponse({
    required this.token,
  });

  factory LoginResponse.fromJson(Map<String, dynamic>? json) => LoginResponse(
    token: json?["token"],
  );

  Map<String, dynamic> toJson() => {
    "token": token,
  };
}
