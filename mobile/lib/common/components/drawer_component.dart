import 'package:flutter/material.dart';
import 'package:mobile/catalogue/screens/catalogue_screen.dart';
import 'package:mobile/profile/screens/profile_screen.dart';

class DrawerComponent extends StatelessWidget {
  const DrawerComponent({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: const EdgeInsets.symmetric(vertical: 30),
        children:  [
          const Text(
            'APAPEDIA',
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: 30,
              fontWeight: FontWeight.bold,
              color: Colors.green,
            ),
          ),
          ListTile(
            leading: const Icon(Icons.store_outlined),
            title: const Text('Catalogue'),
            // Bagian redirection ke MyHomePage
            onTap: () {
              Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const CatalogueScreen(),
                  ));
            },
          ),
          ListTile(
            leading: const Icon(Icons.person_outlined),
            title: const Text('Profile'),
            // Bagian redirection ke MyHomePage
            onTap: () {
              Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const ProfileScreen(),
                  ));
            },
          ),
        ],
      ),
    );
  }
}