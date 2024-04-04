import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';
import 'package:mobile/common/models/common_response.dart';
import 'package:mobile/profile/components/delete_user_button_component.dart';
import 'package:mobile/profile/components/logout_button_component.dart';
import 'package:mobile/profile/components/profile_component.dart';
import 'package:mobile/profile/components/topup_component.dart';
import 'package:mobile/profile/models/get_user_response.dart';
import 'package:mobile/profile/services/profile_service.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<StatefulWidget> createState() => _ProfileScreen();
}

class _ProfileScreen extends State<ProfileScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Profile',
        ),
      ),
      drawer: const DrawerComponent(),
      body: SafeArea(child: FutureBuilder(future: ProfileService.getUser(), builder: (BuildContext context, AsyncSnapshot<CommonResponse<GetUserResponse>> snapshot) {
        if (snapshot.hasError) {
          Future.delayed(Duration.zero).then((value) =>
              ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text(snapshot.error.toString()))));
          return const Center(child: CircularProgressIndicator());
        } else if (snapshot.data == null) {
          return const Center(child: CircularProgressIndicator());
        } else {
          return SingleChildScrollView(child: Column(children: [
            ProfileComponent(user: snapshot.data!.content,),
            TopUpComponent(balance: snapshot.data!.content?.balance ?? 0,),
            Container(
              margin: const EdgeInsets.all(12.0),
              child: const Column(children: [
                 DeleteUserButtonComponent(),
                  SizedBox(
                    height: 10,
                  ),
                 LogoutButtonComponent()
              ]),
            )
          ],));
        }
      }) ),) ;
  }

}
  
