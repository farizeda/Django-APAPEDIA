import 'package:flutter/material.dart';
import 'package:mobile/authentication/screens/login_screen.dart';
import 'package:mobile/authentication/services/authentication_service.dart';
import 'package:mobile/cart/screens/cart_screen.dart';
import 'package:mobile/catalogue/screens/catalogue_screen.dart';
import 'package:mobile/order/screens/order_screen.dart';

void main() => runApp(const App());

final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Apapedia',
        debugShowCheckedModeBanner: false,
        navigatorKey: navigatorKey,
        theme: ThemeData(
          primarySwatch: Colors.green,
        ),
        home: FutureBuilder(
          future: AuthenticationService.isAuthenticated(),
          builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
            if (snapshot.data == null) {
              return const Center(child: CircularProgressIndicator());
            } else {
              return snapshot.data!
                  ?
              const OrderScreen()
                  : const OrderScreen();
            }
          },
        ));
  }
}
