import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:synchronized/synchronized.dart';

class SecureStorageService {
  static const _secureStorage = FlutterSecureStorage();
  static final _lock = Lock();

  static Future<void> write(String key, String value) async {
    await _lock.synchronized(
        () async => await _secureStorage.write(key: key, value: value));
  }

  static Future<String?> read(String key) async {
    return await _secureStorage.read(key: key);
  }

  static Future<void> destroyAll() async {
    await _secureStorage.deleteAll();
  }

  static Future<bool> has(String key) async {
    return await _secureStorage.containsKey(key: key);
  }
}
