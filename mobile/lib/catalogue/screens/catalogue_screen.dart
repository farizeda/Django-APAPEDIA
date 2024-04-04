import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';

class CatalogueScreen extends StatefulWidget {
  const CatalogueScreen({super.key});

  @override
  State<CatalogueScreen> createState() => _CatalogueScreenState();
}

class _CatalogueScreenState extends State<CatalogueScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Catalogue',
        ),
      ),
        drawer: const DrawerComponent(),
        body: const SafeArea(child: Text("catalogue"),),) ;
  }
}
