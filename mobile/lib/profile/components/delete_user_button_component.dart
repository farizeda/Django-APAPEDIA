import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/authentication/screens/login_screen.dart';
import 'package:mobile/common/services/secure_storage_service.dart';
import 'package:mobile/profile/services/profile_service.dart';

class DeleteUserButtonComponent extends StatelessWidget {
  const DeleteUserButtonComponent({super.key});

  @override
  Widget build(BuildContext context) {
    void deleteButtonPressed() async {
      await ProfileService.deleteUser();

      await SecureStorageService.destroyAll();

      Future.delayed(Duration.zero).then((value) =>
          Navigator.of(context).pushReplacement(MaterialPageRoute(
            builder: (context) => const LoginScreen(),
          )));
    }

    return ElevatedButton(
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.redAccent,
          minimumSize: const Size.fromHeight(50),
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(15)),
        ),
        onPressed: deleteButtonPressed,
        child: const Text(
          'Delete Account',
          style: TextStyle(fontSize: 20),
        ));
  }


}