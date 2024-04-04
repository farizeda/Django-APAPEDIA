import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';
import 'package:mobile/common/components/text_field_component.dart';
import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/profile/models/get_user_response.dart';
import 'package:mobile/profile/screens/profile_screen.dart';
import 'package:mobile/profile/services/profile_service.dart';

class UpdateProfileScreen extends StatefulWidget {
  const UpdateProfileScreen({super.key, this.user});

  final GetUserResponse? user;
  @override
  State<StatefulWidget> createState() => _UpdateProfileScreen();
}

class _UpdateProfileScreen extends State<UpdateProfileScreen> {
  final _updateProfileFormKey = GlobalKey<FormState>();

  String? _username;
  String? _email;
  String? _address;
  String? _name;
  String? _oldPassword;
  String? _newPassword;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Update Profile'),
      ),
      drawer: const DrawerComponent(),
      body: SafeArea(
        child: Center(child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
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
                key: _updateProfileFormKey,
                child: Column(
                  children: [
                    TextFieldComponent(
                        keyboardType: TextInputType.emailAddress,
                        labelText: "Email",
                        hintText: "john.doe@mail.com",
                        initialValue: widget.user?.email ?? "",
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
                        initialValue: widget.user?.username ?? "",
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
                        keyboardType: TextInputType.text,
                        labelText: "Name",
                        initialValue: widget.user?.name ?? "",
                        action: (String? value) {
                          setState(() {
                            _name = value!;
                          });
                        },
                        validator: (String? value) {
                          if (value == null || value.isEmpty) {
                            return 'Name cannot be empty!';
                          }
                          return null;
                        }),
                    const SizedBox(
                      height: 15,
                    ),
                    TextFieldComponent(
                        keyboardType: TextInputType.text,
                        labelText: "Address",
                        initialValue: widget.user?.address ?? "",
                        action: (String? value) {
                          setState(() {
                            _address = value!;
                          });
                        },
                        validator: (String? value) {
                          if (value == null || value.isEmpty) {
                            return 'Address cannot be empty!';
                          }
                          return null;
                        }),
                    const SizedBox(
                      height: 15,
                    ),
                    TextFieldComponent(
                      isTextObscured: true,
                      labelText: "Old Password",
                      action: (String? value) {
                        setState(() {
                          _oldPassword = value!;
                        });
                      },
                      validator: (String? value) {
                        if (_newPassword != null && (value == null || value.isEmpty)) {
                          return 'Old password cannot be empty!';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(
                      height: 15,
                    ),
                    TextFieldComponent(
                      isTextObscured: true,
                      labelText: "New Password",
                      action: (String? value) {
                        setState(() {
                          _newPassword = value!;
                        });
                      },
                      validator: (String? value) {
                        if (_oldPassword != null && (value == null || value.isEmpty)) {
                          return 'Confirmation password cannot be empty!';
                        } else if (_oldPassword != null && value == _oldPassword) {
                          return "New password must not be the same!";
                        }
                        return null;
                      },
                    ),
                    const SizedBox(
                      height: 15,
                    ),
                    Row(
                      children: [
                        Expanded(child: ElevatedButton(
                            onPressed: () {
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => const ProfileScreen(),
                                  ));
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.white24,
                              minimumSize: const Size.fromHeight(50),
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(15)),
                            ),
                            child: const Text(
                              'Back',
                              style: TextStyle(fontSize: 20),
                            ))),
                        const SizedBox(
                          width: 15,
                        ),
                        Expanded(child: ElevatedButton(
                            onPressed: _topUp,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.orangeAccent,
                              minimumSize: const Size.fromHeight(50),
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(15)),
                            ),
                            child: const Text(
                              'Update',
                              style: TextStyle(fontSize: 20),
                            )))
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
  void _topUp() async {
    if (_updateProfileFormKey.currentState!.validate()) {
      Map<String, String?> body = {
        "newPassword": _newPassword,
        "oldPassword": _oldPassword,
        "name": _name ?? widget.user?.name,
        "address": _address ?? widget.user?.address,
        "email": _email ?? widget.user?.email,
        "username": _username ?? widget.user?.username,
      };

      CommonResponse response = await ProfileService.updateProfile(body);

      if (response.success) {
        Future.delayed(Duration.zero).then((value) =>
            Navigator.of(context).pushReplacement(MaterialPageRoute(
              builder: (context) => const ProfileScreen(),
            )));
      } else {
        Future.delayed(Duration.zero).then((value) =>
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                content: Text(response.message))));
      }
    }
  }
}