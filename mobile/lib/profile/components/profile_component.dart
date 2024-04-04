import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/profile/models/get_user_response.dart';
import 'package:mobile/profile/screens/update_profile_screen.dart';

class ProfileComponent extends StatelessWidget {
  const ProfileComponent({super.key, required this.user});

  final GetUserResponse? user;

  @override
  Widget build(BuildContext context) {
    return Container(
        width: double.infinity,
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
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("Profile", style: TextStyle(
                color: Colors.green,
                fontWeight: FontWeight.bold,
                fontSize: 20
            ),),
            const SizedBox(
              height: 10,
            ),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [const Text("Name", style: TextStyle(fontWeight: FontWeight.bold)), Text((user?.name ?? "-"
              ))],),
            const SizedBox(
              height: 10,
            ),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [const Text("Email", style: TextStyle(fontWeight: FontWeight.bold)), Text((user?.email ?? "-"
              ))],),
            const SizedBox(
              height: 10,
            ),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [const Text("Address", style: TextStyle(fontWeight: FontWeight.bold)), Text((user?.address ?? "-"
              ))],),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.orangeAccent,
                  minimumSize: const Size.fromHeight(50),
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(15)),
                ),
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => UpdateProfileScreen(user: user,),
                      ));
                }, child: const Text(
              'Update',
              style: TextStyle(fontSize: 20),
            ))
          ],
        )
    );
  }
}