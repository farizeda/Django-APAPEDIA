import 'package:flutter/material.dart';
import 'package:mobile/cart/screens/cart_screen.dart';
import 'package:mobile/common/components/drawer_component.dart';


class OrderScreen extends StatefulWidget {
  const OrderScreen({super.key});

  @override
  State<OrderScreen> createState() => _OrderScreenSate();
}

class _OrderScreenSate extends State<OrderScreen> {
  List<Order> orders = []; // List untuk menyimpan pesanan

  @override
  void initState() {
    super.initState();
    // Inisialisasi daftar pesanan (contoh)
    orders = [
      Order(
        id: 1,
        status: 'Pending',
        cart: [
          CartItem(productName: 'Product A', quantity: 2),
          CartItem(productName: 'Product B', quantity: 1),
        ],
      ),
      Order(
        id: 2,
        status: 'Shipped',
        cart: [
          CartItem(productName: 'Product C', quantity: 3),
        ],
      ),
      Order(
        id: 3,
        status: 'Delivered',
        cart: [
          CartItem(productName: 'Product A', quantity: 1),
          CartItem(productName: 'Product C', quantity: 2),
        ],
      ),
    ];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Order History'),
      ),
      body: ListView.builder(
        itemCount: orders.length,
        itemBuilder: (context, index) {
          return Card(
            margin: EdgeInsets.all(8.0),
            child: ListTile(
              title: Text('Order #${orders[index].id}'),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Status: ${orders[index].status}'),
                  Text('Items:'),
                  for (var cartItem in orders[index].cart)
                    Text('${cartItem.productName} x${cartItem.quantity}'),
                ],
              ),
              trailing: ElevatedButton(
                onPressed: () {
                  // Menampilkan dialog untuk memperbarui status pesanan
                  _showUpdateStatusDialog(orders[index]);
                },
                child: Text('Update Order Status'),
              ),
            ),
          );
        },
      ),
    );
  }

  void _showUpdateStatusDialog(Order order) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Update Order Status'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Current Status: ${order.status}'),
              TextField(
                onChanged: (value) {
                  // Menyimpan perubahan status pada TextField
                  order.status = value;
                },
                decoration: InputDecoration(labelText: 'New Status'),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                // Simulasi pembaruan status pesanan, bisa terhubung ke backend
                setState(() {
                  // Menutup dialog dan memperbarui tampilan
                  Navigator.pop(context);
                });
              },
              child: Text('Update'),
            ),
          ],
        );
      },
    );
  }
}

class Order {
  int id;
  String status;
  List<CartItem> cart;

  Order({required this.id, required this.status, required this.cart});
}

class CartItem {
  String productName;
  int quantity;

  CartItem({required this.productName, required this.quantity});
}