import 'package:flutter/material.dart';
import 'package:mobile/common/components/text_field_component.dart';
import 'package:mobile/authentication/screens/login_screen.dart';
import 'package:mobile/authentication/services/authentication_service.dart';
import 'package:mobile/common/models/common_response.dart';

class RegistrationScreen extends StatefulWidget {
  const RegistrationScreen({super.key});

  @override
  State<RegistrationScreen> createState() => _RegistrationScreenState();
}

class _RegistrationScreenState extends State<RegistrationScreen> {
  final _registrationFormKey = GlobalKey<FormState>();

  String _username = "";
  String _email = "";
  String _confirmationPassword = "";
  String _password = "";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(child: SingleChildScrollView(
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
                key: _registrationFormKey,
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
                        labelText: "Email",
                        hintText: "john.doe@mail.com",
                        action: (String? value) {
                          setState(() {
                            _email = value!;
                          });
                        },
                        validator: (String? value) {
                          if (value == null || value.isEmpty) {
                            return 'Email cannot be empty!';
                          }
                          return null;
                        }),
                    const SizedBox(
                      height: 15,
                    ),
                    TextFieldComponent(
                        keyboardType: TextInputType.text,
                        labelText: "Username",
                        hintText: "john.doe",
                        action: (String? value) {
                          setState(() {
                            _username = value!;
                          });
                        },
                        validator: (String? value) {
                          if (value == null || value.isEmpty) {
                            return 'Username cannot be empty!';
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
                    TextFieldComponent(
                      isTextObscured: true,
                      labelText: "Confirmation Password",
                      action: (String? value) {
                        setState(() {
                          _confirmationPassword = value!;
                        });
                      },
                      validator: (String? value) {
                        if (value == null || value.isEmpty) {
                          return 'Confirmation password cannot be empty!';
                        } else if (value != _confirmationPassword) {
                          return "Password didn't match";
                        }
                        return null;
                      },
                    ),
                    const SizedBox(
                      height: 15,
                    ),
                    ElevatedButton(
                        onPressed: _registerButtonPressed,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.green,
                          minimumSize: const Size.fromHeight(50),
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(15)),
                        ),
                        child: const Text(
                          'Register',
                          style: TextStyle(fontSize: 20),
                        )),
                    const SizedBox(
                      height: 15,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Text("Already have one?",
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
                                    const LoginScreen()));
                          },
                          child: const Text("Login!",
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
          ),)),
      ),
    );
  }

  void _registerButtonPressed() async {
    if (_registrationFormKey.currentState!.validate()) {
      Map<String, String> body = {
        "username": _username,
        "email": _email,
        "password": _password,
      };

      CommonResponse response = await AuthenticationService.registerUser(body);

      if (response.success) {
        Future.delayed(Duration.zero).then((value) => Navigator.of(context).pushReplacement(MaterialPageRoute(
          builder: (context) => const LoginScreen(),
        )));
      } else {
        Future.delayed(Duration.zero).then((value) =>
            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                content: Text("Oops! Something went wrong"))));
      }
    }
  }
}