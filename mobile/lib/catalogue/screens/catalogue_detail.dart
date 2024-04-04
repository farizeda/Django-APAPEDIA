import 'package:flutter/material.dart';
import 'package:mobile/common/components/drawer_component.dart';

class CatalogueDetail extends StatelessWidget {
  final Map<String, dynamic> product;

  CatalogueDetail({required this.product});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          product['title'],
          style: TextStyle(color: Colors.green), // Set the text color to green
        ),
        backgroundColor: Colors.white, // Set the background color if needed
      ),
      drawer: DrawerComponent(),
      body: Center(
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Image.network(product['thumbnail']),
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      product['title'],
                      style:
                          TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    SizedBox(height: 8),
                    Text(
                      product['description'],
                      style: TextStyle(fontSize: 16),
                    ),
                    SizedBox(height: 16),
                    Text(
                      'Price: \$${product['price']}',
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    SizedBox(height: 16),
                    ElevatedButton(
                      onPressed: () {
                        // Add to Cart functionality

                      },
                      child: Text('Add To Cart'),
                    ),
                    SizedBox(height: 8),
                    ElevatedButton(
                      onPressed: () {
                        // Buy Now functionality

                      },
                      child: Text('Buy Now'),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}


// CatalogueDetail(
//        product: {
//          "id": 1,
//          "title": "iPhone 9",
//          "description": "An apple mobile which is nothing like apple",
//          "price": 549,
//          "discountPercentage": 12.96,
//          "rating": 4.69,
//          "stock": 94,
//          "brand": "Apple",
//          "category": "smartphones",
//          "thumbnail": "https://i.dummyjson.com/data/products/1/thumbnail.jpg",
//          "images": [
//            "https://i.dummyjson.com/data/products/1/1.jpg",
//            "https://i.dummyjson.com/data/products/1/2.jpg",
//            "https://i.dummyjson.com/data/products/1/3.jpg",
//            "https://i.dummyjson.com/data/products/1/4.jpg",
//            "https://i.dummyjson.com/data/products/1/thumbnail.jpg"
//          ]
//        }