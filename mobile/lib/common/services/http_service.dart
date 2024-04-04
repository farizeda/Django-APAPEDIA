import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:mobile/authentication/screens/login_screen.dart';
import 'package:mobile/authentication/services/authentication_service.dart';
import 'package:mobile/common/services/secure_storage_service.dart';
import 'package:mobile/main.dart';
import 'package:path/path.dart' as path;
import 'package:http/http.dart' as http;

enum Service {
  user,
  order,
  catalogue
}

class HttpService {
  static String? _baseUrl;
  static final Map<String, String> headers = {
    "Content-Type": "application/json",
  };

  HttpService(Service service) {
    if (service == Service.user) {
      _baseUrl = "http://10.0.2.2:8082";
      // _baseUrl = "https://7d6141c8-5e86-4522-8d0b-0a0208b17b74.mock.pstmn.io";
    } else if (service == Service.catalogue) {
      _baseUrl = "http://10.0.2.2:8081";
    } else if (service == Service.order) {
      _baseUrl = "http://10.0.2.2:8080";
    }
  }

  Future get(String endpoint, {bool isAuthenticated = true}) async {
    Uri url = Uri.parse(path.join(_baseUrl!, endpoint));

    await _authorizeHeader(isAuthenticated);

    Response response = await http.get(url, headers: headers);
    if (response.statusCode == 401 && endpoint != 'auth/login') {
      _handleUnauthorizedRequest();
    }

    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  Future post(String endpoint, Map<String, dynamic> body,
      {bool isAuthenticated = true}) async {
    Uri url = Uri.parse(path.join(_baseUrl!, endpoint));

    await _authorizeHeader(isAuthenticated);

    Response response =
        await http.post(url, headers: headers, body: jsonEncode(body));

    if (response.statusCode == 401 && endpoint != 'auth/login') {
      _handleUnauthorizedRequest();
    }

    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  Future put(String endpoint, Map<String, dynamic> body,
      {bool isAuthenticated = true}) async {
    Uri url = Uri.parse(path.join(_baseUrl!, endpoint));

    await _authorizeHeader(isAuthenticated);

    Response response =
        await http.put(url, headers: headers, body: jsonEncode(body));

    if (response.statusCode == 401 && endpoint != 'auth/login') {
      _handleUnauthorizedRequest();
    }

    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  Future patch(String endpoint, Map<String, dynamic> body,
      {bool isAuthenticated = true}) async {
    Uri url = Uri.parse(path.join(_baseUrl!, endpoint));

    await _authorizeHeader(isAuthenticated);

    Response response =
    await http.patch(url, headers: headers, body: jsonEncode(body));

    if (response.statusCode == 401 && endpoint != 'auth/login') {
      _handleUnauthorizedRequest();
    }

    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  Future delete(String endpoint,
      {bool isAuthenticated = true}) async {
    Uri url = Uri.parse(path.join(_baseUrl!, endpoint));

    await _authorizeHeader(isAuthenticated);

    Response response =
    await http.delete(url, headers: headers);

    if (response.statusCode == 401 && endpoint != 'auth/login') {
      _handleUnauthorizedRequest();
    }

    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  static Future<void> _authorizeHeader(bool isAuthenticated) async {
    if (isAuthenticated) {
      String? token = await SecureStorageService.read("token");

      if (await AuthenticationService.isAuthenticated()) {
        headers['Authorization'] = "Bearer $token";
      } else {
        navigatorKey.currentState?.pushAndRemoveUntil(
          MaterialPageRoute(builder: (context) => const LoginScreen()),
          (route) => false,
        );
      }
    }
  }

  static void _handleUnauthorizedRequest() async {
    await SecureStorageService.destroyAll();
    navigatorKey.currentState?.pushAndRemoveUntil(
      MaterialPageRoute(builder: (context) => const LoginScreen()),
      (route) => false,
    );
  }
}
