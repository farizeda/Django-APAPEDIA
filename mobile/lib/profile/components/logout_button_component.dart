import 'package:flutter/material.dart';
import 'package:mobile/authentication/screens/login_screen.dart';
import 'package:mobile/common/services/secure_storage_service.dart';

class LogoutButtonComponent extends StatelessWidget {
  const LogoutButtonComponent({super.key});

  @override
  Widget build(BuildContext context) {
    void deleteButtonPressed() async {
      await SecureStorageService.destroyAll();

      Future.delayed(Duration.zero).then((value) =>
          Navigator.of(context).pushReplacement(MaterialPageRoute(
            builder: (context) => const LoginScreen(),
          )));
    }

    return ElevatedButton(
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.white24,
          minimumSize: const Size.fromHeight(50),
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(15)),
        ),
        onPressed: deleteButtonPressed,
        child: const Text(
          'Logout',
          style: TextStyle(fontSize: 20),
        ));

  }


}