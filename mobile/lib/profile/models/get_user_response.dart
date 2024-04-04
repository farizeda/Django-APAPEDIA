// To parse this JSON data, do
//
//     final getUserResponse = getUserResponseFromJson(jsonString);

import 'dart:convert';

GetUserResponse getUserResponseFromJson(String str) => GetUserResponse.fromJson(json.decode(str));

String getUserResponseToJson(GetUserResponse data) => json.encode(data.toJson());

class GetUserResponse {
  String id;
  String? name;
  String username;
  String email;
  int balance;
  String? address;

  GetUserResponse({
    required this.id,
    required this.name,
    required this.username,
    required this.email,
    required this.balance,
    required this.address,
  });

  factory GetUserResponse.fromJson(Map<String, dynamic>? json) => GetUserResponse(
    id: json?["id"],
    name: json?["name"],
    username: json?["username"],
    email: json?["email"],
    balance: json?["balance"],
    address: json?["address"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "name": name,
    "username": username,
    "email": email,
    "balance": balance,
    "address": address,
  };
}
