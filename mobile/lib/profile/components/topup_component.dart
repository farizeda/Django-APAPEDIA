import 'package:flutter/material.dart';
import 'package:mobile/profile/screens/top_up_screen.dart';

class TopUpComponent extends StatelessWidget {
  const TopUpComponent({super.key, this.balance = 0});

  final int balance;

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
            const Text("Balance", style: TextStyle(
                color: Colors.green,
                fontWeight: FontWeight.bold,
                fontSize: 20
            ),),
            const SizedBox(
              height: 10,
            ),
            Text("Rp $balance", style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 28)),
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
                        builder: (context) => const TopUpScreen(),
                      ));
                }, child: const Text(
              'Top up',
              style: TextStyle(fontSize: 20),
            ))
          ],
        )
    );
  }
}