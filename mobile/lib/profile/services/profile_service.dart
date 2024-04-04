import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/common/services/http_service.dart';
import 'package:mobile/profile/models/get_user_response.dart';

class ProfileService {
  static Future<CommonResponse<GetUserResponse>> getUser() async {
    dynamic response = await HttpService(Service.user).get("user");

    CommonResponse<GetUserResponse> data = CommonResponse.fromJson(response, (json) => GetUserResponse.fromJson(json));

    return data;
  }

  static Future<void> deleteUser() async {
    await HttpService(Service.user).delete("user");
  }

  static Future<CommonResponse> topUp(Map<String, String> body) async {
    dynamic response = await HttpService(Service.user).patch("user/balance", body);

    CommonResponse data = CommonResponse.fromJson(response);

    return data;
  }
  
  static Future<CommonResponse> updateProfile(Map<String, String?> body) async {
    dynamic response = await HttpService(Service.user).put("user", body);

    CommonResponse data = CommonResponse.fromJson(response);

    return data;
  }
}