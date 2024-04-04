import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({super.key});

  @override
  State<CartScreen> createState() => _CartPageState();
}

class _CartPageState extends State<CartScreen> {
  List<Product> cartItems = []; // List untuk menyimpan produk dalam keranjang

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Shopping Cart'),
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: cartItems.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(cartItems[index].name),
                  subtitle: Text('Quantity: ${cartItems[index].quantity}'),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: Icon(Icons.remove),
                        onPressed: () {
                          // Mengurangi jumlah produk dalam keranjang
                          setState(() {
                            if (cartItems[index].quantity > 1) {
                              cartItems[index].quantity--;
                            }
                          });
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.add),
                        onPressed: () {
                          // Menambah jumlah produk dalam keranjang
                          setState(() {
                            cartItems[index].quantity++;
                          });
                        },
                      ),
                    ],
                  ),
                );
              },
            ),
          ),
          ElevatedButton(
            onPressed: () {
              // Membuat order dan menuju ke halaman order history
              _placeOrder();
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => OrderHistoryPage()),
              );
            },
            child: Text('Order Now'),
          ),
        ],
      ),
    );
  }

  void _placeOrder() {
    // Simulasi proses pemesanan, bisa terhubung ke backend
    print('Order placed!');
  }
}

class OrderHistoryPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Order History'),
      ),
      body: Center(
        child: Text('Order history will be displayed here.'),
      ),
    );
  }
}

class Product {
  String name;
  int quantity;

  Product(this.name, this.quantity);
}