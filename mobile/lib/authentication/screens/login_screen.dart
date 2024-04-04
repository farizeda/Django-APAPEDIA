import 'package:flutter/material.dart';
import 'package:mobile/common/components/text_field_component.dart';
import 'package:mobile/authentication/model/login_response.dart';
import 'package:mobile/authentication/screens/registration_screen.dart';
import 'package:mobile/authentication/services/authentication_service.dart';
import 'package:mobile/catalogue/screens/catalogue_screen.dart';
import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/common/services/secure_storage_service.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _loginFormKey = GlobalKey<FormState>();

  String _identity = "";
  String _password = "";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: SafeArea(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [Container(
              decoration: BoxDecoration(
                  borderRadius: const BorderRadius.all(Radius.circular(20)),
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.grey.withOpacity(0.5),
                      spreadRadius: 3,
                      blurRadius: 7,
                      offset: const Offset(0, 5),                  )
                  ]
              ),
              margin: const EdgeInsets.all(12.0),
              padding: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 24.0),
              child: Form(
                key: _loginFormKey,
                child: Column(
                  children: [
                    const Center(child: Text("APAPEDIA", style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: Colors.green
                    ),),),
                    const SizedBox(
                      height: 25,
                    ),
                    TextFieldComponent(
                        keyboardType: TextInputType.emailAddress,
                        labelText: "Email or Username",
                        hintText: "john.doe@mail.com",
                        action: (String? value) {
                          setState(() {
                            _identity = value!;
                          });
                        },
                        validator: (String? value) {
                          if (value == null || value.isEmpty) {
                            return 'Email or username cannot be empty!';
                          }
                          return null;
                        }),
                    const SizedBox(
                      height: 15,
                    ),
                    TextFieldComponent(
                      isTextObscured: true,
                      labelText: "Password",
                      action: (String? value) {
                        setState(() {
                          _password = value!;
                        });
                      },
                      validator: (String? value) {
                        if (value == null || value.isEmpty) {
                          return 'Password cannot be empty!';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(
                      height: 15,
                    ),
                    ElevatedButton(
                        onPressed: _loginButtonPressed,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.green,
                          minimumSize: const Size.fromHeight(50),
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(15)),
                        ),
                        child: const Text(
                          'Login',
                          style: TextStyle(fontSize: 20),
                        )),
                    const SizedBox(
                      height: 15,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Text("Doesn't have an account?",
                            style: TextStyle(
                                fontSize: 16, fontWeight: FontWeight.bold)),
                        const SizedBox(
                          width: 10,
                        ),
                        GestureDetector(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                    const RegistrationScreen()));
                          },
                          child: const Text("Create One!",
                              style: TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.green)),
                        )
                      ],
                    )
                  ],
                ),
              ),
            )],
          )
        ));
  }

  void _loginButtonPressed() async {
    if (_loginFormKey.currentState!.validate()) {
      Map<String, String> body = {
        "identity": _identity,
        "password": _password,
        "type": "Customer"
      };

      CommonResponse<LoginResponse> response = await AuthenticationService.loginUser(body);

      if (response.success) {
        await SecureStorageService.write("token", response.content?.token ?? "");

        Future.delayed(Duration.zero).then((value) =>
            Navigator.of(context).pushReplacement(MaterialPageRoute(
              builder: (context) => const CatalogueScreen(),
            )));
      } else {
        Future.delayed(Duration.zero).then((value) =>
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                content: Text(response.message))));
      }
    }
  }
}

