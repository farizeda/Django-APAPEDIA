import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';
import 'package:mobile/common/components/text_field_component.dart';
import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/profile/screens/profile_screen.dart';
import 'package:mobile/profile/services/profile_service.dart';

class TopUpScreen extends StatefulWidget {
  const TopUpScreen({super.key});

  @override
  State<StatefulWidget> createState() => _TopUpScreen();
}

class _TopUpScreen extends State<TopUpScreen> {
  final _topUpFormKey = GlobalKey<FormState>();

  String _amount = "";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Top Up'),
      ),
      drawer: const DrawerComponent(),
      body: SafeArea(child:Column(
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
            key: _topUpFormKey,
            child: Column(
              children: [
                const SizedBox(
                  height: 25,
                ),
                TextFieldComponent(
                    keyboardType: TextInputType.number,
                    labelText: "Top Up Amount",
                    action: (String? value) {
                      setState(() {
                        _amount = value!;
                      });
                    },
                    validator: (String? value) {
                      if (value == null || value.isEmpty) {
                        return 'Amount cannot be empty!';
                      }
                      return null;
                    }),
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
                          'Top Up',
                          style: TextStyle(fontSize: 20),
                        )))
                  ],
                )
              ],
            ),
          ),
        )],
      ) ),
    );

  }
  void _topUp() async {
    if (_topUpFormKey.currentState!.validate()) {
      Map<String, String> body = {
        "amount": _amount,
        "mutation": "DEBIT"
      };

      CommonResponse response = await ProfileService.topUp(body);

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