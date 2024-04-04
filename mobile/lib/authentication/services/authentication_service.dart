import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:mobile/authentication/model/login_response.dart';
import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/common/services/http_service.dart';
import 'package:mobile/common/services/secure_storage_service.dart';

class AuthenticationService {
  static Future<CommonResponse> registerUser(Map<String, String> body) async {
    dynamic response = await HttpService(Service.user).post("auth/registration", body,
        isAuthenticated: false);

    CommonResponse data = CommonResponse.fromJson(response);

    return data;
  }

  static Future<CommonResponse<LoginResponse>> loginUser(Map<String, String> body) async {
    dynamic response =
        await HttpService(Service.user).post("auth/login", body, isAuthenticated: false);
    CommonResponse<LoginResponse> data = CommonResponse.fromJson(response, (json) =>  LoginResponse.fromJson(json));

    return data;
  }

  // static Future<dynamic> logoutUser() async {
  //   String? refreshToken = await SecureStorageService.read("refreshToken");

  //   Map<String, String> body = {"refresh": refreshToken!};

  //   dynamic response = await HttpService.post("auth/logout", body);

  //   return response;
  // }

  static Future<bool> isAuthenticated() async {
    String? token = await SecureStorageService.read("token");

    if (token != null) {
      Map<String, dynamic> decodedAccessToken = JwtDecoder.decode(token);
      return decodedAccessToken['exp'] * 1000 >
          DateTime.now().millisecondsSinceEpoch;
    } else {
      return false;
    }
  }
}
